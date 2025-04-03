package com.checkmate.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import com.checkmate.model.Board;
import com.checkmate.model.Piece;

@DisplayName("ChessUtils isValidMove tests")
class ChessUtlisIsValidMoveTest {
    
    private Board board;
    private String currentPlayer;
    
    @BeforeEach
    void setUp() {
        board = new Board();
        currentPlayer = "white"; // Start with white's turn
    }
    
    @Nested
    @DisplayName("Pawn movement validation")
    class PawnTests {
        
        @Nested
        @DisplayName("White pawn movement tests")
        class WhitePawnMovementTests {
            
            @Test
            @DisplayName("Can move forward one square")
            void testMoveForwardOneSquare() {
                assertTrue(ChessUtils.isValidMove(board, 6, 4, 5, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move forward two squares from starting position")
            void testMoveForwardTwoSquaresFromStart() {
                assertTrue(ChessUtils.isValidMove(board, 6, 4, 4, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move forward two squares if not on starting position")
            void testCannotMoveTwoSquaresIfNotOnStart() {
                // First move the pawn one square
                ChessUtils.applyMove(board, 6, 4, 5, 4);
                
                // Then try to move it two more squares
                assertFalse(ChessUtils.isValidMove(board, 5, 4, 3, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move forward if blocked")
            void testCannotMoveIfBlocked() {
                // Place a black piece to block white's path
                board.getSquares()[5][4] = new Piece("pawn", "black", "♟");
                
                // Now check if white's pawn can move through the blocking piece
                assertFalse(ChessUtils.isValidMove(board, 6, 4, 4, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can capture diagonally")
            void testCanCaptureDiagonally() {
                // Position a black piece where it can be captured diagonally
                board.getSquares()[5][5] = new Piece("pawn", "black", "♟");
                
                // Attempt the diagonal capture
                assertTrue(ChessUtils.isValidMove(board, 6, 4, 5, 5, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot capture diagonally when no piece is present")
            void testCannotMoveDiagonallyWithoutCapture() {
                assertFalse(ChessUtils.isValidMove(board, 6, 4, 5, 5, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Black pawn movement tests")
        class BlackPawnMovementTests {
            
            @BeforeEach
            void setBlackPlayer() {
                currentPlayer = "black";
            }
            
            @Test
            @DisplayName("Can move forward one square")
            void testMoveForwardOneSquare() {
                assertTrue(ChessUtils.isValidMove(board, 1, 4, 2, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move forward two squares from starting position")
            void testMoveForwardTwoSquaresFromStart() {
                assertTrue(ChessUtils.isValidMove(board, 1, 4, 3, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can capture diagonally")
            void testCanCaptureDiagonally() {
                // Position a white piece where it can be captured diagonally by black
                ChessUtils.applyMove(board, 6, 3, 4, 3);
                
                // Place black pawn at e5
                board.getSquares()[3][4] = new Piece("pawn", "black", "♟");
                
                // Attempt the diagonal capture
                assertTrue(ChessUtils.isValidMove(board, 3, 4, 4, 3, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Invalid pawn movement tests")
        class InvalidPawnMovementTests {
            
            @Test
            @DisplayName("Cannot move backward")
            void testCannotMoveBackward() {
                // Move a white pawn forward
                ChessUtils.applyMove(board, 6, 4, 4, 4);
                
                // Try to move the white pawn backward
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 5, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move horizontally")
            void testCannotMoveHorizontally() {
                assertFalse(ChessUtils.isValidMove(board, 6, 4, 6, 5, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot jump over pieces with two-square move")
            void testCannotJumpOverPieces() {
                // Place a blocking piece in front of the white pawn
                board.getSquares()[5][3] = new Piece("pawn", "black", "♟");
                
                // Check that the pawn can't jump over the piece
                assertFalse(ChessUtils.isValidMove(board, 6, 3, 4, 3, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot capture pieces of the same color")
            void testCannotCaptureSameColorPieces() {
                // Place a white piece diagonally from the pawn
                board.getSquares()[5][5] = new Piece("knight", "white", "♘");
                
                // Now try to "capture" white's own knight with a pawn
                assertFalse(ChessUtils.isValidMove(board, 6, 4, 5, 5, currentPlayer));
            }
            
            @Test
            @DisplayName("En passant capture (TODO)")
            void testEnPassantCapture() {
                // TODO: Implement en passant logic in ChessUtils and then enable this test
            }
        }
    }
    
    // Future test classes for other piece types can be added here
}
