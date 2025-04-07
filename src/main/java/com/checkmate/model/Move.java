package com.checkmate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
@Table(name = "moves")
public class Move {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    
    @Column(name = "from_row")
    private int fromRow;
    
    @Column(name = "from_col")
    private int fromCol;
    
    @Column(name = "to_row")
    private int toRow;
    
    @Column(name = "to_col")
    private int toCol;
    
    @Column(name = "move_notation")
    private String moveNotation;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Default constructor required by JPA
    public Move() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Move(Game game, int fromRow, int fromCol, int toRow, int toCol) {
        this.game = game;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.createdAt = LocalDateTime.now();
        // Generate chess notation like "e2e4" or "a7a8q" for promotion
        this.moveNotation = generateNotation(fromRow, fromCol, toRow, toCol);
    }
    
    private String generateNotation(int fromRow, int fromCol, int toRow, int toCol) {
        char fromFile = (char) ('a' + fromCol);
        char toFile = (char) ('a' + toCol);
        int fromRank = 8 - fromRow;
        int toRank = 8 - toRow;
        
        return String.format("%c%d%c%d", fromFile, fromRank, toFile, toRank);
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
    
    public int getFromRow() {
        return fromRow;
    }
    
    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }
    
    public int getFromCol() {
        return fromCol;
    }
    
    public void setFromCol(int fromCol) {
        this.fromCol = fromCol;
    }
    
    public int getToRow() {
        return toRow;
    }
    
    public void setToRow(int toRow) {
        this.toRow = toRow;
    }
    
    public int getToCol() {
        return toCol;
    }
    
    public void setToCol(int toCol) {
        this.toCol = toCol;
    }
    
    public String getMoveNotation() {
        return moveNotation;
    }
    
    public void setMoveNotation(String moveNotation) {
        this.moveNotation = moveNotation;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}