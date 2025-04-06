package com.checkmate.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "boards")
public class Board implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game;
    
    @Column(name = "board_state", columnDefinition = "TEXT")
    private String boardState;
    
    @Column(name = "current_turn")
    private String currentTurn = "WHITE"; // Default to WHITE starting
    
    // Default constructor required by JPA
    public Board() {
        initializeDefaultBoard();
    }
    
    public Board(Game game) {
        this.game = game;
        initializeDefaultBoard();
    }
    
    private void initializeDefaultBoard() {
        // Initialize with the standard chess starting position
        // This could be a FEN string representation or your own format
        this.boardState = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    }
    
    // Getters and setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Game getGame() {
        return game;
    }
    
    public void setGame(Game game) {
        this.game = game;
    }
    
    public String getBoardState() {
        return boardState;
    }
    
    public void setBoardState(String boardState) {
        this.boardState = boardState;
    }
    
    public String getCurrentTurn() {
        return currentTurn;
    }
    
    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }
    
    // Method to toggle turn
    public void toggleTurn() {
        this.currentTurn = this.currentTurn.equals("WHITE") ? "BLACK" : "WHITE";
    }
}