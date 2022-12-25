package com.example.games;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends Rectangle{
    private int[] coords;
    private Piece content;
    private Piece ghostContent;


    public Square(Color color, int dimension, int[] coords, Piece content) {
        this.coords = coords;
        this.content = content;
        setX(coords[0]);
        setY(coords[1]);
        setWidth(dimension);
        setHeight(dimension);
        setFill(color);
    }



    public int[] getCoords() {
        return coords;
    }

    public void setCoords(int[] coords) {
        this.coords = coords;
    }

    public Piece getContent() {
        return content;
    }

    public void setContent(Piece content) {
        this.content = content;
    }

    public Piece getGhostContent() {
        return ghostContent;
    }

    public void setGhostContent(Piece ghostContent) {
        this.ghostContent = ghostContent;
    }

}
