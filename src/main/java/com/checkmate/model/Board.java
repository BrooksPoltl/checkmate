package com.checkmate.model;

import com.checkmate.model.Piece;

public class Board {
    private Piece[][] squares;

    public Board() {
        squares = new Piece[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        squares[0][0] = new Piece("rook", "black", "♜");
        squares[0][1] = new Piece("knight", "black", "♞");
        squares[0][2] = new Piece("bishop", "black", "♝");
        squares[0][3] = new Piece("queen", "black", "♛");
        squares[0][4] = new Piece("king", "black", "♚");
        squares[0][5] = new Piece("bishop", "black", "♝");
        squares[0][6] = new Piece("knight", "black", "♞");
        squares[0][7] = new Piece("rook", "black", "♜");
        for (int col = 0; col < 8; col++) {
            squares[1][col] = new Piece("pawn", "black", "♟");
        }
        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = null;
            }
        }
        for (int col = 0; col < 8; col++) {
            squares[6][col] = new Piece("pawn", "white", "♙");
        }
        squares[7][0] = new Piece("rook", "white", "♖");
        squares[7][1] = new Piece("knight", "white", "♘");
        squares[7][2] = new Piece("bishop", "white", "♗");
        squares[7][3] = new Piece("queen", "white", "♕");
        squares[7][4] = new Piece("king", "white", "♔");
        squares[7][5] = new Piece("bishop", "white", "♗");
        squares[7][6] = new Piece("knight", "white", "♘");
        squares[7][7] = new Piece("rook", "white", "♖");
    }

    public Piece[][] getSquares() {
        return squares;
    }
}