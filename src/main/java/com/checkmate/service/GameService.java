package com.checkmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.checkmate.model.Game;
import com.checkmate.repository.GameRepository;

import java.util.List;
import java.util.Optional;

import com.checkmate.utils.ChessUtils;

@Service
public class GameService {
    @Autowired
    private final GameRepository gameRepository;
    @Autowired
    private final MoveRepository moveRepository;

    public GameService(GameRepository gameRepository, moveRepository moveRepository) {
        this.moveRepository = moveRepository;
        this.gameRepository = gameRepository;
    }
   
    public Game saveGame(Game game) {
        return gameRepository.saveAndFlush(game);
    }
    public Optional<Game> getGameById(int id) {
        return gameRepository.findById(id);
    }
    public boolean makeMove(Long gameId, int fromRow, int fromCol, int toRow, int toCol) {
    }
}