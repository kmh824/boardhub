package com.boardhub.boardhub.global.init;

import com.boardhub.boardhub.domain.board.entity.Board;
import com.boardhub.boardhub.domain.board.repository.BoardRepository;
import com.boardhub.boardhub.domain.member.entity.Member;
import com.boardhub.boardhub.domain.member.entity.Role;
import com.boardhub.boardhub.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository; // 테스트용 아이디도 하나 만들면 편하니까
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 1. 게시판 초기 데이터 생성
        if (boardRepository.count() == 0) {
            boardRepository.save(Board.builder().code("free").name("자유게시판").description("자유롭게 글을 쓰는 곳").build());
            boardRepository.save(Board.builder().code("humor").name("유머게시판").description("웃긴 자료 올리는 곳").build());
            System.out.println("✅ 초기 게시판 데이터(free, humor) 생성 완료!");
        }

        // 2. 테스트용 관리자 계정 생성 (개발 편의성)
        if (memberRepository.findByEmail("admin@test.com").isEmpty()) {
            memberRepository.save(Member.builder()
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .username("관리자")
                    .nickname("관리자")
                    .role(Role.ADMIN)
                    .build());
            System.out.println("✅ 테스트용 관리자 계정(admin@test.com) 생성 완료!");
        }
    }
}