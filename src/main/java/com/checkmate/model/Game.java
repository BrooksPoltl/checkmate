package com.checkmate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import java.util.List;
import java.time.LocalDateTime;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "white_player_id")
    private String whitePlayerId;

    @Column(name = "black_player_id")
    private String blackPlayerId;

    @Column(name = "current_player")
    private String currentPlayer; // "white" or "black"

    @OneToMany(mappedBy = "game")
    private List<Move> moves;
    
    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Board board;

    @Column(name = "status")
    private String status; // "ACTIVE", "FINISHED"

    @Column(name = "winner")
    private String winner; // null, "white", "black", "draw"

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Game() {
        this.status = "ACTIVE";
        this.currentPlayer = "white";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Game(String whitePlayerId, String blackPlayerId) {
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.status = "ACTIVE";
        this.currentPlayer = "white";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Add constructor that takes an integer player ID
    public Game(Integer playerId) {
        this.whitePlayerId = playerId.toString();
        this.status = "ACTIVE";
        this.currentPlayer = "white";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getWhitePlayerId() { return whitePlayerId; }
    public void setWhitePlayerId(String whitePlayerId) { this.whitePlayerId = whitePlayerId; }
    public String getBlackPlayerId() { return blackPlayerId; }
    public void setBlackPlayerId(String blackPlayerId) { this.blackPlayerId = blackPlayerId; }
    public String getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(String currentPlayer) { this.currentPlayer = currentPlayer; }
    public List<Move> getMoves() { return moves; }
    public void setMoves(List<Move> moves) { this.moves = moves; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Add board getter and setter
    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
}