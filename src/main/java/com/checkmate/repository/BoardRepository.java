package com.checkmate.repository;

import com.checkmate.model.Board;
import com.checkmate.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    
    /**
     * Find the board associated with a specific game
     * 
     * @param game the game entity
     * @return an Optional containing the board if found
     */
    Optional<Board> findByGame(Game game);
}
