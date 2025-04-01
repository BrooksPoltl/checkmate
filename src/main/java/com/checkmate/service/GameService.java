package com.checkmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.checkmate.model.Game;
import com.checkmate.repository.GameRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    @Autowired
    private final GameRepository gameRepository;
   
    /**
     * Method to get all games.
     * @param game - the game to save.
     * @return A created game
     */
    public User saveGame(Game game) {
        return gameRepository.saveAndFlush(game);
    }
    /**
     * Method to get all games.
     * @param game - the game to save.
     * @return A game
     */
    public Optional<Game> getGameById(int id) {
        return gameRepository.findById(id);
    }
}