package com.service.boardservice.repository;

import com.service.boardservice.model.Message;
import com.service.boardservice.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByBoard(Board board);
}
