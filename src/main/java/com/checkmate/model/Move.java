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
    private int moveNumber; // e.g., 1, 2, 3 (incremented per move)

    @Column(name = "player")
    private String player; // "white" or "black"

    @Column(name = "notation")
    private String notation; // e.g., "e2e4", "Nf3"

    // Constructors
    public Move() {}

    public Move(Game game, int moveNumber, String player, String notation) {
        this.game = game;
        this.moveNumber = moveNumber;
        this.player = player;
        this.notation = notation;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public int getMoveNumber() { return moveNumber; }
    public void setMoveNumber(int moveNumber) { this.moveNumber = moveNumber; }
    public String getPlayer() { return player; }
    public void setPlayer(String player) { this.player = player; }
    public String getNotation() { return notation; }
    public void setNotation(String notation) { this.notation = notation; }
}