package com.example.games;

public class Move {
    private Piece piece;
    private Piece takenPiece;
    private Square start;
    private Square end;
    private int[] coords;


    public Move(Piece piece, Piece takenPiece, Square start, Square end) {
        this.piece = piece;
        this.takenPiece = takenPiece;
        this.start = start;
        this.end = end;
    }

    public Move(Piece piece, int[] coords) {
        this.piece = piece;
        this.coords = coords;

    }

    public Move(Piece piece, Square start, Square end) {
        this.piece = piece;
        this.start = start;
        this.end = end;

    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getTakenPiece() {
        return takenPiece;
    }

    public Square getStart() {
        return start;
    }

    public Square getEnd() {
        return end;
    }

    public int[] getCoords() {
        return coords;
    }

    public void setCoords(int[] coords) {
        this.coords = coords;
    }
}
