package com.checkmate.model;

import java.io.Serializable;

/**
 * Represents a chess piece with its type, color, and movement state.
 * This is a transient class (not persisted directly to database) used for game logic.
 */
public class Piece implements Serializable {
    private String type;
    private String color;
    private String symbol;
    private boolean hasMoved;
    
    /**
     * Default constructor
     */
    public Piece() {
    }
    
    /**
     * Create a new chess piece
     * 
     * @param type The type of piece (pawn, rook, knight, bishop, queen, king)
     * @param color The color of the piece (white, black)
     * @param symbol The Unicode symbol for the piece
     */
    public Piece(String type, String color, String symbol) {
        this.type = type;
        this.color = color;
        this.symbol = symbol;
        this.hasMoved = false;
    }
    
    /**
     * Get the type of piece
     * 
     * @return The piece type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Set the type of piece
     * 
     * @param type The piece type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Get the color of the piece
     * 
     * @return The piece color
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Set the color of the piece
     * 
     * @param color The piece color
     */
    public void setColor(String color) {
        this.color = color;
    }
    
    /**
     * Get the Unicode symbol for the piece
     * 
     * @return The piece symbol
     */
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Set the Unicode symbol for the piece
     * 
     * @param symbol The piece symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    /**
     * Check if the piece has moved
     * 
     * @return true if the piece has moved, false otherwise
     */
    public boolean getHasMoved() {
        return hasMoved;
    }
    
    /**
     * Set whether the piece has moved
     * 
     * @param hasMoved true if the piece has moved, false otherwise
     */
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    
    @Override
    public String toString() {
        return symbol;
    }
}
