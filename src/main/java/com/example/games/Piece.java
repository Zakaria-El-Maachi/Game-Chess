package com.example.games;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Piece extends ImageView {

    public static final Image[] correspondance = {new Image("pw.png"),
            new Image("rw.png"),
            new Image("knw.png"),
            new Image("bw.png"),
            new Image("qw.png"),
            new Image("kw.png"),
            new Image("pb.png"),
            new Image("rb.png"),
            new Image("knb.png"),
            new Image("bb.png"),
            new Image("qb.png"),
            new Image("kb.png")};

    public static Piece[][] defaultSetup = {
        {new Rook(true), new Knight(true), new Bishop(true), new Queen(true), new King(true), new Bishop(true), new Knight(true), new Rook(true)},
        {new Pawn(true),new Pawn(true),new Pawn(true),new Pawn(true),new Pawn(true),new Pawn(true),new Pawn(true), new Pawn(true)},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {null, null, null, null, null, null, null, null},
        {new Pawn(false),new Pawn(false),new Pawn(false),new Pawn(false),new Pawn(false),new Pawn(false),new Pawn(false), new Pawn(false)},
        {new Rook(false), new Knight(false), new Bishop(false), new Queen(false), new King(false), new Bishop(false), new Knight(false), new Rook(false)},
    };

    private boolean color;
    public Piece(boolean color) {
        this.color = color;
    }

    public boolean getColor() {
        return color;
    }

    public abstract boolean validMove(Square start, Square end, Square[][] board);

}
