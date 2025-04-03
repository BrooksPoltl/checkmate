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
        default: 
            // Implement other piece movement rules (rook, knight, bishop, queen, king)
            return false;
        }
    }


    public static void applyMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getSquares()[fromRow][fromCol];
        board.getSquares()[toRow][toCol] = piece; // Move the piece
        board.getSquares()[fromRow][fromCol] = null; // Clear the original square
        
    }

    private static boolean isPathClear(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // TODO: Implement path checking logic
        return true;
    }
}