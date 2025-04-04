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
            case "king":
                // Kings move one square in any direction
                if (Math.abs(fromRow - toRow) <= 1 && Math.abs(fromCol - toCol) <= 1) {
                    return true; // Valid king move
                }
                
                // Check for castling
                if (fromRow == toRow && Math.abs(fromCol - toCol) == 2) {
                    // Castling move (king moves two squares horizontally)
                    return isValidCastling(board, fromRow, fromCol, toRow, toCol, currentPlayer);
                }
                
                return false;
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
        
        // King must not have moved from its starting position
        if (king == null || !king.getType().equals("king")) {
            return false;
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
        
        // Check if the path between king and rook is clear
        if (!isPathClear(board, fromRow, fromCol, fromRow, rookCol)) {
            return false; // Path between king and rook is not clear
        }
        
        // Check if the king would move through or into check
        // For simplicity, we're just checking if the squares the king moves through are under attack
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
        
        // Check all opponent pieces on the board
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board.getSquares()[r][c];
                if (piece != null && piece.getColor().equals(opponentColor)) {
                    // Skip the opponent's king to avoid infinite recursion when checking for check
                    if (piece.getType().equals("king")) {
                        // For king, manually check if it can attack the square
                        if (Math.abs(r - row) <= 1 && Math.abs(c - col) <= 1) {
                            return true; // King can attack this square
                        }
                    } else {
                        // For all other pieces, use the isValidMove method to check if they can attack
                        // We temporarily place a dummy piece of the current player's color on the square to simulate "capturing" it
                        Piece originalPiece = board.getSquares()[row][col];
                        board.getSquares()[row][col] = new Piece("pawn", playerColor, "♙"); // Dummy piece
                        
                        boolean canAttack = isValidMove(board, r, c, row, col, opponentColor);
                        
                        // Restore the original piece (or null)
                        board.getSquares()[row][col] = originalPiece;
                        
                        if (canAttack) {
                            return true; // The square is under attack
                        }
                    }
                }
            }
        }
        
        return false; // The square is not under attack
    }
}