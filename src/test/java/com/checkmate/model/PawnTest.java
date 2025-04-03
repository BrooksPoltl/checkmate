package com.checkmate.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class PawnTest {
    
    // Test utility classes
    enum Color { WHITE, BLACK }
    
    class Position {
        private int row;
        private int col;
        
        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
        
        public int getRow() { return row; }
        public int getCol() { return col; }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Position position = (Position) obj;
            return row == position.row && col == position.col;
        }
    }
    
    abstract class Piece {
        private Color color;
        private Position position;
        
        public Piece(Color color, Position position) {
            this.color = color;
            this.position = position;
        }
        
        public Color getColor() { return color; }
        public Position getPosition() { return position; }
        public void setPosition(Position position) { this.position = position; }
        
        public abstract boolean isValidMove(Board board, Position destination);
    }
    
    class Board {
        private Piece[][] pieces = new Piece[8][8];
        
        public void placePiece(Piece piece, Position position) {
            pieces[position.getRow()][position.getCol()] = piece;
        }
        
        public void movePiece(Piece piece, Position destination) {
            Position oldPosition = piece.getPosition();
            pieces[oldPosition.getRow()][oldPosition.getCol()] = null;
            pieces[destination.getRow()][destination.getCol()] = piece;
            piece.setPosition(destination);
        }
        
        public Piece getPiece(Position position) {
            return pieces[position.getRow()][position.getCol()];
        }
        
        public boolean isOccupied(Position position) {
            return getPiece(position) != null;
        }
    }
    
    class Pawn extends Piece {
        private boolean justMadeDoubleMove = false;
        
        public Pawn(Color color, Position position) {
            super(color, position);
        }
        
        public boolean isJustMadeDoubleMove() {
            return justMadeDoubleMove;
        }
        
        public void setJustMadeDoubleMove(boolean justMadeDoubleMove) {
            this.justMadeDoubleMove = justMadeDoubleMove;
        }
        
        @Override
        public boolean isValidMove(Board board, Position destination) {
            Position current = getPosition();
            int rowDiff = destination.getRow() - current.getRow();
            int colDiff = destination.getCol() - current.getCol();
            
            // Basic direction check
            if (getColor() == Color.WHITE && rowDiff >= 0) return false; // White moves up (decreasing row)
            if (getColor() == Color.BLACK && rowDiff <= 0) return false; // Black moves down (increasing row)
            
            // Forward movement (no capture)
            if (colDiff == 0) {
                // One square forward
                if (Math.abs(rowDiff) == 1) {
                    return !board.isOccupied(destination);
                }
                
                // Two squares forward from starting position
                if (Math.abs(rowDiff) == 2) {
                    int startRow = (getColor() == Color.WHITE) ? 6 : 1;
                    if (current.getRow() != startRow) return false;
                    
                    Position middle = new Position(current.getRow() + (rowDiff / 2), current.getCol());
                    return !board.isOccupied(middle) && !board.isOccupied(destination);
                }
            }
            
            // Diagonal capture
            if (Math.abs(colDiff) == 1 && Math.abs(rowDiff) == 1) {
                // Normal capture
                if (board.isOccupied(destination)) {
                    Piece targetPiece = board.getPiece(destination);
                    return targetPiece.getColor() != getColor();
                }
                
                // En passant capture
                Position enPassantPosition = new Position(current.getRow(), destination.getCol());
                if (board.isOccupied(enPassantPosition)) {
                    Piece targetPiece = board.getPiece(enPassantPosition);
                    if (targetPiece instanceof Pawn && 
                        targetPiece.getColor() != getColor() && 
                        ((Pawn) targetPiece).isJustMadeDoubleMove()) {
                        return true;
                    }
                }
            }
            
            return false;
        }
    }
    
    // Test instance variables
    private Board board;
    private Pawn whitePawn;
    private Pawn blackPawn;
    
    @BeforeEach
    void setUp() {
        board = new Board();
        whitePawn = new Pawn(Color.WHITE, new Position(6, 4)); // e2 position (white pawn)
        blackPawn = new Pawn(Color.BLACK, new Position(1, 4)); // e7 position (black pawn)
        
        // Place the pawns on the board
        board.placePiece(whitePawn, whitePawn.getPosition());
        board.placePiece(blackPawn, blackPawn.getPosition());
    }
    
    @Test
    @DisplayName("White pawn can move forward one square")
    void testWhitePawnMoveForwardOneSquare() {
        Position destination = new Position(5, 4); // e3
        assertTrue(whitePawn.isValidMove(board, destination));
    }
    
    @Test
    @DisplayName("White pawn can move forward two squares from starting position")
    void testWhitePawnMoveForwardTwoSquaresFromStart() {
        Position destination = new Position(4, 4); // e4
        assertTrue(whitePawn.isValidMove(board, destination));
    }
    
    @Test
    @DisplayName("White pawn cannot move forward two squares if not on starting position")
    void testWhitePawnCannotMoveTwoSquaresIfNotOnStart() {
        // Move the pawn away from starting position
        whitePawn.setPosition(new Position(5, 4)); // Move to e3
        board.movePiece(whitePawn, new Position(5, 4));
        
        Position destination = new Position(3, 4); // Try to move to e5 (two squares)
        assertFalse(whitePawn.isValidMove(board, destination));
    }
    
    @Test
    @DisplayName("White pawn cannot move forward if blocked")
    void testWhitePawnCannotMoveIfBlocked() {
        // Place a piece in front of the white pawn
        Piece blockingPiece = new Pawn(Color.BLACK, new Position(5, 4)); // e3
        board.placePiece(blockingPiece, blockingPiece.getPosition());
        
        assertFalse(whitePawn.isValidMove(board, new Position(5, 4))); // Try to move to e3
    }
    
    @Test
    @DisplayName("White pawn can capture diagonally")
    void testWhitePawnCanCaptureDiagonally() {
        // Place an enemy piece at a diagonal
        Piece enemyPiece = new Pawn(Color.BLACK, new Position(5, 5)); // f3
        board.placePiece(enemyPiece, enemyPiece.getPosition());
        
        assertTrue(whitePawn.isValidMove(board, new Position(5, 5))); // Try to capture to f3
    }
    
    @Test
    @DisplayName("White pawn cannot capture diagonally when no piece is present")
    void testWhitePawnCannotMoveDiagonallyWithoutCapture() {
        assertFalse(whitePawn.isValidMove(board, new Position(5, 5))); // Try to move to f3 with no piece
    }
    
    @Test
    @DisplayName("Black pawn can move forward one square")
    void testBlackPawnMoveForwardOneSquare() {
        Position destination = new Position(2, 4); // e6
        assertTrue(blackPawn.isValidMove(board, destination));
    }
    
    @Test
    @DisplayName("Black pawn can move forward two squares from starting position")
    void testBlackPawnMoveForwardTwoSquaresFromStart() {
        Position destination = new Position(3, 4); // e5
        assertTrue(blackPawn.isValidMove(board, destination));
    }
    
    @Test
    @DisplayName("Black pawn can capture diagonally")
    void testBlackPawnCanCaptureDiagonally() {
        // Place an enemy piece at a diagonal
        Piece enemyPiece = new Pawn(Color.WHITE, new Position(2, 3)); // d6
        board.placePiece(enemyPiece, enemyPiece.getPosition());
        
        assertTrue(blackPawn.isValidMove(board, new Position(2, 3))); // Try to capture to d6
    }
    
    @Test
    @DisplayName("Pawn cannot move backward")
    void testPawnCannotMoveBackward() {
        // White pawn moving backward
        assertFalse(whitePawn.isValidMove(board, new Position(7, 4))); // e1 (backward)
        
        // Black pawn moving backward
        assertFalse(blackPawn.isValidMove(board, new Position(0, 4))); // e8 (backward)
    }
    
    @Test
    @DisplayName("Pawn cannot move horizontally")
    void testPawnCannotMoveHorizontally() {
        // White pawn moving horizontally
        assertFalse(whitePawn.isValidMove(board, new Position(6, 5))); // f2 (horizontal)
        
        // Black pawn moving horizontally
        assertFalse(blackPawn.isValidMove(board, new Position(1, 3))); // d7 (horizontal)
    }
    
    @Test
    @DisplayName("Pawn cannot jump over pieces with two-square move")
    void testPawnCannotJumpOverPieces() {
        // Place a piece in path of white pawn's two-square move
        Piece blockingWhite = new Pawn(Color.BLACK, new Position(5, 4)); // e3
        board.placePiece(blockingWhite, blockingWhite.getPosition());
        assertFalse(whitePawn.isValidMove(board, new Position(4, 4))); // Try to move to e4
        
        // Place a piece in path of black pawn's two-square move
        Piece blockingBlack = new Pawn(Color.WHITE, new Position(2, 4)); // e6
        board.placePiece(blockingBlack, blockingBlack.getPosition());
        assertFalse(blackPawn.isValidMove(board, new Position(3, 4))); // Try to move to e5
    }
    
    @Test
    @DisplayName("Pawn cannot capture pieces of the same color")
    void testPawnCannotCaptureSameColorPieces() {
        // White pawn trying to capture white piece
        Piece whitePiece = new Pawn(Color.WHITE, new Position(5, 5)); // f3
        board.placePiece(whitePiece, whitePiece.getPosition());
        assertFalse(whitePawn.isValidMove(board, new Position(5, 5))); // Try to capture to f3
        
        // Black pawn trying to capture black piece
        Piece blackPiece = new Pawn(Color.BLACK, new Position(2, 3)); // d6
        board.placePiece(blackPiece, blackPiece.getPosition());
        assertFalse(blackPawn.isValidMove(board, new Position(2, 3))); // Try to capture to d6
    }
    
    @Test
    @DisplayName("Pawn en passant capture")
    void testPawnEnPassantCapture() {
        // Setup en passant scenario for white pawn
        Pawn enemyPawn = new Pawn(Color.BLACK, new Position(4, 5)); // f4
        enemyPawn.setJustMadeDoubleMove(true);
        board.placePiece(enemyPawn, enemyPawn.getPosition());
        
        // Move white pawn to prepare for en passant
        whitePawn.setPosition(new Position(4, 4)); // e4
        board.movePiece(whitePawn, new Position(4, 4));
        
        // Test en passant capture
        assertTrue(whitePawn.isValidMove(board, new Position(3, 5))); // Try en passant to f5
    }
}
