package com.checkmate.model;

public class Piece {
    private String type; // e.g., "pawn", "rook", "king"
    private String color; // "white" or "black"
    private String symbol; // Unicode symbol (e.g., "♙", "♜")

    public Piece(String type, String color, String symbol) {
        this.type = type;
        this.color = color;
        this.symbol = symbol;
    }

    public String getType() { return type; }
    public String getColor() { return color; }
    public String getSymbol() { return symbol; }

    public void setType(String type) { this.type = type; }
    public void setColor(String color) { this.color = color; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
}
