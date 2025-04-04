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

        switch (piece.getType()) {
            case "pawn":
                int direction = piece.getColor().equals("white") ? -1 : 1;
                int startRow = piece.getColor().equals("white") ? 6 : 1;

                // Forward move (one or two)
                if (fromCol == toCol && target == null) {
                    // Single step forward
                    if (toRow == fromRow + direction) return true;
                    // Double step forward from starting position
                    if (fromRow == startRow && toRow == fromRow + 2 * direction
                        && board.getSquares()[fromRow + direction][toCol] == null) return true;
                }
                // Diagonal capture
                if (Math.abs(fromCol - toCol) == 1 && toRow == fromRow + direction && isCapture) return true;
                return false;
                
            case "rook":
                // Rooks move horizontally or vertically any number of squares
                if (fromRow != toRow && fromCol != toCol) {
                    return false; // Not a horizontal or vertical move
                }
                
                // Check if the path is clear
                return isPathClear(board, fromRow, fromCol, toRow, toCol);
            
            case "bishop":
                // Bishops move diagonally any number of squares
                if (Math.abs(fromRow - toRow) != Math.abs(fromCol - toCol)) {
                    return false; // Not a diagonal move
                }
                
                // Check if the path is clear
                return isPathClear(board, fromRow, fromCol, toRow, toCol);
            
            case "knight":
                // Knights move in an L-shape: two squares in one direction and one square perpendicular
                if ((Math.abs(fromRow - toRow) == 2 && Math.abs(fromCol - toCol) == 1) ||
                    (Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 2)) {
                    return true; // Valid knight move
                }
                return false;
            case "queen":
                // Queens move like both rooks and bishops
                if (fromRow != toRow && fromCol != toCol) {
                    if (Math.abs(fromRow - toRow) != Math.abs(fromCol - toCol)) {
                        return false; // Not a valid queen move
                    }
                }
                
                // Check if the path is clear
                return isPathClear(board, fromRow, fromCol, toRow, toCol);
            default: 
                // Implement other piece movement rules (knight, bishop, queen, king)
                return false;
        }
    }


    public static void applyMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getSquares()[fromRow][fromCol];
        board.getSquares()[toRow][toCol] = piece; // Move the piece
        board.getSquares()[fromRow][fromCol] = null; // Clear the original square
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
}