package com.boardhub.boardhub.domain.post.scheduler;

import com.boardhub.boardhub.domain.post.entity.Post;
import com.boardhub.boardhub.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ViewCountScheduler {

    private final RedisTemplate<String, String> redisTemplate;
    private final PostRepository postRepository;

    // ✅ 1분마다 실행 (60000ms)
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void syncViewCountToDatabase() {
        // 1. "viewCount:" 로 시작하는 모든 키 찾기 (Keys 명령어는 부하가 있을 수 있으니 실무에선 Scan 사용 권장, 지금은 간단히)
        Set<String> keys = redisTemplate.keys("viewCount:*");

        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {
            // 2. 게시글 ID 추출 ("viewCount:1" -> 1)
            Long postId = Long.parseLong(key.split(":")[1]);

            // 3. Redis에서 조회수 가져오기
            String viewCountStr = redisTemplate.opsForValue().get(key);
            long viewCount = Long.parseLong(viewCountStr);

            // 4. DB에 반영 (더티 체킹 or 쿼리)
            // 여기서는 덮어쓰기 방식으로 구현 (Redis가 항상 최신이라고 가정)
            Post post = postRepository.findById(postId).orElse(null);
            if (post != null) {
                // Post 엔티티에 setViewCount 같은 메서드가 필요하거나, 필드 직접 수정 불가하면 메서드 추가 필요
                // 편의상 Post 엔티티에 메서드를 추가했다고 가정합니다.
                post.syncViewCount(viewCount);

                // (선택) 반영 후 Redis 키 삭제? -> 누적 조회수라면 삭제 안 함.
                // 방문자 수 같은 일회성이면 삭제. 여기서는 "총 조회수"니까 유지.
            }
        }
        System.out.println("✅ 조회수 동기화 완료: " + keys.size() + "건");
    }
}