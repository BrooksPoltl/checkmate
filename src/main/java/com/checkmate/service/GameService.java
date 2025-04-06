package com.checkmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.checkmate.model.Game;
import com.checkmate.model.Board;
import com.checkmate.model.Move;
import com.checkmate.repository.GameRepository;
import com.checkmate.repository.MoveRepository;
import com.checkmate.repository.BoardRepository;

import java.util.List;
import java.util.Optional;

import com.checkmate.utils.ChessUtils;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final MoveRepository moveRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public GameService(GameRepository gameRepository, MoveRepository moveRepository, BoardRepository boardRepository) {
        this.moveRepository = moveRepository;
        this.gameRepository = gameRepository;
        this.boardRepository = boardRepository;
    }
   
    public Game saveGame(Game game) {
        // Initialize and save the board for a new game
        if (game.getId() == null) {
            Board board = new Board(game);
            boardRepository.save(board);
            game.setBoard(board);
        }
        return gameRepository.saveAndFlush(game);
    }
    
    public Optional<Game> getGameById(int id) {
        return gameRepository.findById(id);
    }
    
    public boolean makeMove(Long gameId, int fromRow, int fromCol, int toRow, int toCol) {
        Optional<Game> gameOpt = gameRepository.findById(gameId.intValue());
        if (!gameOpt.isPresent()) {
            return false;
        }
        
        Game game = gameOpt.get();
        Board board = game.getBoard();
        
        // Validate the move using ChessUtils or your game logic
        if (ChessUtils.isValidMove(board, fromRow, fromCol, toRow, toCol)) {
            // Update the board state
            board = ChessUtils.makeMove(board, fromRow, fromCol, toRow, toCol);
            boardRepository.save(board);
            
            // Create and save the move record
            Move move = new Move(game, fromRow, fromCol, toRow, toCol);
            moveRepository.save(move);
            
            return true;
        }
        
        return false;
    }
    
    public Board getBoardForGame(Game game) {
        return boardRepository.findByGame(game)
            .orElseGet(() -> {
                Board newBoard = new Board(game);
                return boardRepository.save(newBoard);
            });
    }
}