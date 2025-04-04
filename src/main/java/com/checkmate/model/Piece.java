package com.checkmate.model;

public class Piece {
    private String type; // e.g., "pawn", "rook", "king"
    private String color; // "white" or "black"
    private String symbol; // Unicode symbol (e.g., "♙", "♜")
    private boolean hasMoved; // Flag to track if the piece has moved

    public Piece(String type, String color, String symbol) {
        this.type = type;
        this.color = color;
        this.symbol = symbol;
        this.hasMoved = false; // Default to false for new pieces
    }

    public String getType() { return type; }
    public String getColor() { return color; }
    public String getSymbol() { return symbol; }
    public boolean getHasMoved() { return hasMoved; }

    public void setType(String type) { this.type = type; }
    public void setColor(String color) { this.color = color; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }
}
