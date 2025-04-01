package com.checkmate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "move_number")
    private int moveNumber;

    @Column(name = "from_row")
    private int fromRow;

    @Column(name = "from_col")
    private int fromCol;

    @Column(name = "to_row")
    private int toRow;

    @Column(name = "to_col")
    private int toCol;

    @Column(name = "player")
    private String player; // "white" or "black"

    public Move() {}

    public Move(Game game, int moveNumber, int fromRow, int fromCol, int toRow, int toCol, String player) {
        this.game = game;
        this.moveNumber = moveNumber;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.player = player;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public int getMoveNumber() { return moveNumber; }
    public void setMoveNumber(int moveNumber) { this.moveNumber = moveNumber; }
    public int getFromRow() { return fromRow; }
    public void setFromRow(int fromRow) { this.fromRow = fromRow; }
    public int getFromCol() { return fromCol; }
    public void setFromCol(int fromCol) { this.fromCol = fromCol; }
    public int getToRow() { return toRow; }
    public void setToRow(int toRow) { this.toRow = toRow; }
    public int getToCol() { return toCol; }
    public void setToCol(int toCol) { this.toCol = toCol; }
    public String getPlayer() { return player; }
    public void setPlayer(String player) { this.player = player; }
}