package com.boardhub.boardhub;

import com.boardhub.boardhub.domain.board.entity.Board;
import com.boardhub.boardhub.domain.board.repository.BoardRepository;
import com.boardhub.boardhub.domain.member.entity.Member;
import com.boardhub.boardhub.domain.member.repository.MemberRepository;
import com.boardhub.boardhub.domain.post.entity.Post;
import com.boardhub.boardhub.domain.post.repository.PostRepository;
import com.boardhub.boardhub.domain.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") // ✅ application-test.yml 설정 사용
class BoardhubApplicationTests {

	@Autowired
	private PostService postService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Test
	@DisplayName("동시성 이슈 확인: 100명이 동시에 조회하면 조회수가 100이 되어야 한다.")
	void viewCountConcurrencyTest() throws InterruptedException {
		// 1. 테스트 데이터 준비
		// ✅ 이메일과 유저네임을 고유하게 설정
		Member member = memberRepository.save(Member.builder()
				.email("test_user@test.com")
				.password("1234")
				.nickname("테스트유저")
				.username("test_user_1") // ✅ 필수 필드 누락 방지
				.role(com.boardhub.boardhub.domain.member.entity.Role.USER)
				.build());

		// ✅ 게시판 코드를 "free"가 아닌 "test-free"로 설정 (DataInit 충돌 방지)
		Board board = boardRepository.save(Board.builder()
				.code("test-free")
				.name("테스트게시판")
				.build());

		Post post = postRepository.save(Post.builder()
				.title("테스트 게시글")
				.content("내용")
				.member(member)
				.board(board)
				.build());

		Long postId = post.getId();

		// 2. 동시성 테스트 환경 설정
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// 3. 100번 동시에 요청 쏘기
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					postService.findById(postId);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		// 4. 결과 검증
		Post resultPost = postRepository.findById(postId).orElseThrow();
		System.out.println("최종 조회수: " + resultPost.getViewCount());

		// 100번 불렀으니 100이어야 함! (실패 예상: Expected 100, Actual 34...)
		assertThat(resultPost.getViewCount()).isEqualTo(100);
	}
}