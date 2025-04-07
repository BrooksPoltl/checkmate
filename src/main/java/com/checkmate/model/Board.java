package com.checkmate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;

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
    
    @Transient
    private Piece[][] squares;
    
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

    public Piece[][] getBoardArray() {
        if (squares != null) {
            return squares;
        }
        
        // Initialize 8x8 board
        squares = new Piece[8][8];
        
        // Parse FEN string
        String[] ranks = boardState.split("/");
        
        for (int rank = 0; rank < 8; rank++) {
            int file = 0;
            for (int i = 0; i < ranks[rank].length() && file < 8; i++) {
                char c = ranks[rank].charAt(i);
                
                if (Character.isDigit(c)) {
                    // Skip empty squares
                    file += Character.getNumericValue(c);
                } else {
                    // Create piece
                    squares[rank][file] = createPieceFromChar(c);
                    file++;
                }
            }
        }
        
        return squares;
    }
    
    /**
     * Creates a Piece object from a FEN character
     * 
     * @param c The FEN character representing a piece
     * @return A new Piece object
     */
    private Piece createPieceFromChar(char c) {
        String color = Character.isUpperCase(c) ? "white" : "black";
        char type = Character.toLowerCase(c);
        
        String pieceType;
        String symbol;
        
        switch (type) {
            case 'p':
                pieceType = "pawn";
                symbol = color.equals("white") ? "♙" : "♟";
                break;
            case 'r':
                pieceType = "rook";
                symbol = color.equals("white") ? "♖" : "♜";
                break;
            case 'n':
                pieceType = "knight";
                symbol = color.equals("white") ? "♘" : "♞";
                break;
            case 'b':
                pieceType = "bishop";
                symbol = color.equals("white") ? "♗" : "♝";
                break;
            case 'q':
                pieceType = "queen";
                symbol = color.equals("white") ? "♕" : "♛";
                break;
            case 'k':
                pieceType = "king";
                symbol = color.equals("white") ? "♔" : "♚";
                break;
            default:
                return null;
        }
        
        return new Piece(pieceType, color, symbol);
    }
    
    /**
     * Updates the board state based on the current pieces array
     */
    public void updateBoardState() {
        if (squares == null) {
            return;
        }
        
        StringBuilder fen = new StringBuilder();
        
        for (int rank = 0; rank < 8; rank++) {
            int emptyCount = 0;
            
            for (int file = 0; file < 8; file++) {
                Piece piece = squares[rank][file];
                
                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    
                    char pieceChar = getPieceChar(piece);
                    fen.append(pieceChar);
                }
            }
            
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }
            
            if (rank < 7) {
                fen.append('/');
            }
        }
        
        this.boardState = fen.toString();
    }
    
    /**
     * Converts a Piece object to its FEN character representation
     * 
     * @param piece The piece to convert
     * @return The FEN character for the piece
     */
    private char getPieceChar(Piece piece) {
        char pieceChar;
        
        switch (piece.getType()) {
            case "pawn":
                pieceChar = 'p';
                break;
            case "rook":
                pieceChar = 'r';
                break;
            case "knight":
                pieceChar = 'n';
                break;
            case "bishop":
                pieceChar = 'b';
                break;
            case "queen":
                pieceChar = 'q';
                break;
            case "king":
                pieceChar = 'k';
                break;
            default:
                return '.';
        }
        
        if (piece.getColor().equals("white")) {
            pieceChar = Character.toUpperCase(pieceChar);
        }
        
        return pieceChar;
    }
    
    /**
     * Gets the squares array (the board representation)
     * 
     * @return The 2D array of Piece objects
     */
    public Piece[][] getSquares() {
        return getBoardArray();
    }
    
    /**
     * Sets the squares array and updates the board state
     * 
     * @param squares The new 2D array of Piece objects
     */
    public void setSquares(Piece[][] squares) {
        this.squares = squares;
        updateBoardState();
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