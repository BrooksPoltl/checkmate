package com.checkmate.repository;

import com.checkmate.model.Move;
import com.checkmate.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<Move, Integer> {
    
    /**
     * Find all moves for a specific game, ordered by creation time
     * 
     * @param game the game entity
     * @return a list of moves
     */
    List<Move> findByGameOrderByCreatedAtAsc(Game game);
}
