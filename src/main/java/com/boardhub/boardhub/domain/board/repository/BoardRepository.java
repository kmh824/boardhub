package com.boardhub.boardhub.domain.board.repository;

import com.boardhub.boardhub.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByCode(String code); // 코드(free, humor)로 게시판 찾기
}