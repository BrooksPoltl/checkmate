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
        currentPlayer = "white";
        
        // Clear the board by setting all squares to null
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board.getSquares()[row][col] = null;
            }
        }
    }
    
    @Nested
    @DisplayName("Pawn movement validation")
    class PawnTests {
        
        @Nested
        @DisplayName("White pawn movement tests")
        class WhitePawnMovementTests {
            
            @BeforeEach
            void setUpWhitePawns() {
                // Set up white pawns in their starting positions (6th row)
                for (int col = 0; col < 8; col++) {
                    board.getSquares()[6][col] = new Piece("pawn", "white", "♙");
                }
            }
            
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
                ChessUtils.applyMove(board, 6, 4, 5, 4);
                assertFalse(ChessUtils.isValidMove(board, 5, 4, 3, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move forward if blocked")
            void testCannotMoveIfBlocked() {
                board.getSquares()[5][4] = new Piece("pawn", "black", "♟");
                assertFalse(ChessUtils.isValidMove(board, 6, 4, 4, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Can capture diagonally")
            void testCanCaptureDiagonally() {
                board.getSquares()[5][5] = new Piece("pawn", "black", "♟");
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
            void setUpBlackPawns() {
                // Set up black pawns in their starting positions (1st row)
                for (int col = 0; col < 8; col++) {
                    board.getSquares()[1][col] = new Piece("pawn", "black", "♟");
                }
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
                ChessUtils.applyMove(board, 6, 3, 4, 3);
                board.getSquares()[3][4] = new Piece("pawn", "black", "♟");
                assertTrue(ChessUtils.isValidMove(board, 3, 4, 4, 3, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Invalid pawn movement tests")
        class InvalidPawnMovementTests {
            
            @Test
            @DisplayName("Cannot move backward")
            void testCannotMoveBackward() {
                ChessUtils.applyMove(board, 6, 4, 4, 4);
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
                board.getSquares()[5][3] = new Piece("pawn", "black", "♟");
                assertFalse(ChessUtils.isValidMove(board, 6, 3, 4, 3, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot capture pieces of the same color")
            void testCannotCaptureSameColorPieces() {
                board.getSquares()[5][5] = new Piece("knight", "white", "♘");
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
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 4, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 4, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Rook capture tests")
        class RookCaptureTests {
            
            @BeforeEach
            void setUpRookAndOpponentPieces() {
                board.getSquares()[4][4] = new Piece("rook", "white", "♖");
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
                board.getSquares()[4][6] = new Piece("pawn", "white", "♙");
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 6, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Invalid Rook movement tests")
        class InvalidRookMovementTests {
            
            @BeforeEach
            void setUpRookWithObstacles() {
                board.getSquares()[4][4] = new Piece("rook", "white", "♖");
                board.getSquares()[4][6] = new Piece("pawn", "white", "♙");
                board.getSquares()[2][4] = new Piece("pawn", "black", "♟");
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
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 0, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move like other pieces")
            void testCannotMoveInLShape() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 5, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 3, currentPlayer));
            }
            
            @Test
            @DisplayName("Can stop at opponent piece for capture but not move past it")
            void testCannotMovePastOpponentPiece() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 4, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 1, 4, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 0, 4, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Black Rook movement tests")
        class BlackRookMovementTests {
            
            @BeforeEach
            void setUpBlackRook() {
                board.getSquares()[3][3] = new Piece("rook", "black", "♜");
                currentPlayer = "black";
            }
            
            @Test
            @DisplayName("Black rook can move horizontally and vertically")
            void testBlackRookMovement() {
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 7, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 7, 3, currentPlayer));
            }
            
            @Test
            @DisplayName("Black rook can capture white pieces")
            void testBlackRookCapture() {
                board.getSquares()[3][7] = new Piece("pawn", "white", "♙");
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 7, currentPlayer));
            }
        }
    }
    
    @Nested
    @DisplayName("Bishop movement validation")
    class BishopTests {
        
        @BeforeEach
        void setUpBishopTests() {
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
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 5, currentPlayer));
            }
            
            @Test
            @DisplayName("Maintains color-bound movement")
            void testMaintainsColorBound() {
                board.getSquares()[4][4] = new Piece("bishop", "white", "♗");
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 2, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Bishop capture tests")
        class BishopCaptureTests {
            
            @BeforeEach
            void setUpBishopAndOpponentPieces() {
                board.getSquares()[4][4] = new Piece("bishop", "white", "♗");
                board.getSquares()[2][2] = new Piece("pawn", "black", "♟");
                board.getSquares()[7][7] = new Piece("knight", "black", "♞");
            }
            
            @Test
            @DisplayName("Can capture diagonally")
            void testCaptureDiagonally() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 2, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot capture pieces of the same color")
            void testCannotCaptureSameColorPieces() {
                board.getSquares()[3][3] = new Piece("pawn", "white", "♙");
                board.getSquares()[6][6] = new Piece("knight", "white", "♘");
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Invalid Bishop movement tests")
        class InvalidBishopMovementTests {
            
            @BeforeEach
            void setUpBishopWithObstacles() {
                board.getSquares()[4][4] = new Piece("bishop", "white", "♗");
                board.getSquares()[3][3] = new Piece("pawn", "white", "♙");
                board.getSquares()[6][6] = new Piece("pawn", "black", "♟");
            }
            
            @Test
            @DisplayName("Cannot move horizontally or vertically")
            void testCannotMoveHorizontallyOrVertically() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move in non-diagonal paths")
            void testCannotMoveNonDiagonally() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 7, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot jump over pieces")
            void testCannotJumpOverPieces() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 1, 1, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move like other pieces")
            void testCannotMoveInLShape() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 5, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 3, currentPlayer));
            }
            
            @Test
            @DisplayName("Can stop at opponent piece for capture but not move past it")
            void testCannotMovePastOpponentPiece() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Black Bishop movement tests")
        class BlackBishopMovementTests {
            
            @BeforeEach
            void setUpBlackBishop() {
                board.getSquares()[3][3] = new Piece("bishop", "black", "♝");
                currentPlayer = "black";
            }
            
            @Test
            @DisplayName("Black bishop can move diagonally")
            void testBlackBishopMovement() {
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 5, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 1, 1, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 1, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 5, 1, currentPlayer));
            }
            
            @Test
            @DisplayName("Black bishop can capture white pieces")
            void testBlackBishopCapture() {
                board.getSquares()[5][5] = new Piece("pawn", "white", "♙");
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 5, 5, currentPlayer));
            }
        }
    }
    
    @Nested
    @DisplayName("Knight movement validation")
    class KnightTests {
        
        @BeforeEach
        void setUpKnightTests() {
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
                board.getSquares()[4][4] = new Piece("knight", "white", "♘");
            }
            
            @Test
            @DisplayName("Can move in all eight L-shaped directions")
            void testMoveInLShape() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 6, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 6, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 6, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 6, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 2, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 2, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move from a corner")
            void testMoveFromCorner() {
                board.getSquares()[4][4] = null;
                board.getSquares()[0][0] = new Piece("knight", "white", "♘");
                
                assertTrue(ChessUtils.isValidMove(board, 0, 0, 2, 1, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 0, 0, 1, 2, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move from the edge of the board")
            void testMoveFromEdge() {
                board.getSquares()[4][4] = null;
                board.getSquares()[0][3] = new Piece("knight", "white", "♘");
                
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
                board.getSquares()[4][4] = new Piece("knight", "white", "♘");
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
                board.getSquares()[2][5] = new Piece("pawn", "white", "♙");
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer));
            }
            
            @Test
            @DisplayName("Can capture and pass through blocking pieces")
            void testCanCaptureAndJumpOverPieces() {
                board.getSquares()[3][4] = new Piece("pawn", "black", "♟");
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Invalid Knight movement tests")
        class InvalidKnightMovementTests {
            
            @BeforeEach
            void setUpKnight() {
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
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 5, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Black Knight movement tests")
        class BlackKnightMovementTests {
            
            @BeforeEach
            void setUpBlackKnight() {
                board.getSquares()[3][3] = new Piece("knight", "black", "♞");
                currentPlayer = "black";
            }
            
            @Test
            @DisplayName("Black knight can make L-shaped moves")
            void testBlackKnightMovement() {
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 1, 2, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 1, 4, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 5, 2, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 5, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Black knight can capture white pieces")
            void testBlackKnightCapture() {
                board.getSquares()[1][2] = new Piece("pawn", "white", "♙");
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 1, 2, currentPlayer));
            }
        }
    }
    
    @Nested
    @DisplayName("Queen movement validation")
    class QueenTests {
        
        @BeforeEach
        void setUpQueenTests() {
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
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 4, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 4, currentPlayer));
                
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 5, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Queen capture tests")
        class QueenCaptureTests {
            
            @BeforeEach
            void setUpQueenAndOpponentPieces() {
                board.getSquares()[4][4] = new Piece("queen", "white", "♕");
                board.getSquares()[4][7] = new Piece("pawn", "black", "♟");
                board.getSquares()[7][4] = new Piece("knight", "black", "♞");
                board.getSquares()[2][2] = new Piece("pawn", "black", "♟");
                board.getSquares()[7][7] = new Piece("bishop", "black", "♝");
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
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 2, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot capture pieces of the same color")
            void testCannotCaptureSameColorPieces() {
                board.getSquares()[4][6] = new Piece("pawn", "white", "♙");
                board.getSquares()[6][4] = new Piece("knight", "white", "♘");
                board.getSquares()[3][3] = new Piece("pawn", "white", "♙");
                
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
                board.getSquares()[4][4] = new Piece("queen", "white", "♕");
                board.getSquares()[4][6] = new Piece("pawn", "white", "♙");
                board.getSquares()[2][4] = new Piece("pawn", "black", "♟");
                board.getSquares()[3][3] = new Piece("pawn", "white", "♙");
                board.getSquares()[6][6] = new Piece("pawn", "black", "♟");
            }
            
            @Test
            @DisplayName("Cannot move in non-straight or non-diagonal paths")
            void testCannotMoveInNonStraightOrNonDiagonalPaths() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 5, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 3, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 7, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot jump over pieces horizontally or vertically")
            void testCannotJumpOverPiecesHorizontallyOrVertically() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 7, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 0, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot jump over pieces diagonally")
            void testCannotJumpOverPiecesDiagonally() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 1, 1, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
            
            @Test
            @DisplayName("Can stop at opponent piece for capture but not move past it")
            void testCannotMovePastOpponentPiece() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 2, 4, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 1, 4, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 0, 4, currentPlayer));
                
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 7, 7, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Black Queen movement tests")
        class BlackQueenMovementTests {
            
            @BeforeEach
            void setUpBlackQueen() {
                board.getSquares()[3][3] = new Piece("queen", "black", "♛");
                currentPlayer = "black";
            }
            
            @Test
            @DisplayName("Black queen can move in all directions")
            void testBlackQueenMovement() {
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 7, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 7, 3, currentPlayer));
                
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 0, 0, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 0, 6, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 7, 7, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 6, 0, currentPlayer));
            }
            
            @Test
            @DisplayName("Black queen can capture white pieces")
            void testBlackQueenCapture() {
                board.getSquares()[3][7] = new Piece("pawn", "white", "♙");
                board.getSquares()[0][0] = new Piece("rook", "white", "♖");
                
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 7, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 0, 0, currentPlayer));
            }
        }
    }
    
    @Nested
    @DisplayName("King movement validation")
    class KingTests {
        
        @BeforeEach
        void setUpKingTests() {
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
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 5, currentPlayer));
            }
            
            @Test
            @DisplayName("Can move in all eight directions")
            void testMoveInAllDirections() {
                for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                    for (int colOffset = -1; colOffset <= 1; colOffset++) {
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
                board.getSquares()[4][4] = new Piece("king", "white", "♔");
                // Add opponent pieces around the king for capture testing
                board.getSquares()[3][3] = new Piece("pawn", "black", "♟");
                board.getSquares()[3][4] = new Piece("pawn", "black", "♟");
                board.getSquares()[3][5] = new Piece("pawn", "black", "♟");
                board.getSquares()[4][5] = new Piece("pawn", "black", "♟");
            }
            
            @Test
            @DisplayName("Can capture in all directions")
            void testCaptureInAllDirections() {
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 4, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 5, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 4, 4, 4, 5, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot capture pieces of the same color")
            void testCannotCaptureSameColorPieces() {
                board.getSquares()[3][3] = new Piece("pawn", "white", "♙");
                board.getSquares()[3][4] = new Piece("pawn", "white", "♙");
                
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 3, 3, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 3, 4, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Invalid King movement tests")
        class InvalidKingMovementTests {
            
            @BeforeEach
            void setUpKingOnBoard() {
                board.getSquares()[4][4] = new Piece("king", "white", "♔");
            }
            
            @Test
            @DisplayName("Cannot move more than one square horizontally")
            void testCannotMoveTwoSquaresHorizontally() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 6, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 4, 2, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move more than one square vertically")
            void testCannotMoveTwoSquaresVertically() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 4, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move more than one square diagonally")
            void testCannotMoveTwoSquaresDiagonally() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 2, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 6, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 2, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 6, currentPlayer));
            }
            
            @Test
            @DisplayName("Cannot move like other pieces")
            void testCannotMoveInOtherPatterns() {
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 5, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 2, 3, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 5, currentPlayer));
                assertFalse(ChessUtils.isValidMove(board, 4, 4, 6, 3, currentPlayer));
                
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
                board.getSquares()[3][3] = new Piece("king", "black", "♚");
                currentPlayer = "black";
            }
            
            @Test
            @DisplayName("Black king can move one square in all directions")
            void testBlackKingMovement() {
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 4, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 3, 2, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 2, 3, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 4, 3, currentPlayer));
                
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 2, 2, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 2, 4, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 4, 2, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 4, 4, currentPlayer));
            }
            
            @Test
            @DisplayName("Black king can capture white pieces")
            void testBlackKingCapture() {
                board.getSquares()[2][2] = new Piece("pawn", "white", "♙");
                board.getSquares()[2][4] = new Piece("pawn", "white", "♙");
                
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 2, 2, currentPlayer));
                assertTrue(ChessUtils.isValidMove(board, 3, 3, 2, 4, currentPlayer));
            }
        }
        
        @Nested
        @DisplayName("Castling tests")
        class CastlingTests {
            
            @BeforeEach
            void setUpCastlingBoard() {
                // Set up kings and rooks in initial positions for castling
                board.getSquares()[7][4] = new Piece("king", "white", "♔");
                board.getSquares()[7][0] = new Piece("rook", "white", "♖");
                board.getSquares()[7][7] = new Piece("rook", "white", "♖");
                
                board.getSquares()[0][4] = new Piece("king", "black", "♚");
                board.getSquares()[0][0] = new Piece("rook", "black", "♜");
                board.getSquares()[0][7] = new Piece("rook", "black", "♜");
            }
            
            @Test
            @DisplayName("White kingside castling should be valid when path is clear")
            void testWhiteKingsideCastling() {
                assertTrue(ChessUtils.isValidMove(board, 7, 4, 7, 6, "white"));
                
                ChessUtils.castleRookWithKing(board, 7, 4, 7, 7);
                
                assertNull(board.getSquares()[7][4]);
                assertNull(board.getSquares()[7][7]);
                
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
                
                ChessUtils.castleRookWithKing(board, 7, 4, 7, 0);
                
                assertNull(board.getSquares()[7][4]);
                assertNull(board.getSquares()[7][0]);
                
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
                
                ChessUtils.castleRookWithKing(board, 0, 4, 0, 7);
                
                assertNull(board.getSquares()[0][4]);
                assertNull(board.getSquares()[0][7]);
                
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
                board.getSquares()[7][5] = new Piece("bishop", "white", "♗");
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 6, "white"));
                
                board.getSquares()[7][1] = new Piece("knight", "white", "♘");
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 2, "white"));
            }
            
            @Test
            @DisplayName("Castling should be invalid when the king would move through check")
            void testCastlingThroughCheck() {
                board.getSquares()[5][5] = new Piece("rook", "black", "♜");
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 6, "white"));
            }
            
            @Test
            @DisplayName("Castling should be invalid when the king is in check")
            void testCastlingInCheck() {
                board.getSquares()[4][4] = new Piece("rook", "black", "♜");
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 6, "white"));
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 2, "white"));
            }
            
            @Test
            @DisplayName("Castling should be invalid if king or rook are not in initial positions")
            void testCastlingWithMovedPieces() {
                Piece king = board.getSquares()[7][4];
                board.getSquares()[7][4] = null;
                board.getSquares()[7][3] = king;
                
                king.setHasMoved(true);
                
                board.getSquares()[7][3] = null;
                board.getSquares()[7][4] = king;
                
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 6, "white"));
                
                Piece rook = board.getSquares()[7][0];
                board.getSquares()[7][0] = null;
                board.getSquares()[7][1] = rook;
                
                rook.setHasMoved(true);
                
                board.getSquares()[7][1] = null;
                board.getSquares()[7][0] = rook;
                
                assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 2, "white"));
            }
        }
    }

    @Nested
    @DisplayName("Check prevention tests")
    class CheckPreventionTests {
        
        @BeforeEach
        void setUpCheckPreventionTests() {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    board.getSquares()[row][col] = null;
                }
            }
        }
        
        @Test
        @DisplayName("Cannot move a piece that would expose king to check")
        void testCannotMoveToExposeKingToCheck() {
            // Setup a position where moving a piece would expose the king to check
            board.getSquares()[7][4] = new Piece("king", "white", "♔");  // White king at e1
            board.getSquares()[7][3] = new Piece("queen", "white", "♕"); // White queen at d1
            board.getSquares()[0][3] = new Piece("rook", "black", "♜");  // Black rook at d8
            
            // Moving the queen would expose the king to the rook's attack
            assertFalse(ChessUtils.isValidMove(board, 7, 3, 6, 3, "white"));
        }
        
        @Test
        @DisplayName("Cannot move a pinned piece in a way that breaks the pin")
        void testCannotMovePinnedPieceInWrongDirection() {
            // Setup a position with a pinned bishop
            board.getSquares()[4][4] = new Piece("king", "white", "♔");  // White king at e4
            board.getSquares()[3][3] = new Piece("bishop", "white", "♗"); // White bishop at d5
            board.getSquares()[1][1] = new Piece("queen", "black", "♛");  // Black queen at b7
            
            // The bishop is pinned and can only move along the diagonal to block or capture
            assertFalse(ChessUtils.isValidMove(board, 3, 3, 2, 4, "white")); // Cannot move in other direction
            
            // However, it can move along the pin line
            assertTrue(ChessUtils.isValidMove(board, 3, 3, 2, 2, "white")); // Can move along the diagonal
        }
        
        @Test
        @DisplayName("King cannot move into check")
        void testKingCannotMoveIntoCheck() {
            // Setup a position where the king would move into check
            board.getSquares()[7][4] = new Piece("king", "white", "♔");  // White king at e1
            board.getSquares()[5][3] = new Piece("queen", "black", "♛");  // Black queen at d3
            
            // King cannot move to a square that's under attack
            assertFalse(ChessUtils.isValidMove(board, 7, 4, 6, 4, "white")); // d2 is under attack
        }
        
        @Test
        @DisplayName("Piece must block check if king is in check")
        void testMustBlockCheckIfKingInCheck() {
            // Setup a position where the king is in check
            board.getSquares()[7][4] = new Piece("king", "white", "♔");  // White king at e1
            board.getSquares()[5][4] = new Piece("queen", "black", "♛");  // Black queen at e3 (giving check)
            board.getSquares()[6][2] = new Piece("knight", "white", "♘"); // White knight at c2
            board.getSquares()[6][6] = new Piece("rook", "white", "♖");   // White rook at g2
            
            // Knight cannot make a move that doesn't address the check
            assertFalse(ChessUtils.isValidMove(board, 6, 2, 4, 3, "white"));
            
            // Rook can move to block the check
            assertTrue(ChessUtils.isValidMove(board, 6, 6, 6, 4, "white"));
        }
        
        @Test
        @DisplayName("Piece must capture checking piece if king is in check")
        void testMustCaptureCheckingPieceIfKingInCheck() {
            // Setup a position where the king is in check
            board.getSquares()[7][4] = new Piece("king", "white", "♔");  // White king at e1
            board.getSquares()[5][4] = new Piece("queen", "black", "♛");  // Black queen at e3 (giving check)
            board.getSquares()[4][4] = new Piece("rook", "white", "♖");   // White rook at e4
            
            // Rook can capture the checking queen
            assertTrue(ChessUtils.isValidMove(board, 4, 4, 5, 4, "white"));
        }
        
        @Test
        @DisplayName("King must move out of check if no other option")
        void testKingMustMoveOutOfCheck() {
            // Setup a position where the king is in check and must move
            board.getSquares()[7][4] = new Piece("king", "white", "♔");  // White king at e1
            board.getSquares()[0][4] = new Piece("queen", "black", "♛");  // Black queen at e8 (giving check)
            
            // King must move out of check
            assertTrue(ChessUtils.isValidMove(board, 7, 4, 7, 3, "white"));  // Can move to d1
            assertTrue(ChessUtils.isValidMove(board, 7, 4, 7, 5, "white"));  // Can move to f1
        }
        
        @Test
        @DisplayName("Cannot make any move if in checkmate")
        void testCannotMoveIfInCheckmate() {
            // Correcting the fool's mate position
            board.getSquares()[7][4] = new Piece("king", "white", "♔");   // White king at e1
            board.getSquares()[6][5] = new Piece("pawn", "white", "♙");   // White pawn at f2
            board.getSquares()[6][6] = new Piece("pawn", "white", "♙");   // White pawn at g2
            board.getSquares()[5][7] = new Piece("queen", "black", "♛");  // Black queen at h3 (giving checkmate)
            
            // King has no legal moves
            assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 3, "white"));  // Cannot move to d1
            assertFalse(ChessUtils.isValidMove(board, 7, 4, 6, 3, "white"));  // Cannot move to d2
            assertFalse(ChessUtils.isValidMove(board, 7, 4, 6, 4, "white"));  // Cannot move to e2
            
            // Cannot move pawns either as they don't block the check
            assertFalse(ChessUtils.isValidMove(board, 6, 5, 5, 5, "white"));  // Cannot move f-pawn
            assertFalse(ChessUtils.isValidMove(board, 6, 6, 5, 6, "white"));  // Cannot move g-pawn
        }
        
        @Test
        @DisplayName("Moving a piece away from king doesn't affect check status")
        void testMovingAwayFromKingDoesNotAffectCheck() {
            // Setup a position with pieces away from king
            board.getSquares()[7][4] = new Piece("king", "white", "♔");  // White king at e1
            board.getSquares()[7][0] = new Piece("rook", "white", "♖");  // White rook at a1
            board.getSquares()[0][0] = new Piece("rook", "black", "♜");  // Black rook at a8
            
            // Moving the rook (which is not involved in any pin or check) should be valid
            assertTrue(ChessUtils.isValidMove(board, 7, 0, 6, 0, "white"));  // Can move to a2
        }
        
        @Test
        @DisplayName("Double check requires king move")
        void testDoubleCheckRequiresKingMove() {
            // Setup a double check position - correcting coordinate issues
            board.getSquares()[4][4] = new Piece("king", "white", "♔");   // White king at e4
            board.getSquares()[4][7] = new Piece("rook", "black", "♜");   // Black rook at h4 (horizontal check)
            board.getSquares()[7][1] = new Piece("bishop", "black", "♝");  // Black bishop at b1 (diagonal check)
            board.getSquares()[3][4] = new Piece("queen", "white", "♕");   // White queen at e5
            
            // Queen cannot block both checks
            assertFalse(ChessUtils.isValidMove(board, 3, 4, 3, 7, "white"));  
            
            // King must move out of check
            assertTrue(ChessUtils.isValidMove(board, 4, 4, 3, 3, "white"));  // Can move to d5
        }
        
        @Test
        @DisplayName("Cannot castle through check")
        void testCannotCastleThroughCheck() {
            // Setup a position for castling but with a square under attack
            board.getSquares()[7][4] = new Piece("king", "white", "♔");  // White king at e1
            board.getSquares()[7][7] = new Piece("rook", "white", "♖");  // White rook at h1
            // Correcting the bishop position to attack f1 (7,5) instead of f3
            board.getSquares()[5][5] = new Piece("bishop", "black", "♝"); // Black bishop attacks f1
            
            // Cannot castle kingside through check
            assertFalse(ChessUtils.isValidMove(board, 7, 4, 7, 6, "white"));
        }
        
        @Test
        @DisplayName("En passant capture cannot expose king to check")
        void testEnPassantCannotExposeKingToCheck() {
            // TODO: Implement this test when en passant is implemented
        }
    }
}
