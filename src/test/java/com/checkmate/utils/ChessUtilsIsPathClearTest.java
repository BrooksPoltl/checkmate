package com.checkmate.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import com.checkmate.model.Board;
import com.checkmate.model.Piece;

@DisplayName("ChessUtils isPathClear tests")
class ChessUtilsIsPathClearTest {
    
    private Board board;
    
    @BeforeEach
    void setUp() {
        board = new Board();
        // Clear the board for testing path clarity
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board.getSquares()[row][col] = null;
            }
        }
    }
    
    @Nested
    @DisplayName("Horizontal path tests")
    class HorizontalPathTests {
        
        @Test
        @DisplayName("Empty horizontal path to the right should be clear")
        void testEmptyHorizontalPathRight() {
            assertTrue(ChessUtils.isPathClear(board, 3, 2, 3, 6));
        }
        
        @Test
        @DisplayName("Empty horizontal path to the left should be clear")
        void testEmptyHorizontalPathLeft() {
            assertTrue(ChessUtils.isPathClear(board, 3, 6, 3, 2));
        }
        
        @Test
        @DisplayName("Blocked horizontal path to the right should not be clear")
        void testBlockedHorizontalPathRight() {
            // Place a blocking piece
            board.getSquares()[3][4] = new Piece("pawn", "white", "♙");
            
            assertFalse(ChessUtils.isPathClear(board, 3, 2, 3, 6));
        }
        
        @Test
        @DisplayName("Blocked horizontal path to the left should not be clear")
        void testBlockedHorizontalPathLeft() {
            // Place a blocking piece
            board.getSquares()[3][4] = new Piece("pawn", "white", "♙");
            
            assertFalse(ChessUtils.isPathClear(board, 3, 6, 3, 2));
        }
        
        @Test
        @DisplayName("Path with blocking piece at the end is not considered blocked")
        void testPathWithBlockingPieceAtEnd() {
            // Place a piece at the destination
            board.getSquares()[3][6] = new Piece("pawn", "black", "♟");
            
            // Path to destination should still be clear (for capturing)
            assertTrue(ChessUtils.isPathClear(board, 3, 2, 3, 6));
        }
    }
    
    @Nested
    @DisplayName("Vertical path tests")
    class VerticalPathTests {
        
        @Test
        @DisplayName("Empty vertical path upward should be clear")
        void testEmptyVerticalPathUp() {
            assertTrue(ChessUtils.isPathClear(board, 5, 3, 1, 3));
        }
        
        @Test
        @DisplayName("Empty vertical path downward should be clear")
        void testEmptyVerticalPathDown() {
            assertTrue(ChessUtils.isPathClear(board, 1, 3, 5, 3));
        }
        
        @Test
        @DisplayName("Blocked vertical path upward should not be clear")
        void testBlockedVerticalPathUp() {
            // Place a blocking piece
            board.getSquares()[3][3] = new Piece("pawn", "white", "♙");
            
            assertFalse(ChessUtils.isPathClear(board, 5, 3, 1, 3));
        }
        
        @Test
        @DisplayName("Blocked vertical path downward should not be clear")
        void testBlockedVerticalPathDown() {
            // Place a blocking piece
            board.getSquares()[3][3] = new Piece("pawn", "white", "♙");
            
            assertFalse(ChessUtils.isPathClear(board, 1, 3, 5, 3));
        }
    }
    
    @Nested
    @DisplayName("Diagonal path tests")
    class DiagonalPathTests {
        
        @Test
        @DisplayName("Empty diagonal path (top-right direction) should be clear")
        void testEmptyDiagonalPathTopRight() {
            assertTrue(ChessUtils.isPathClear(board, 5, 2, 2, 5));
        }
        
        @Test
        @DisplayName("Empty diagonal path (top-left direction) should be clear")
        void testEmptyDiagonalPathTopLeft() {
            assertTrue(ChessUtils.isPathClear(board, 5, 5, 2, 2));
        }
        
        @Test
        @DisplayName("Empty diagonal path (bottom-right direction) should be clear")
        void testEmptyDiagonalPathBottomRight() {
            assertTrue(ChessUtils.isPathClear(board, 2, 2, 5, 5));
        }
        
        @Test
        @DisplayName("Empty diagonal path (bottom-left direction) should be clear")
        void testEmptyDiagonalPathBottomLeft() {
            assertTrue(ChessUtils.isPathClear(board, 2, 5, 5, 2));
        }
        
        @Test
        @DisplayName("Blocked diagonal path should not be clear")
        void testBlockedDiagonalPath() {
            // Place a blocking piece
            board.getSquares()[4][3] = new Piece("pawn", "white", "♙");
            
            assertFalse(ChessUtils.isPathClear(board, 5, 2, 2, 5));
        }
    }
    
    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Path to adjacent square should be clear")
        void testAdjacentSquarePath() {
            assertTrue(ChessUtils.isPathClear(board, 4, 4, 4, 5));
            assertTrue(ChessUtils.isPathClear(board, 4, 4, 5, 4));
            assertTrue(ChessUtils.isPathClear(board, 4, 4, 5, 5));
        }
        
        @Test
        @DisplayName("Path from square to itself should be clear")
        void testSameSquarePath() {
            assertTrue(ChessUtils.isPathClear(board, 4, 4, 4, 4));
        }
        
        @Test
        @DisplayName("Path with multiple blocking pieces should not be clear")
        void testMultipleBlockingPieces() {
            // Place multiple blocking pieces
            board.getSquares()[3][3] = new Piece("pawn", "white", "♙");
            board.getSquares()[3][5] = new Piece("pawn", "black", "♟");
            
            assertFalse(ChessUtils.isPathClear(board, 3, 1, 3, 7));
        }
        
        @Test
        @DisplayName("Path just barely blocked should not be clear")
        void testPathBarelyBlocked() {
            // Place a blocking piece right next to the starting position
            board.getSquares()[4][3] = new Piece("pawn", "white", "♙");
            
            assertFalse(ChessUtils.isPathClear(board, 4, 2, 4, 7));
            
            // Place a blocking piece right before the ending position
            board.getSquares()[4][6] = new Piece("pawn", "white", "♙");
            
            assertFalse(ChessUtils.isPathClear(board, 4, 2, 4, 7));
        }
    }
}
