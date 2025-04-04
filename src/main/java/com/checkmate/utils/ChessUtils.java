package com.checkmate.utils;

import com.checkmate.model.Board;
import com.checkmate.model.Piece;

public class ChessUtils {

    public static boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol, String currentPlayer) {
        Piece piece = board.getSquares()[fromRow][fromCol];
        // Check if the piece belongs to the current player
        if (piece == null || !piece.getColor().equals(currentPlayer)) return false;

        Piece target = board.getSquares()[toRow][toCol];

        boolean isCapture = target != null && !target.getColor().equals(piece.getColor());

        // Check if destination contains a piece of the same color
        if (target != null && target.getColor().equals(piece.getColor())) {
            return false; // Cannot capture own pieces
        }

        boolean isValidBasedOnPieceRules = false;
        
        switch (piece.getType()) {
            case "pawn":
                int direction = piece.getColor().equals("white") ? -1 : 1;
                int startRow = piece.getColor().equals("white") ? 6 : 1;

                // Forward move (one or two)
                if (fromCol == toCol && target == null) {
                    // Single step forward
                    if (toRow == fromRow + direction) isValidBasedOnPieceRules = true;
                    // Double step forward from starting position
                    if (fromRow == startRow && toRow == fromRow + 2 * direction
                        && board.getSquares()[fromRow + direction][toCol] == null) isValidBasedOnPieceRules = true;
                }
                // Diagonal capture
                if (Math.abs(fromCol - toCol) == 1 && toRow == fromRow + direction && isCapture) isValidBasedOnPieceRules = true;
                break;
                
            case "rook":
                // Rooks move horizontally or vertically any number of squares
                if (fromRow != toRow && fromCol != toCol) {
                    isValidBasedOnPieceRules = false; // Not a horizontal or vertical move
                } else {
                    // Check if the path is clear
                    isValidBasedOnPieceRules = isPathClear(board, fromRow, fromCol, toRow, toCol);
                }
                break;
            
            case "bishop":
                // Bishops move diagonally any number of squares
                if (Math.abs(fromRow - toRow) != Math.abs(fromCol - toCol)) {
                    isValidBasedOnPieceRules = false; // Not a diagonal move
                } else {
                    // Check if the path is clear
                    isValidBasedOnPieceRules = isPathClear(board, fromRow, fromCol, toRow, toCol);
                }
                break;
            
            case "knight":
                // Knights move in an L-shape: two squares in one direction and one square perpendicular
                if ((Math.abs(fromRow - toRow) == 2 && Math.abs(fromCol - toCol) == 1) ||
                    (Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 2)) {
                    isValidBasedOnPieceRules = true; // Valid knight move
                }
                break;
                
            case "queen":
                // Queens move like both rooks and bishops
                if (fromRow != toRow && fromCol != toCol) {
                    if (Math.abs(fromRow - toRow) != Math.abs(fromCol - toCol)) {
                        isValidBasedOnPieceRules = false; // Not a valid queen move
                    } else {
                        // Check if the path is clear for diagonal movement
                        isValidBasedOnPieceRules = isPathClear(board, fromRow, fromCol, toRow, toCol);
                    }
                } else {
                    // Check if the path is clear for horizontal/vertical movement
                    isValidBasedOnPieceRules = isPathClear(board, fromRow, fromCol, toRow, toCol);
                }
                break;
                
            case "king":
                // Kings move one square in any direction
                if (Math.abs(fromRow - toRow) <= 1 && Math.abs(fromCol - toCol) <= 1) {
                    isValidBasedOnPieceRules = true; // Valid king move
                } else if (fromRow == toRow && Math.abs(fromCol - toCol) == 2) {
                    // Castling move (king moves two squares horizontally)
                    isValidBasedOnPieceRules = isValidCastling(board, fromRow, fromCol, toRow, toCol, currentPlayer);
                }
                break;
                
            default: 
                isValidBasedOnPieceRules = false;
        }
        
        // If the move is not valid based on piece rules, return immediately
        if (!isValidBasedOnPieceRules) {
            return false;
        }
        
        // Check if this move would put or leave the player's own king in check
        if (wouldMoveResultInCheck(board, fromRow, fromCol, toRow, toCol, currentPlayer)) {
            return false;
        }
        
        return true;
    }

    /**
     * Checks if a move would result in the player's own king being in check
     *
     * @param board The chess board
     * @param fromRow Starting row
     * @param fromCol Starting column
     * @param toRow Ending row
     * @param toCol Ending column
     * @param playerColor The current player's color
     * @return true if the move would result in check, false otherwise
     */
    private static boolean wouldMoveResultInCheck(Board board, int fromRow, int fromCol, int toRow, int toCol, String playerColor) {
        // Create a temporary copy of the board to simulate the move
        Board tempBoard = createBoardCopy(board);
        
        // Make the move on the temporary board
        Piece piece = tempBoard.getSquares()[fromRow][fromCol];
        Piece capturedPiece = tempBoard.getSquares()[toRow][toCol];
        tempBoard.getSquares()[toRow][toCol] = piece;
        tempBoard.getSquares()[fromRow][fromCol] = null;
        
        // Find the king's position
        int[] kingPosition = findKingPosition(tempBoard, playerColor);
        
        // If the king wasn't found (shouldn't happen in a valid chess position), assume check
        if (kingPosition == null) {
            return true;
        }
        
        // Check if the king is in check after the move
        boolean inCheck = isSquareUnderAttack(tempBoard, kingPosition[0], kingPosition[1], playerColor);
        
        return inCheck;
    }
    
    /**
     * Creates a deep copy of the chess board
     *
     * @param original The original board
     * @return A new board with the same piece positions
     */
    private static Board createBoardCopy(Board original) {
        Board copy = new Board();
        Piece[][] originalSquares = original.getSquares();
        Piece[][] copySquares = copy.getSquares();
        
        // Clear the new board (which has the initial setup)
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                copySquares[row][col] = null;
            }
        }
        
        // Copy the pieces from the original board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece originalPiece = originalSquares[row][col];
                if (originalPiece != null) {
                    // Create a new piece with the same properties
                    Piece newPiece = new Piece(
                        originalPiece.getType(),
                        originalPiece.getColor(),
                        originalPiece.getSymbol()
                    );
                    newPiece.setHasMoved(originalPiece.getHasMoved());
                    copySquares[row][col] = newPiece;
                }
            }
        }
        
        return copy;
    }
    
    /**
     * Finds the position of a player's king on the board
     *
     * @param board The chess board
     * @param playerColor The player's color ("white" or "black")
     * @return An array containing the king's [row, col], or null if not found
     */
    private static int[] findKingPosition(Board board, String playerColor) {
        Piece[][] squares = board.getSquares();
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col];
                if (piece != null && piece.getType().equals("king") && piece.getColor().equals(playerColor)) {
                    return new int[]{row, col};
                }
            }
        }
        
        return null; // King not found (shouldn't happen in a valid chess position)
    }

    public static void applyMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getSquares()[fromRow][fromCol];
        
        // Mark the piece as having moved
        if (piece != null) {
            piece.setHasMoved(true);
        }
        
        board.getSquares()[toRow][toCol] = piece; // Move the piece
        board.getSquares()[fromRow][fromCol] = null; // Clear the original square
    }
    
    /**
     * Applies a castling move between the king and a rook
     * 
     * @param board The chess board
     * @param kingRow The row of the king
     * @param kingCol The column of the king
     * @param rookRow The row of the rook
     * @param rookCol The column of the rook
     */
    public static void castleRookWithKing(Board board, int kingRow, int kingCol, int rookRow, int rookCol) {
        Piece king = board.getSquares()[kingRow][kingCol];
        Piece rook = board.getSquares()[rookRow][rookCol];
        
        if (king == null || rook == null || !king.getType().equals("king") || !rook.getType().equals("rook")) {
            throw new IllegalArgumentException("Castling requires a king and a rook");
        }
        
        // Determine if this is a kingside or queenside castle
        boolean isKingsideCastle = rookCol > kingCol;
        
        // New positions for the pieces
        int newKingCol = isKingsideCastle ? kingCol + 2 : kingCol - 2;
        int newRookCol = isKingsideCastle ? kingCol + 1 : kingCol - 1;
        
        // Mark both pieces as having moved
        king.setHasMoved(true);
        rook.setHasMoved(true);
        
        // Move the king
        board.getSquares()[kingRow][kingCol] = null;
        board.getSquares()[kingRow][newKingCol] = king;
        
        // Move the rook
        board.getSquares()[rookRow][rookCol] = null;
        board.getSquares()[kingRow][newRookCol] = rook;
    }

    /**
     * Checks if the path between two squares is clear (no pieces in the way)
     * This is used for pieces like rooks, bishops, and queens that cannot jump over other pieces
     * 
     * @param board The chess board
     * @param fromRow Starting row
     * @param fromCol Starting column
     * @param toRow Ending row
     * @param toCol Ending column
     * @return true if the path is clear, false otherwise
     */
    public static boolean isPathClear(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // Determine the direction of movement
        int rowStep = 0;
        int colStep = 0;
        
        if (fromRow < toRow) rowStep = 1;
        else if (fromRow > toRow) rowStep = -1;
        
        if (fromCol < toCol) colStep = 1;
        else if (fromCol > toCol) colStep = -1;
        
        // Start from the square after the starting position
        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;
        
        // Check each square along the path (excluding the destination square)
        while (currentRow != toRow || currentCol != toCol) {
            if (board.getSquares()[currentRow][currentCol] != null) {
                return false; // Path is blocked
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        
        return true; // Path is clear
    }
    
    /**
     * Checks if a castling move is valid
     * 
     * @param board The chess board
     * @param fromRow Starting row of the king
     * @param fromCol Starting column of the king
     * @param toRow Ending row of the king
     * @param toCol Ending column of the king
     * @param currentPlayer The current player ("white" or "black")
     * @return true if the castling move is valid, false otherwise
     */
    private static boolean isValidCastling(Board board, int fromRow, int fromCol, int toRow, int toCol, String currentPlayer) {
        Piece king = board.getSquares()[fromRow][fromCol];
        
        // Basic checks for king position
        if (king == null || !king.getType().equals("king")) {
            return false;
        }
        
        // Check if king has moved - use the hasMoved flag we set in applyMove
        if (king.getHasMoved()) {
            return false; // King has already moved, castling not allowed
        }
        
        // Check if king is in the correct starting position
        boolean isWhiteKing = king.getColor().equals("white");
        if ((isWhiteKing && fromRow != 7) || (!isWhiteKing && fromRow != 0) || fromCol != 4) {
            return false; // King is not in its starting position
        }
        
        // Determine if kingside (short) or queenside (long) castling
        boolean isKingsideCastle = toCol > fromCol; // King is moving to the right
        int rookCol = isKingsideCastle ? 7 : 0;
        
        // Check if the rook is in the correct position
        Piece rook = board.getSquares()[fromRow][rookCol];
        if (rook == null || !rook.getType().equals("rook") || !rook.getColor().equals(king.getColor())) {
            return false; // Rook is not in its starting position or not the correct piece
        }
        
        // Check if the rook has moved - use the hasMoved flag
        if (rook.getHasMoved()) {
            return false; // Rook has already moved, castling not allowed
        }
        
        // Check if the path between king and rook is clear
        if (!isPathClear(board, fromRow, fromCol, fromRow, rookCol)) {
            return false; // Path between king and rook is not clear
        }
        
        // Check if the king is currently in check
        if (isKingInCheck(board, fromRow, fromCol, currentPlayer)) {
            return false; // King is in check, castling not allowed
        }
        
        // Check if the king would move through or into check
        int direction = isKingsideCastle ? 1 : -1;
        
        // Check the square the king moves through
        if (isSquareUnderAttack(board, fromRow, fromCol + direction, currentPlayer)) {
            return false; // The square the king passes through is under attack
        }
        
        // Check the destination square
        if (isSquareUnderAttack(board, fromRow, toCol, currentPlayer)) {
            return false; // The king's destination square is under attack
        }
        
        return true; // All checks passed, castling is valid
    }
    
    /**
     * Checks if the king is in check
     * 
     * @param board The chess board
     * @param kingRow Row of the king
     * @param kingCol Column of the king
     * @param playerColor Color of the player whose king is being checked ("white" or "black")
     * @return true if the king is in check, false otherwise
     */
    private static boolean isKingInCheck(Board board, int kingRow, int kingCol, String playerColor) {
        return isSquareUnderAttack(board, kingRow, kingCol, playerColor);
    }
    
    /**
     * Checks if a square is under attack by any opponent piece
     * 
     * @param board The chess board
     * @param row Row of the square to check
     * @param col Column of the square to check
     * @param playerColor Color of the player whose turn it is ("white" or "black")
     * @return true if the square is under attack, false otherwise
     */
    private static boolean isSquareUnderAttack(Board board, int row, int col, String playerColor) {
        String opponentColor = playerColor.equals("white") ? "black" : "white";
        
        // Keep track of the original piece on the square being checked
        Piece originalPiece = board.getSquares()[row][col];
        
        // If the square has a piece of the player's color, temporarily remove it
        // to avoid interference when checking if the square is under attack
        if (originalPiece != null && originalPiece.getColor().equals(playerColor)) {
            board.getSquares()[row][col] = null;
        }
        
        try {
            // Check all opponent pieces on the board
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece piece = board.getSquares()[r][c];
                    if (piece != null && piece.getColor().equals(opponentColor)) {
                        // For king, manually check if it can attack the square to avoid infinite recursion
                        if (piece.getType().equals("king")) {
                            if (Math.abs(r - row) <= 1 && Math.abs(c - col) <= 1) {
                                return true; // King can attack this square
                            }
                        } 
                        // For pawns, manually check diagonal attacks
                        else if (piece.getType().equals("pawn")) {
                            int direction = opponentColor.equals("white") ? -1 : 1;
                            if (r + direction == row && (c - 1 == col || c + 1 == col)) {
                                return true; // Pawn can attack this square
                            }
                        }
                        // For all other pieces, use a simplified check to avoid recursive issues
                        else {
                            if (piece.getType().equals("rook")) {
                                // Rook checks - horizontal or vertical movement
                                if ((r == row || c == col) && isPathClear(board, r, c, row, col)) {
                                    return true;
                                }
                            } else if (piece.getType().equals("bishop")) {
                                // Bishop checks - diagonal movement
                                if (Math.abs(r - row) == Math.abs(c - col) && isPathClear(board, r, c, row, col)) {
                                    return true;
                                }
                            } else if (piece.getType().equals("queen")) {
                                // Queen checks - combination of rook and bishop
                                if ((r == row || c == col || Math.abs(r - row) == Math.abs(c - col)) 
                                    && isPathClear(board, r, c, row, col)) {
                                    return true;
                                }
                            } else if (piece.getType().equals("knight")) {
                                // Knight checks - L-shaped movement
                                if ((Math.abs(r - row) == 2 && Math.abs(c - col) == 1) ||
                                    (Math.abs(r - row) == 1 && Math.abs(c - col) == 2)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            
            return false; // The square is not under attack
        } finally {
            // Restore the original piece
            board.getSquares()[row][col] = originalPiece;
        }
    }
}