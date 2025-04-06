package com.checkmate.utils;

import com.checkmate.model.Board;
import com.checkmate.model.Piece;

public class ChessUtils {

    /**
     * Checks if a move is valid according to chess rules.
     * 
     * @param board The current chess board state
     * @param fromRow The row of the piece to be moved
     * @param fromCol The column of the piece to be moved
     * @param toRow The destination row
     * @param toCol The destination column
     * @param currentPlayer The color of the player making the move ("white" or "black")
     * @return true if the move is valid, false otherwise
     */
    public static boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol, String currentPlayer) {
        // Check if coordinates are within bounds
        if (fromRow < 0 || fromRow >= 8 || fromCol < 0 || fromCol >= 8 ||
            toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
            return false;
        }
        
        // Check if there's a piece at the source position
        Piece piece = board.getSquares()[fromRow][fromCol];
        if (piece == null || !piece.getColor().equals(currentPlayer)) {
            return false;
        }
        
        // Check if the destination has a piece of the same color
        Piece target = board.getSquares()[toRow][toCol];
        if (target != null && target.getColor().equals(currentPlayer)) {
            return false;
        }
        
        // Check piece-specific movement rules
        boolean validPieceMove;
        switch (piece.getType()) {
            case "pawn":
                validPieceMove = isPawnMoveValid(board, fromRow, fromCol, toRow, toCol);
                break;
            case "rook":
                validPieceMove = isRookMoveValid(board, fromRow, fromCol, toRow, toCol);
                break;
            case "knight":
                validPieceMove = isKnightMoveValid(fromRow, fromCol, toRow, toCol);
                break;
            case "bishop":
                validPieceMove = isBishopMoveValid(board, fromRow, fromCol, toRow, toCol);
                break;
            case "queen":
                validPieceMove = isQueenMoveValid(board, fromRow, fromCol, toRow, toCol);
                break;
            case "king":
                validPieceMove = isKingMoveValid(board, fromRow, fromCol, toRow, toCol, currentPlayer);
                break;
            default:
                return false;
        }
        
        if (!validPieceMove) {
            return false;
        }
        
        // Check if the move would leave the player's king in check
        if (wouldMoveExposeKingToCheck(board, fromRow, fromCol, toRow, toCol, currentPlayer)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Validates if a move is legal according to chess rules
     * 
     * @param board the current board state
     * @param fromRow starting row (0-7)
     * @param fromCol starting column (0-7)
     * @param toRow destination row (0-7)
     * @param toCol destination column (0-7)
     * @return true if the move is valid, false otherwise
     */
    public static boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // Get the current player's color from the board
        String currentPlayer = board.getCurrentTurn().toLowerCase();
        return isValidMove(board, fromRow, fromCol, toRow, toCol, currentPlayer);
    }
    
    /**
     * Makes a move on the board and returns the updated board
     * 
     * @param board the current board state
     * @param fromRow starting row (0-7)
     * @param fromCol starting column (0-7)
     * @param toRow destination row (0-7)
     * @param toCol destination column (0-7)
     * @return the updated board after the move
     */
    public static Board makeMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // Apply the move to the board
        applyMove(board, fromRow, fromCol, toRow, toCol);
        
        // Toggle the current player's turn
        board.toggleTurn();
        
        // Update the board state from the pieces array
        board.updateBoardState();
        
        return board;
    }
    
    /**
     * Checks if moving the piece would expose the king to check.
     * 
     * @param board The current chess board state
     * @param fromRow The row of the piece to be moved
     * @param fromCol The column of the piece to be moved
     * @param toRow The destination row
     * @param toCol The destination column
     * @param playerColor The color of the player making the move
     * @return true if the move would expose the king to check, false otherwise
     */
    private static boolean wouldMoveExposeKingToCheck(Board board, int fromRow, int fromCol, int toRow, int toCol, String playerColor) {
        // Create a temporary board to simulate the move
        Board tempBoard = new Board();
        Piece[][] originalSquares = board.getSquares();
        Piece[][] tempSquares = tempBoard.getSquares();
        
        // Copy the current state to the temporary board
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (originalSquares[r][c] != null) {
                    tempSquares[r][c] = new Piece(originalSquares[r][c].getType(), 
                                                 originalSquares[r][c].getColor(),
                                                 originalSquares[r][c].getSymbol());
                    tempSquares[r][c].setHasMoved(originalSquares[r][c].getHasMoved());
                } else {
                    tempSquares[r][c] = null;
                }
            }
        }
        
        // Simulate the move
        Piece movingPiece = tempSquares[fromRow][fromCol];
        tempSquares[toRow][toCol] = movingPiece;
        tempSquares[fromRow][fromCol] = null;
        
        // Handle castling (moving the rook)
        if (movingPiece.getType().equals("king") && Math.abs(fromCol - toCol) == 2) {
            int rookFromCol = toCol > fromCol ? 7 : 0;
            int rookToCol = toCol > fromCol ? toCol - 1 : toCol + 1;
            Piece rook = tempSquares[fromRow][rookFromCol];
            if (rook != null) {
                tempSquares[fromRow][rookToCol] = rook;
                tempSquares[fromRow][rookFromCol] = null;
            }
        }
        
        // Find the king's position
        int kingRow = -1, kingCol = -1;
        if (movingPiece.getType().equals("king")) {
            kingRow = toRow;
            kingCol = toCol;
        } else {
            // Find the king's position on the temporary board
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece p = tempSquares[r][c];
                    if (p != null && p.getType().equals("king") && p.getColor().equals(playerColor)) {
                        kingRow = r;
                        kingCol = c;
                        break;
                    }
                }
                if (kingRow != -1) break;
            }
        }
        
        // If no king is found, assume no check (shouldn't happen in a valid game)
        if (kingRow == -1) return false;
        
        // Check if any opponent piece can attack the king
        String opponentColor = playerColor.equals("white") ? "black" : "white";
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = tempSquares[r][c];
                if (p != null && p.getColor().equals(opponentColor)) {
                    if (canPieceAttackSquare(tempBoard, r, c, kingRow, kingCol)) {
                        return true; // King would be in check
                    }
                }
            }
        }
        
        return false; // King would not be in check
    }
    
    /**
     * Checks if a piece can attack a specific square.
     * 
     * @param board The current chess board state
     * @param pieceRow The row of the attacking piece
     * @param pieceCol The column of the attacking piece
     * @param targetRow The row of the target square
     * @param targetCol The column of the target square
     * @return true if the piece can attack the target square, false otherwise
     */
    private static boolean canPieceAttackSquare(Board board, int pieceRow, int pieceCol, int targetRow, int targetCol) {
        Piece piece = board.getSquares()[pieceRow][pieceCol];
        if (piece == null) return false;
        
        switch (piece.getType()) {
            case "pawn":
                // Pawns attack diagonally
                int direction = piece.getColor().equals("white") ? -1 : 1;
                return pieceRow + direction == targetRow && Math.abs(pieceCol - targetCol) == 1;
                
            case "rook":
                // Rooks attack horizontally or vertically
                if ((pieceRow == targetRow || pieceCol == targetCol) && 
                    isPathClear(board, pieceRow, pieceCol, targetRow, targetCol)) {
                    return true;
                }
                return false;
                
            case "knight":
                // Knights attack in L-shape and can jump over pieces
                return (Math.abs(pieceRow - targetRow) == 2 && Math.abs(pieceCol - targetCol) == 1) ||
                       (Math.abs(pieceRow - targetRow) == 1 && Math.abs(pieceCol - targetCol) == 2);
                
            case "bishop":
                // Bishops attack diagonally
                if (Math.abs(pieceRow - targetRow) == Math.abs(pieceCol - targetCol) && 
                    isPathClear(board, pieceRow, pieceCol, targetRow, targetCol)) {
                    return true;
                }
                return false;
                
            case "queen":
                // Queens attack horizontally, vertically, or diagonally
                if ((pieceRow == targetRow || pieceCol == targetCol || 
                     Math.abs(pieceRow - targetRow) == Math.abs(pieceCol - targetCol)) && 
                    isPathClear(board, pieceRow, pieceCol, targetRow, targetCol)) {
                    return true;
                }
                return false;
                
            case "king":
                // Kings attack one square in any direction
                return Math.abs(pieceRow - targetRow) <= 1 && Math.abs(pieceCol - targetCol) <= 1;
                
            default:
                return false;
        }
    }
    
    /**
     * Checks if a pawn move is valid according to chess rules.
     * 
     * @param board The current chess board state
     * @param fromRow The row of the pawn
     * @param fromCol The column of the pawn
     * @param toRow The destination row
     * @param toCol The destination column
     * @return true if the pawn move is valid, false otherwise
     */
    private static boolean isPawnMoveValid(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        Piece pawn = board.getSquares()[fromRow][fromCol];
        String color = pawn.getColor();
        
        // Define direction based on color
        int direction = color.equals("white") ? -1 : 1;
        int startRow = color.equals("white") ? 6 : 1;
        
        Piece targetPiece = board.getSquares()[toRow][toCol];
        boolean isCapture = targetPiece != null;
        
        // Forward movement (no capture)
        if (fromCol == toCol && !isCapture) {
            // Single step forward
            if (toRow == fromRow + direction) {
                return true;
            }
            
            // Double step from starting position
            if (fromRow == startRow && toRow == fromRow + 2 * direction) {
                // Check if path is clear
                return board.getSquares()[fromRow + direction][fromCol] == null;
            }
        }
        
        // Diagonal capture
        if (Math.abs(fromCol - toCol) == 1 && toRow == fromRow + direction) {
            return isCapture;
        }
        
        return false;
    }
    
    /**
     * Checks if a rook move is valid according to chess rules.
     * 
     * @param board The current chess board state
     * @param fromRow The row of the rook
     * @param fromCol The column of the rook
     * @param toRow The destination row
     * @param toCol The destination column
     * @return true if the rook move is valid, false otherwise
     */
    private static boolean isRookMoveValid(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // Rooks move horizontally or vertically
        if (fromRow != toRow && fromCol != toCol) {
            return false;
        }
        
        return isPathClear(board, fromRow, fromCol, toRow, toCol);
    }
    
    /**
     * Checks if a knight move is valid according to chess rules.
     * 
     * @param fromRow The row of the knight
     * @param fromCol The column of the knight
     * @param toRow The destination row
     * @param toCol The destination column
     * @return true if the knight move is valid, false otherwise
     */
    private static boolean isKnightMoveValid(int fromRow, int fromCol, int toRow, int toCol) {
        // Knights move in an L-shape
        return (Math.abs(fromRow - toRow) == 2 && Math.abs(fromCol - toCol) == 1) ||
               (Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 2);
    }
    
    /**
     * Checks if a bishop move is valid according to chess rules.
     * 
     * @param board The current chess board state
     * @param fromRow The row of the bishop
     * @param fromCol The column of the bishop
     * @param toRow The destination row
     * @param toCol The destination column
     * @return true if the bishop move is valid, false otherwise
     */
    private static boolean isBishopMoveValid(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // Bishops move diagonally
        if (Math.abs(fromRow - toRow) != Math.abs(fromCol - toCol)) {
            return false;
        }
        
        return isPathClear(board, fromRow, fromCol, toRow, toCol);
    }
    
    /**
     * Checks if a queen move is valid according to chess rules.
     * 
     * @param board The current chess board state
     * @param fromRow The row of the queen
     * @param fromCol The column of the queen
     * @param toRow The destination row
     * @param toCol The destination column
     * @return true if the queen move is valid, false otherwise
     */
    private static boolean isQueenMoveValid(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // Queens move like bishops or rooks
        boolean isDiagonal = Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol);
        boolean isStraight = fromRow == toRow || fromCol == toCol;
        
        if (!isDiagonal && !isStraight) {
            return false;
        }
        
        return isPathClear(board, fromRow, fromCol, toRow, toCol);
    }
    
    /**
     * Checks if a king move is valid according to chess rules.
     * 
     * @param board The current chess board state
     * @param fromRow The row of the king
     * @param fromCol The column of the king
     * @param toRow The destination row
     * @param toCol The destination column
     * @param currentPlayer The color of the player making the move
     * @return true if the king move is valid, false otherwise
     */
    private static boolean isKingMoveValid(Board board, int fromRow, int fromCol, int toRow, int toCol, String currentPlayer) {
        // Regular king move (one square in any direction)
        if (Math.abs(fromRow - toRow) <= 1 && Math.abs(fromCol - toCol) <= 1) {
            return true;
        }
        
        // Castling (king moves two squares horizontally)
        if (fromRow == toRow && Math.abs(fromCol - toCol) == 2) {
            return isValidCastling(board, fromRow, fromCol, toRow, toCol, currentPlayer);
        }
        
        return false;
    }
    
    /**
     * Checks if castling is valid according to chess rules.
     * 
     * @param board The current chess board state
     * @param fromRow The row of the king
     * @param fromCol The column of the king
     * @param toRow The destination row of the king
     * @param toCol The destination column of the king
     * @param currentPlayer The color of the player making the move
     * @return true if castling is valid, false otherwise
     */
    private static boolean isValidCastling(Board board, int fromRow, int fromCol, int toRow, int toCol, String currentPlayer) {
        Piece king = board.getSquares()[fromRow][fromCol];
        
        // King must not have moved yet
        if (king.getHasMoved()) {
            return false;
        }
        
        // Determine if it's kingside or queenside castling
        boolean isKingsideCastling = toCol > fromCol;
        int rookCol = isKingsideCastling ? 7 : 0;
        
        // Check if the rook is in position and hasn't moved
        Piece rook = board.getSquares()[fromRow][rookCol];
        if (rook == null || !rook.getType().equals("rook") || 
            !rook.getColor().equals(currentPlayer) || rook.getHasMoved()) {
            return false;
        }
        
        // Check if the path between king and rook is clear
        if (!isPathClear(board, fromRow, fromCol, fromRow, rookCol)) {
            return false;
        }
        
        // Check if the king is in check
        if (isKingInCheck(board, fromRow, fromCol, currentPlayer)) {
            return false;
        }
        
        // Check if king would move through or into check
        int direction = isKingsideCastling ? 1 : -1;
        int midCol = fromCol + direction;
        
        // Simulate the move to check squares the king passes through
        Board tempBoard = simulateMove(board, fromRow, fromCol, fromRow, midCol);
        if (isKingInCheck(tempBoard, fromRow, midCol, currentPlayer)) {
            return false; // King would move through check
        }
        
        tempBoard = simulateMove(board, fromRow, fromCol, fromRow, toCol);
        if (isKingInCheck(tempBoard, fromRow, toCol, currentPlayer)) {
            return false; // King would end up in check
        }
        
        return true;
    }
    
    /**
     * Simulates a move on a temporary board without modifying the original board.
     * 
     * @param board The current chess board state
     * @param fromRow The row of the piece to be moved
     * @param fromCol The column of the piece to be moved
     * @param toRow The destination row
     * @param toCol The destination column
     * @return A new board with the simulated move applied
     */
    private static Board simulateMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        Board tempBoard = new Board();
        Piece[][] origSquares = board.getSquares();
        Piece[][] tempSquares = tempBoard.getSquares();
        
        // Create a deep copy of the board
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (origSquares[r][c] != null) {
                    tempSquares[r][c] = new Piece(origSquares[r][c].getType(), 
                                                origSquares[r][c].getColor(),
                                                origSquares[r][c].getSymbol());
                    tempSquares[r][c].setHasMoved(origSquares[r][c].getHasMoved());
                } else {
                    tempSquares[r][c] = null;
                }
            }
        }
        
        // Make the move
        tempSquares[toRow][toCol] = tempSquares[fromRow][fromCol];
        tempSquares[fromRow][fromCol] = null;
        
        return tempBoard;
    }
    
    /**
     * Checks if the king is in check.
     * 
     * @param board The current chess board state
     * @param kingRow The row of the king
     * @param kingCol The column of the king
     * @param kingColor The color of the king
     * @return true if the king is in check, false otherwise
     */
    private static boolean isKingInCheck(Board board, int kingRow, int kingCol, String kingColor) {
        String opponentColor = kingColor.equals("white") ? "black" : "white";
        
        // Check if any opponent piece can attack the king
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board.getSquares()[r][c];
                if (piece != null && piece.getColor().equals(opponentColor)) {
                    if (canPieceAttackSquare(board, r, c, kingRow, kingCol)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Checks if the path between two positions is clear of any pieces.
     * 
     * @param board The current chess board state
     * @param fromRow The starting row
     * @param fromCol The starting column
     * @param toRow The ending row
     * @param toCol The ending column
     * @return true if the path is clear, false if any piece is blocking the path
     */
    public static boolean isPathClear(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // Get direction of movement
        int rowStep = Integer.compare(toRow, fromRow);
        int colStep = Integer.compare(toCol, fromCol);
        
        // Start from the square after the start position
        int row = fromRow + rowStep;
        int col = fromCol + colStep;
        
        // Check each square along the path (excluding the destination)
        while (row != toRow || col != toCol) {
            if (board.getSquares()[row][col] != null) {
                return false; // Path is blocked
            }
            row += rowStep;
            col += colStep;
        }
        
        return true; // Path is clear
    }
    
    /**
     * Applies a move to the board, updating the position of the pieces.
     * Also handles special moves like castling.
     * 
     * @param board The chess board to modify
     * @param fromRow The row of the piece to be moved
     * @param fromCol The column of the piece to be moved
     * @param toRow The destination row
     * @param toCol The destination column
     */
    public static void applyMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getSquares()[fromRow][fromCol];
        
        // Mark the piece as moved
        if (piece != null) {
            piece.setHasMoved(true);
        }
        
        // Move the piece
        board.getSquares()[toRow][toCol] = piece;
        board.getSquares()[fromRow][fromCol] = null;
        
        // Handle castling (move the rook too)
        if (piece != null && piece.getType().equals("king") && Math.abs(fromCol - toCol) == 2) {
            int rookFromCol = toCol > fromCol ? 7 : 0;
            int rookToCol = toCol > fromCol ? toCol - 1 : toCol + 1;
            
            Piece rook = board.getSquares()[fromRow][rookFromCol];
            if (rook != null && rook.getType().equals("rook")) {
                rook.setHasMoved(true);
                board.getSquares()[fromRow][rookToCol] = rook;
                board.getSquares()[fromRow][rookFromCol] = null;
            }
        }
    }
    
    /**
     * Performs a castling move, moving both the king and rook.
     * 
     * @param board The chess board to modify
     * @param kingRow The row of the king
     * @param kingCol The column of the king
     * @param rookRow The row of the rook
     * @param rookCol The column of the rook
     * @throws IllegalArgumentException If the pieces at the provided positions are not a king and a rook
     */
    public static void castleRookWithKing(Board board, int kingRow, int kingCol, int rookRow, int rookCol) {
        Piece king = board.getSquares()[kingRow][kingCol];
        Piece rook = board.getSquares()[rookRow][rookCol];
        
        if (king == null || rook == null || !king.getType().equals("king") || !rook.getType().equals("rook")) {
            throw new IllegalArgumentException("Castling requires a king and a rook");
        }
        
        // Kingside or queenside castling
        boolean isKingsideCastle = rookCol > kingCol;
        
        // New positions
        int newKingCol = isKingsideCastle ? kingCol + 2 : kingCol - 2;
        int newRookCol = isKingsideCastle ? kingCol + 1 : kingCol - 1;
        
        // Mark pieces as moved
        king.setHasMoved(true);
        rook.setHasMoved(true);
        
        // Move pieces
        board.getSquares()[kingRow][newKingCol] = king;
        board.getSquares()[kingRow][newRookCol] = rook;
        board.getSquares()[kingRow][kingCol] = null;
        board.getSquares()[rookRow][rookCol] = null;
    }
}