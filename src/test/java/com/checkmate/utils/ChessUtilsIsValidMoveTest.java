package com.checkmate.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import com.checkmate.model.Board;
import com.checkmate.model.Piece;

@DisplayName("ChessUtils isValidMove tests")
class ChessUtilsIsValidMoveTest {
    
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
    
    @Nested
    @DisplayName("Rook movement validation")
    class RookTests {
        
        @BeforeEach
        void setUpRookTests() {
            // Remove all pieces from the board to simplify testing
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    board.getSquares()[row][col] = null;
                }
            }
        }
        
        @Nested
        @DisplayName("Basic Rook movement tests")
        class BasicRookMovementTests {
            
            @BeforeEach
            void setUpRookOnBoard() {
                // Place a white rook in the middle of the board for flexible testing
                board.getSquares()[4][4] = new Piece("rook", "white", "♖");
            }
            
            @Test
            @DisplayName("Can move horizontally to the right")
            void testMoveHorizontallyRight() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move horizontally to the left")
            void testMoveHorizontallyLeft() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 0, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move vertically upward")
            void testMoveVerticallyUp() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 0, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move vertically downward")
            void testMoveVerticallyDown() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move to adjacent squares")
            void testMoveToAdjacentSquares() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 5, currentPlayer)); // Right
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 3, currentPlayer)); // Left
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 4, currentPlayer)); // Up
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 4, currentPlayer)); // Down
            }
        }
        
        @Nested
        @DisplayName("Rook capture tests")
        class RookCaptureTests {
            
            @BeforeEach
            void setUpRookAndOpponentPieces() {
                // Place a white rook in the middle of the board
                board.getSquares()[4][4] = new Piece("rook", "white", "♖");
                
                // Place some opponent pieces for capture tests
                board.getSquares()[4][7] = new Piece("pawn", "black", "♟");
                board.getSquares()[7][4] = new Piece("knight", "black", "♞");
            }
            
            @Test
            @DisplayName("Can capture horizontally")
            void testCaptureHorizontally() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Can capture vertically")
            void testCaptureVertically() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot capture pieces of the same color")
            void testCannotCaptureSameColorPieces() {
                // Place a white piece in the rook's path
                board.getSquares()[4][6] = new Piece("pawn", "white", "♙");
                
                // Try to "capture" the white pawn
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 6, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Invalid Rook movement tests")
        class InvalidRookMovementTests {
            
            @BeforeEach
            void setUpRookWithObstacles() {
                // Place a white rook in the middle of the board
                board.getSquares()[4][4] = new Piece("rook", "white", "♖");
                
                // Place obstacles in the rook's path
                board.getSquares()[4][6] = new Piece("pawn", "white", "♙"); // Same color
                board.getSquares()[2][4] = new Piece("pawn", "black", "♟"); // Opponent
            }
            
            @Test
            @DisplayName("Cannot move diagonally")
            void testCannotMoveDiagonally() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 2, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 2, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 6, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot jump over pieces")
            void testCannotJumpOverPieces() {
                // Try to jump over the white pawn horizontally
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
                
                // Try to jump over the black pawn vertically
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 0, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move like other pieces")
            void testCannotMoveInLShape() {
                // Try to move like a knight
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 5, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 3, currentPlayer));
            }
            
            @Test
            @DisplayName("Can stop at opponent piece for capture but not move past it")
            void testCannotMovePastOpponentPiece() {
                // Can capture the black pawn at (2,4)
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 4, currentPlayer));
                
                // Cannot move past it to (1,4)
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 1, 4, currentPlayer));
                
                // Cannot move past it to (0,4)
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 0, 4, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Black Rook movement tests")
        class BlackRookMovementTests {
            
            @BeforeEach
            void setUpBlackRook() {
                // Place a black rook in the middle of the board
                board.getSquares()[3][3] = new Piece("rook", "black", "♜");
                currentPlayer = "black"; // Switch to black's turn
            }
            
            @Test
            @DisplayName("Black rook can move horizontally and vertically")
            void testBlackRookMovement() {
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 7, currentPlayer)); // Horizontal
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 7, 3, currentPlayer)); // Vertical
            }
            
            @Test
            @DisplayName("Black rook can capture white pieces")
            void testBlackRookCapture() {
                // Place a white piece for the black rook to capture
                board.getSquares()[3][7] = new Piece("pawn", "white", "♙");
                
                // Black rook should be able to capture the white pawn
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 7, currentPlayer));
            }
        }
    }
    
    @Nested
    @DisplayName("Bishop movement validation")
    class BishopTests {
        
        @BeforeEach
        void setUpBishopTests() {
            // Clear the board for testing
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    board.getSquares()[row][col] = null;
                }
            }
        }
        
        @Nested
        @DisplayName("Basic Bishop movement tests")
        class BasicBishopMovementTests {
            
            @BeforeEach
            void setUpBishopOnBoard() {
                // Place a white bishop in the middle of the board for flexible testing
                board.getSquares()[4][4] = new Piece("bishop", "white", "♗");
            }
            
            @Test
            @DisplayName("Can move diagonally top-right")
            void testMoveDiagonallyTopRight() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 1, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move diagonally top-left")
            void testMoveDiagonallyTopLeft() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 1, 1, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move diagonally bottom-right")
            void testMoveDiagonallyBottomRight() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move diagonally bottom-left")
            void testMoveDiagonallyBottomLeft() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 1, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move to adjacent diagonal squares")
            void testMoveToAdjacentDiagonalSquares() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer)); // Top-left
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 5, currentPlayer)); // Top-right
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 3, currentPlayer)); // Bottom-left
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 5, currentPlayer)); // Bottom-right
            }
            
            @Test
            @DisplayName("Maintains color-bound movement")
            void testMaintainsColorBound() {
                // A bishop stays on the same color squares throughout the game
                
                // Place a bishop on a white square
                board.getSquares()[4][4] = new Piece("bishop", "white", "♗");
                
                // Check that moving diagonally keeps it on the same color
                // In this case, all moves should end on white squares
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 5, currentPlayer)); // Still on white
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 2, currentPlayer)); // Still on white
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer)); // Still on white
            }
        }
        
        @Nested
        @DisplayName("Bishop capture tests")
        class BishopCaptureTests {
            
            @BeforeEach
            void setUpBishopAndOpponentPieces() {
                // Place a white bishop in the middle of the board
                board.getSquares()[4][4] = new Piece("bishop", "white", "♗");
                
                // Place some opponent pieces in diagonal paths
                board.getSquares()[2][2] = new Piece("pawn", "black", "♟"); // Top-left
                board.getSquares()[7][7] = new Piece("knight", "black", "♞"); // Bottom-right
            }
            
            @Test
            @DisplayName("Can capture diagonally")
            void testCaptureDiagonally() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 2, currentPlayer)); // Top-left
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer)); // Bottom-right
            }
            
            @Test
            @DisplayName("Cannot capture pieces of the same color")
            void testCannotCaptureSameColorPieces() {
                // Place friendly pieces in the bishop's path
                board.getSquares()[3][3] = new Piece("pawn", "white", "♙"); // Top-left
                board.getSquares()[6][6] = new Piece("knight", "white", "♘"); // Bottom-right
                
                // Try to "capture" the white pieces
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Invalid Bishop movement tests")
        class InvalidBishopMovementTests {
            
            @BeforeEach
            void setUpBishopWithObstacles() {
                // Place a white bishop in the middle of the board
                board.getSquares()[4][4] = new Piece("bishop", "white", "♗");
                
                // Place obstacles in the bishop's path
                board.getSquares()[3][3] = new Piece("pawn", "white", "♙"); // Friendly piece on top-left
                board.getSquares()[6][6] = new Piece("pawn", "black", "♟"); // Opponent on bottom-right
            }
            
            @Test
            @DisplayName("Cannot move horizontally or vertically")
            void testCannotMoveHorizontallyOrVertically() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer)); // Horizontal
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 4, currentPlayer)); // Vertical
            }
            
            @Test
            @DisplayName("Cannot move in non-diagonal paths")
            void testCannotMoveNonDiagonally() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 7, currentPlayer)); // Not a diagonal
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer)); // Not a diagonal
            }
            
            @Test
            @DisplayName("Cannot jump over pieces")
            void testCannotJumpOverPieces() {
                // Try to jump over the white pawn diagonally
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 1, 1, currentPlayer));
                
                // Try to jump over the black pawn diagonally
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move like other pieces")
            void testCannotMoveInLShape() {
                // Try to move like a knight
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 5, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 3, currentPlayer));
            }
            
            @Test
            @DisplayName("Can stop at opponent piece for capture but not move past it")
            void testCannotMovePastOpponentPiece() {
                // Can capture the black pawn at (6,6)
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
                
                // Cannot move past it to (7,7)
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Black Bishop movement tests")
        class BlackBishopMovementTests {
            
            @BeforeEach
            void setUpBlackBishop() {
                // Place a black bishop in the middle of the board
                board.getSquares()[3][3] = new Piece("bishop", "black", "♝");
                currentPlayer = "black"; // Switch to black's turn
            }
            
            @Test
            @DisplayName("Black bishop can move diagonally")
            void testBlackBishopMovement() {
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 5, 5, currentPlayer)); // Bottom-right
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 1, 1, currentPlayer)); // Top-left
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 1, 5, currentPlayer)); // Top-right
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 5, 1, currentPlayer)); // Bottom-left
            }
            
            @Test
            @DisplayName("Black bishop can capture white pieces")
            void testBlackBishopCapture() {
                // Place a white piece for the black bishop to capture
                board.getSquares()[5][5] = new Piece("pawn", "white", "♙");
                
                // Black bishop should be able to capture the white pawn
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 5, 5, currentPlayer));
            }
        }
    }
    
    @Nested
    @DisplayName("Knight movement validation")
    class KnightTests {
        
        @BeforeEach
        void setUpKnightTests() {
            // Clear the board for testing
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    board.getSquares()[row][col] = null;
                }
            }
        }
        
        @Nested
        @DisplayName("Basic Knight movement tests")
        class BasicKnightMovementTests {
            
            @BeforeEach
            void setUpKnightOnBoard() {
                // Place a white knight in the middle of the board for flexible testing
                board.getSquares()[4][4] = new Piece("knight", "white", "♘");
            }
            
            @Test
            @DisplayName("Can move in all eight L-shaped directions")
            void testMoveInLShape() {
                // Up 2, Right 1
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer));
                
                // Up 2, Left 1
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 3, currentPlayer));
                
                // Down 2, Right 1
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 6, 5, currentPlayer));
                
                // Down 2, Left 1
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 6, 3, currentPlayer));
                
                // Right 2, Up 1
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 6, currentPlayer));
                
                // Right 2, Down 1
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 6, currentPlayer));
                
                // Left 2, Up 1
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 2, currentPlayer));
                
                // Left 2, Down 1
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 2, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move from a corner")
            void testMoveFromCorner() {
                // Place knight in the corner
                board.getSquares()[4][4] = null;
                board.getSquares()[0][0] = new Piece("knight", "white", "♘");
                
                // Only two possible moves from corner
                assertTrue(ChessUtils.isValidMove(board, 0, 0, 2, 1, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 0, 0, 1, 2, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move from the edge of the board")
            void testMoveFromEdge() {
                // Place knight on an edge
                board.getSquares()[4][4] = null;
                board.getSquares()[0][3] = new Piece("knight", "white", "♘");
                
                // Knight on top edge has 4 possible moves
                assertTrue(ChessUtils.isValidMove(board, 0, 3, 1, 1, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 0, 3, 1, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 0, 3, 2, 2, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 0, 3, 2, 4, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Knight capture tests")
        class KnightCaptureTests {
            
            @BeforeEach
            void setUpKnightAndOpponentPieces() {
                // Place a white knight in the middle of the board
                board.getSquares()[4][4] = new Piece("knight", "white", "♘");
                
                // Place some opponent pieces for capture tests
                board.getSquares()[2][5] = new Piece("pawn", "black", "♟");
                board.getSquares()[6][3] = new Piece("pawn", "black", "♟");
            }
            
            @Test
            @DisplayName("Can capture with L-shaped moves")
            void testCaptureWithLShapedMove() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 6, 3, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot capture pieces of the same color")
            void testCannotCaptureSameColorPieces() {
                // Place a white piece at knight's landing spot
                board.getSquares()[2][5] = new Piece("pawn", "white", "♙");
                
                // Try to "capture" the white pawn
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer));
            }
            
            @Test
            @DisplayName("Can capture and pass through blocking pieces")
            void testCanCaptureAndJumpOverPieces() {
                // Place a blocking piece in the way (knight should be able to jump over it)
                board.getSquares()[3][4] = new Piece("pawn", "black", "♟");
                
                // Knight can still reach its destination and capture
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Invalid Knight movement tests")
        class InvalidKnightMovementTests {
            
            @BeforeEach
            void setUpKnight() {
                // Place a white knight in the middle of the board
                board.getSquares()[4][4] = new Piece("knight", "white", "♘");
            }
            
            @Test
            @DisplayName("Cannot move horizontally")
            void testCannotMoveHorizontally() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 6, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 2, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move vertically")
            void testCannotMoveVertically() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 4, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move diagonally")
            void testCannotMoveDiagonally() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 2, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 6, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 2, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move to non-L-shaped positions")
            void testCannotMoveToNonLShapedPositions() {
                // Not L-shaped (3-square straight move)
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
                
                // Not L-shaped (2,2 move - this is diagonal)
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
                
                // Not L-shaped (3,1 move)
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 5, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Black Knight movement tests")
        class BlackKnightMovementTests {
            
            @BeforeEach
            void setUpBlackKnight() {
                // Place a black knight in the middle of the board
                board.getSquares()[3][3] = new Piece("knight", "black", "♞");
                currentPlayer = "black"; // Switch to black's turn
            }
            
            @Test
            @DisplayName("Black knight can make L-shaped moves")
            void testBlackKnightMovement() {
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 1, 2, currentPlayer)); // Up 2, Left 1
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 1, 4, currentPlayer)); // Up 2, Right 1
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 5, 2, currentPlayer)); // Down 2, Left 1
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 5, 4, currentPlayer)); // Down 2, Right 1
            }
            
            @Test
            @DisplayName("Black knight can capture white pieces")
            void testBlackKnightCapture() {
                // Place a white piece for the black knight to capture
                board.getSquares()[1][2] = new Piece("pawn", "white", "♙");
                
                // Black knight should be able to capture the white pawn
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 1, 2, currentPlayer));
            }
        }
    }
    
    @Nested
    @DisplayName("Queen movement validation")
    class QueenTests {
        
        @BeforeEach
        void setUpQueenTests() {
            // Clear the board for testing
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    board.getSquares()[row][col] = null;
                }
            }
        }
        
        @Nested
        @DisplayName("Basic Queen movement tests")
        class BasicQueenMovementTests {
            
            @BeforeEach
            void setUpQueenOnBoard() {
                // Place a white queen in the middle of the board for flexible testing
                board.getSquares()[4][4] = new Piece("queen", "white", "♕");
            }
            
            @Test
            @DisplayName("Can move horizontally to the right")
            void testMoveHorizontallyRight() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move horizontally to the left")
            void testMoveHorizontallyLeft() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 0, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move vertically upward")
            void testMoveVerticallyUp() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 0, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move vertically downward")
            void testMoveVerticallyDown() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move diagonally top-right")
            void testMoveDiagonallyTopRight() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 1, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move diagonally top-left")
            void testMoveDiagonallyTopLeft() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 1, 1, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move diagonally bottom-right")
            void testMoveDiagonallyBottomRight() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move diagonally bottom-left")
            void testMoveDiagonallyBottomLeft() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 1, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move to adjacent squares in all directions")
            void testMoveToAdjacentSquares() {
                // Horizontal and vertical (rook-like) moves
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 5, currentPlayer)); // Right
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 3, currentPlayer)); // Left
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 4, currentPlayer)); // Up
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 4, currentPlayer)); // Down
                
                // Diagonal (bishop-like) moves
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer)); // Top-left
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 5, currentPlayer)); // Top-right
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 3, currentPlayer)); // Bottom-left
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 5, currentPlayer)); // Bottom-right
            }
        }
        
        @Nested
        @DisplayName("Queen capture tests")
        class QueenCaptureTests {
            
            @BeforeEach
            void setUpQueenAndOpponentPieces() {
                // Place a white queen in the middle of the board
                board.getSquares()[4][4] = new Piece("queen", "white", "♕");
                
                // Place some opponent pieces for capture tests
                board.getSquares()[4][7] = new Piece("pawn", "black", "♟"); // Horizontal
                board.getSquares()[7][4] = new Piece("knight", "black", "♞"); // Vertical
                board.getSquares()[2][2] = new Piece("pawn", "black", "♟"); // Diagonal top-left
                board.getSquares()[7][7] = new Piece("bishop", "black", "♝"); // Diagonal bottom-right
            }
            
            @Test
            @DisplayName("Can capture horizontally")
            void testCaptureHorizontally() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Can capture vertically")
            void testCaptureVertically() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can capture diagonally")
            void testCaptureDiagonally() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 2, currentPlayer)); // Top-left
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer)); // Bottom-right
            }
            
            @Test
            @DisplayName("Cannot capture pieces of the same color")
            void testCannotCaptureSameColorPieces() {
                // Place friendly pieces in various paths
                board.getSquares()[4][6] = new Piece("pawn", "white", "♙"); // Horizontal
                board.getSquares()[6][4] = new Piece("knight", "white", "♘"); // Vertical
                board.getSquares()[3][3] = new Piece("pawn", "white", "♙"); // Diagonal
                
                // Try to "capture" the white pieces
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 6, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 4, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Invalid Queen movement tests")
        class InvalidQueenMovementTests {
            
            @BeforeEach
            void setUpQueenWithObstacles() {
                // Place a white queen in the middle of the board
                board.getSquares()[4][4] = new Piece("queen", "white", "♕");
                
                // Place obstacles in the queen's paths
                board.getSquares()[4][6] = new Piece("pawn", "white", "♙"); // Horizontal, same color
                board.getSquares()[2][4] = new Piece("pawn", "black", "♟"); // Vertical, opponent
                board.getSquares()[3][3] = new Piece("pawn", "white", "♙"); // Diagonal, same color
                board.getSquares()[6][6] = new Piece("pawn", "black", "♟"); // Diagonal, opponent
            }
            
            @Test
            @DisplayName("Cannot move in non-straight or non-diagonal paths")
            void testCannotMoveInNonStraightOrNonDiagonalPaths() {
                // Try to move like a knight
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 5, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 3, currentPlayer));
                
                // Try other invalid moves
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 7, currentPlayer)); // Not a straight or diagonal path
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer)); // Not a straight or diagonal path
            }
            
            @Test
            @DisplayName("Cannot jump over pieces horizontally or vertically")
            void testCannotJumpOverPiecesHorizontallyOrVertically() {
                // Try to jump over the white pawn horizontally
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
                
                // Try to jump over the black pawn vertically
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 0, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot jump over pieces diagonally")
            void testCannotJumpOverPiecesDiagonally() {
                // Try to jump over the white pawn diagonally
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 1, 1, currentPlayer));
                
                // Try to jump over the black pawn diagonally
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Can stop at opponent piece for capture but not move past it")
            void testCannotMovePastOpponentPiece() {
                // Can capture the black pawn at (2,4)
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 4, currentPlayer));
                
                // Cannot move past it
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 1, 4, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 0, 4, currentPlayer));
                
                // Can capture the black pawn at (6,6)
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
                
                // Cannot move past it
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Black Queen movement tests")
        class BlackQueenMovementTests {
            
            @BeforeEach
            void setUpBlackQueen() {
                // Place a black queen in the middle of the board
                board.getSquares()[3][3] = new Piece("queen", "black", "♛");
                currentPlayer = "black"; // Switch to black's turn
            }
            
            @Test
            @DisplayName("Black queen can move in all directions")
            void testBlackQueenMovement() {
                // Horizontal and vertical movements
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 7, currentPlayer)); // Horizontal
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 7, 3, currentPlayer)); // Vertical
                
                // Diagonal movements
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 0, 0, currentPlayer)); // Top-left
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 0, 6, currentPlayer)); // Top-right
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 7, 7, currentPlayer)); // Bottom-right
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 6, 0, currentPlayer)); // Bottom-left
            }
            
            @Test
            @DisplayName("Black queen can capture white pieces")
            void testBlackQueenCapture() {
                // Place white pieces for the black queen to capture
                board.getSquares()[3][7] = new Piece("pawn", "white", "♙"); // Horizontal
                board.getSquares()[0][0] = new Piece("rook", "white", "♖"); // Diagonal
                
                // Black queen should be able to capture the white pieces
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 7, currentPlayer)); // Horizontal capture
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 0, 0, currentPlayer)); // Diagonal capture
            }
        }
    }
    
    @Nested
    @DisplayName("King movement validation")
    class KingTests {
        
        @BeforeEach
        void setUpKingTests() {
            // Clear the board for testing
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    board.getSquares()[row][col] = null;
                }
            }
        }
        
        @Nested
        @DisplayName("Basic King movement tests")
        class BasicKingMovementTests {
            
            @BeforeEach
            void setUpKingOnBoard() {
                // Place a white king in the middle of the board for flexible testing
                board.getSquares()[4][4] = new Piece("king", "white", "♔");
            }
            
            @Test
            @DisplayName("Can move one square horizontally to the right")
            void testMoveHorizontallyRight() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 5, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move one square horizontally to the left")
            void testMoveHorizontallyLeft() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 3, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move one square vertically upward")
            void testMoveVerticallyUp() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move one square vertically downward")
            void testMoveVerticallyDown() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move one square diagonally in all directions")
            void testMoveDiagonally() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer)); // Top-left
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 5, currentPlayer)); // Top-right
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 3, currentPlayer)); // Bottom-left
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 5, currentPlayer)); // Bottom-right
            }
            
            @Test
            @DisplayName("Can move in all eight directions")
            void testMoveInAllDirections() {
                // Test movement in all 8 surrounding squares
                for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                    for (int colOffset = -1; colOffset <= 1; colOffset++) {
                        // Skip the current position (0,0 offset)
                        if (rowOffset == 0 && colOffset == 0) continue;
                        
                        assertTrue(ChessUtils.isValidMove(
                            board, 4, 4, 4 + rowOffset, 4 + colOffset, currentPlayer
                        ));
                    }
                }
            }
        }
        
        @Nested
        @DisplayName("King capture tests")
        class KingCaptureTests {
            
            @BeforeEach
            void setUpKingAndOpponentPieces() {
                // Place a white king in the middle of the board
                board.getSquares()[4][4] = new Piece("king", "white", "♔");
                
                // Place opponent pieces around the king
                board.getSquares()[3][3] = new Piece("pawn", "black", "♟"); // Top-left
                board.getSquares()[3][4] = new Piece("pawn", "black", "♟"); // Top
                board.getSquares()[3][5] = new Piece("pawn", "black", "♟"); // Top-right
                board.getSquares()[4][5] = new Piece("pawn", "black", "♟"); // Right
            }
            
            @Test
            @DisplayName("Can capture in all directions")
            void testCaptureInAllDirections() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer)); // Top-left
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 4, currentPlayer)); // Top
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 5, currentPlayer)); // Top-right
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 5, currentPlayer)); // Right
            }
            
            @Test
            @DisplayName("Cannot capture pieces of the same color")
            void testCannotCaptureSameColorPieces() {
                // Replace opponent pieces with friendly pieces
                board.getSquares()[3][3] = new Piece("pawn", "white", "♙"); // Top-left
                board.getSquares()[3][4] = new Piece("pawn", "white", "♙"); // Top
                
                // Try to "capture" the white pawns
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 3, 4, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Invalid King movement tests")
        class InvalidKingMovementTests {
            
            @BeforeEach
            void setUpKingOnBoard() {
                // Place a white king in the middle of the board
                board.getSquares()[4][4] = new Piece("king", "white", "♔");
            }
            
            @Test
            @DisplayName("Cannot move more than one square horizontally")
            void testCannotMoveTwoSquaresHorizontally() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 6, currentPlayer)); // Right 2
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 2, currentPlayer)); // Left 2
            }
            
            @Test
            @DisplayName("Cannot move more than one square vertically")
            void testCannotMoveTwoSquaresVertically() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 4, currentPlayer)); // Up 2
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 4, currentPlayer)); // Down 2
            }
            
            @Test
            @DisplayName("Cannot move more than one square diagonally")
            void testCannotMoveTwoSquaresDiagonally() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 2, currentPlayer)); // Top-left 2
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 6, currentPlayer)); // Top-right 2
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 2, currentPlayer)); // Bottom-left 2
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer)); // Bottom-right 2
            }
            
            @Test
            @DisplayName("Cannot move like other pieces")
            void testCannotMoveInOtherPatterns() {
                // Try to move like a knight
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 3, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 5, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 3, currentPlayer));
                
                // Try to move to non-adjacent squares
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 4, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 1, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Black King movement tests")
        class BlackKingMovementTests {
            
            @BeforeEach
            void setUpBlackKing() {
                // Place a black king in the middle of the board
                board.getSquares()[3][3] = new Piece("king", "black", "♚");
                currentPlayer = "black"; // Switch to black's turn
            }
            
            @Test
            @DisplayName("Black king can move one square in all directions")
            void testBlackKingMovement() {
                // Test horizontal and vertical moves
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 4, currentPlayer)); // Right
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 2, currentPlayer)); // Left
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 2, 3, currentPlayer)); // Up
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 4, 3, currentPlayer)); // Down
                
                // Test diagonal moves
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 2, 2, currentPlayer)); // Top-left
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 2, 4, currentPlayer)); // Top-right
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 4, 2, currentPlayer)); // Bottom-left
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 4, 4, currentPlayer)); // Bottom-right
            }
            
            @Test
            @DisplayName("Black king can capture white pieces")
            void testBlackKingCapture() {
                // Place white pieces around the black king
                board.getSquares()[2][2] = new Piece("pawn", "white", "♙"); // Top-left
                board.getSquares()[2][4] = new Piece("pawn", "white", "♙"); // Top-right
                
                // Black king should be able to capture the white pieces
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 2, 2, currentPlayer)); // Capture top-left
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 2, 4, currentPlayer)); // Capture top-right
            }
        }
        
        @Nested
        @DisplayName("Castling tests")
        class CastlingTests {
            
            @BeforeEach
            void setUpCastlingBoard() {
                // Clear the board for testing
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        board.getSquares()[row][col] = null;
                    }
                }
                
                // Set up king and rooks in initial positions
                board.getSquares()[7][4] = new Piece("king", "white", "♔");
                board.getSquares()[7][0] = new Piece("rook", "white", "♖"); // Queenside rook
                board.getSquares()[7][7] = new Piece("rook", "white", "♖"); // Kingside rook
                
                board.getSquares()[0][4] = new Piece("king", "black", "♚");
                board.getSquares()[0][0] = new Piece("rook", "black", "♜"); // Queenside rook
                board.getSquares()[0][7] = new Piece("rook", "black", "♜"); // Kingside rook
            }
            
            @Test
            @DisplayName("White kingside castling should be valid when path is clear")
            void testWhiteKingsideCastling() {
                assertTrue(ChessUtils.isValidMove(board, 7, 4, 7, 6, "white"));
                
                // Test the actual castling
                ChessUtils.castleRookWithKing(board, 7, 4, 7, 7);
                
                // Verify king and rook have moved correctly
                assertNull(board.getSquares()[7][4]); // King's original position is empty
                assertNull(board.getSquares()[7][7]); // Rook's original position is empty
                
                Piece king = board.getSquares()[7][6];
                Piece rook = board.getSquares()[7][5];
                
                assertNotNull(king);
                assertNotNull(rook);
                assertEquals("king", king.getType());
                assertEquals("rook", rook.getType());
            }
            
            @Test
            @DisplayName("White queenside castling should be valid when path is clear")
            void testWhiteQueensideCastling() {
                assertTrue(ChessUtils.isValidMove(board, 7, 4, 7, 2, "white"));
                
                // Test the actual castling
                ChessUtils.castleRookWithKing(board, 7, 4, 7, 0);
                
                // Verify king and rook have moved correctly
                assertNull(board.getSquares()[7][4]); // King's original position is empty
                assertNull(board.getSquares()[7][0]); // Rook's original position is empty
                
                Piece king = board.getSquares()[7][2];
                Piece rook = board.getSquares()[7][3];
                
                assertNotNull(king);
                assertNotNull(rook);
                assertEquals("king", king.getType());
                assertEquals("rook", rook.getType());
            }
            
            @Test
            @DisplayName("Black kingside castling should be valid when path is clear")
            void testBlackKingsideCastling() {
                currentPlayer = "black";
                assertTrue(ChessUtils.isValidMove(board, 0, 4, 0, 6, currentPlayer));
                
                // Test the actual castling
                ChessUtils.castleRookWithKing(board, 0, 4, 0, 7);
                
                // Verify king and rook have moved correctly
                assertNull(board.getSquares()[0][4]); // King's original position is empty
                assertNull(board.getSquares()[0][7]); // Rook's original position is empty
                
                Piece king = board.getSquares()[0][6];
                Piece rook = board.getSquares()[0][5];
                
                assertNotNull(king);
                assertNotNull(rook);
                assertEquals("king", king.getType());
                assertEquals("rook", rook.getType());
            }
            
            @Test
            @DisplayName("Castling should be invalid when pieces are in the way")
            void testCastlingWithPiecesInTheWay() {
                // Place a piece between the king and kingside rook
                board.getSquares()[7][5] = new Piece("bishop", "white", "♗");
                
                // Kingside castling should now be invalid
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 6, "white"));
                
                // Place a piece between the king and queenside rook
                board.getSquares()[7][1] = new Piece("knight", "white", "♘");
                
                // Queenside castling should now be invalid
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 2, "white"));
            }
            
            @Test
            @DisplayName("Castling should be invalid when the king would move through check")
            void testCastlingThroughCheck() {
                // Place a black rook that attacks the square the king would move through during kingside castling
                board.getSquares()[5][5] = new Piece("rook", "black", "♜");
                
                // Kingside castling should now be invalid because king would move through check
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 6, "white"));
            }
            
            @Test
            @DisplayName("Castling should be invalid when the king is in check")
            void testCastlingInCheck() {
                // Place a black rook that puts the white king in check
                board.getSquares()[4][4] = new Piece("rook", "black", "♜");
                
                // Both kingside and queenside castling should be invalid when king is in check
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 6, "white"));
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 2, "white"));
            }
            
            @Test
            @DisplayName("Castling should be invalid if king or rook are not in initial positions")
            void testCastlingWithMovedPieces() {
                // Move king and put it back to simulate the king having moved
                Piece king = board.getSquares()[7][4];
                board.getSquares()[7][4] = null;
                board.getSquares()[7][3] = king;
                king.setHasMoved(true);
                
                board.getSquares()[7][3] = null;
                board.getSquares()[7][4] = king;
                
                // Kingside castling should now be invalid
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 6, "white"));
                
                // Move rook and put it back to simulate the rook having moved
                Piece rook = board.getSquares()[7][0];
                board.getSquares()[7][0] = null;
                board.getSquares()[7][1] = rook;
                rook.setHasMoved(true);
                board.getSquares()[7][1] = null;
                board.getSquares()[7][0] = rook;
                
                // Queenside castling should now be invalid
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 2, "white"));
            }
        }
    }
    
    // Future test classes for other piece types can be added here
}
