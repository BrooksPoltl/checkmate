package com.checkmate.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.checkmate.utils.ChessUtils;

class PawnTest {
    
    private Board board;
    private String currentPlayer;
    
    @BeforeEach
    void setUp() {
        board = new Board();
        currentPlayer = "white"; // Start with white's turn
    }
    
    @Test
    @DisplayName("White pawn can move forward one square")
    void testWhitePawnMoveForwardOneSquare() {
        assertTrue(ChessUtils.isValidMove(board, 6, 4, 5, 4, currentPlayer));
    }
    
    @Test
    @DisplayName("White pawn can move forward two squares from starting position")
    void testWhitePawnMoveForwardTwoSquaresFromStart() {
        assertTrue(ChessUtils.isValidMove(board, 6, 4, 4, 4, currentPlayer));
    }
    
    @Test
    @DisplayName("White pawn cannot move forward two squares if not on starting position")
    void testWhitePawnCannotMoveTwoSquaresIfNotOnStart() {
        // First move the pawn one square
        ChessUtils.applyMove(board, 6, 4, 5, 4);
        
        // Then try to move it two more squares
        assertFalse(ChessUtils.isValidMove(board, 5, 4, 3, 4, currentPlayer));
    }
    
    @Test
    @DisplayName("White pawn cannot move forward if blocked")
    void testWhitePawnCannotMoveIfBlocked() {
        // Place a black piece to block white's path
        board.getSquares()[5][4] = new Piece("pawn", "black", "♟");
        
        // Now check if white's pawn can move through the blocking piece
        assertFalse(ChessUtils.isValidMove(board, 6, 4, 4, 4, currentPlayer));
    }
    
    @Test
    @DisplayName("White pawn can capture diagonally")
    void testWhitePawnCanCaptureDiagonally() {
        // Position a black piece where it can be captured diagonally
        board.getSquares()[5][5] = new Piece("pawn", "black", "♟");
        
        // Attempt the diagonal capture
        assertTrue(ChessUtils.isValidMove(board, 6, 4, 5, 5, currentPlayer));
    }
    
    @Test
    @DisplayName("White pawn cannot capture diagonally when no piece is present")
    void testWhitePawnCannotMoveDiagonallyWithoutCapture() {
        assertFalse(ChessUtils.isValidMove(board, 6, 4, 5, 5, currentPlayer));
    }
    
    @Test
    @DisplayName("Black pawn can move forward one square")
    void testBlackPawnMoveForwardOneSquare() {
        // Set current player to black
        currentPlayer = "black";
        
        assertTrue(ChessUtils.isValidMove(board, 1, 4, 2, 4, currentPlayer));
    }
    
    @Test
    @DisplayName("Black pawn can move forward two squares from starting position")
    void testBlackPawnMoveForwardTwoSquaresFromStart() {
        // Set current player to black
        currentPlayer = "black";
        
        assertTrue(ChessUtils.isValidMove(board, 1, 4, 3, 4, currentPlayer));
    }
    
    @Test
    @DisplayName("Black pawn can capture diagonally")
    void testBlackPawnCanCaptureDiagonally() {
        // Position a white piece where it can be captured diagonally by black
        ChessUtils.applyMove(board, 6, 3, 4, 3);
        
        // Set current player to black
        currentPlayer = "black";
        
        // Place black pawn at e5
        board.getSquares()[3][4] = new Piece("pawn", "black", "♟");
        
        // Attempt the diagonal capture
        assertTrue(ChessUtils.isValidMove(board, 3, 4, 4, 3, currentPlayer));
    }
    
    @Test
    @DisplayName("Pawn cannot move backward")
    void testPawnCannotMoveBackward() {
        // Move a white pawn forward
        ChessUtils.applyMove(board, 6, 4, 4, 4);
        
        // Try to move the white pawn backward
        assertFalse(ChessUtils.isValidMove(board, 4, 4, 5, 4, currentPlayer));
    }
    
    @Test
    @DisplayName("Pawn cannot move horizontally")
    void testPawnCannotMoveHorizontally() {
        assertFalse(ChessUtils.isValidMove(board, 6, 4, 6, 5, currentPlayer));
    }
    
    @Test
    @DisplayName("Pawn cannot jump over pieces with two-square move")
    void testPawnCannotJumpOverPieces() {
        // Place a blocking piece in front of the white pawn
        board.getSquares()[5][3] = new Piece("pawn", "black", "♟");
        
        // Check that the pawn can't jump over the piece
        assertFalse(ChessUtils.isValidMove(board, 6, 3, 4, 3, currentPlayer));
    }
    
    @Test
    @DisplayName("Pawn cannot capture pieces of the same color")
    void testPawnCannotCaptureSameColorPieces() {
        // Place a white piece diagonally from the pawn
        board.getSquares()[5][5] = new Piece("knight", "white", "♘");
        
        // Now try to "capture" white's own knight with a pawn
        assertFalse(ChessUtils.isValidMove(board, 6, 4, 5, 5, currentPlayer));
    }
    
    @Test
    @DisplayName("Pawn en passant capture")
    void testPawnEnPassantCapture() {
        // TODO: Implement en passant logic in ChessUtils and then enable this test
    }
}
