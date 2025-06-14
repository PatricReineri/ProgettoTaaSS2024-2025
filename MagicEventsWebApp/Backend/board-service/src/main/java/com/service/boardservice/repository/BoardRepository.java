package com.service.boardservice.repository;

import com.service.boardservice.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByEventID(Long eventID);
    void deleteByEventID(Long eventID);
}
