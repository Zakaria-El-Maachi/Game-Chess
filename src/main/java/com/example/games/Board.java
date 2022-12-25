package com.example.games;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Board extends GridPane {

    ArrayList<Square> castlingRights = new ArrayList<>();
    int[] whiteKing;
    int[] blackKing;
    private boolean currentPlayer = true;
    private Square start;
    private Square end;
    private int squareDimension;
    private Square[][] board = new Square[8][8];
    private InnerShadow shadow1 = new InnerShadow();
    private InnerShadow shadow2 = new InnerShadow();
    private ArrayList<Move> possibilities = new ArrayList<Move>();
    boolean promotion = false;
    Piece promotionTaken;
    public static class Turn extends Event{
        public boolean turn;
        public Piece taken;

        public static final EventType<Turn> changeTurns = new EventType<>(Event.ANY, "Changing Turns");
        public Turn(boolean turn, Piece takenPiece) {
            super(changeTurns);
            this.turn = turn;
            this.taken = takenPiece;
        }
    }
    public static class Promoting extends Event{
        public Square promotionSquare;
        public static final EventType<Promoting> promotion = new EventType<>(Event.ANY, "Promoting");

        public Promoting(Square pr){
            super(promotion);
            this.promotionSquare = pr;
        }
    }

    public static class Winner extends Event{
        public boolean color;
        public static final EventType<Winner> winner = new EventType<>(Event.ANY, "Winner");

        public Winner(boolean clr){
            super(winner);
            this.color = clr;
        }
    }

    public Board(Color c1, Color c2, int squareDimension) {
        shadow1.setHeight(30);
        shadow1.setWidth(30);
        shadow1.setRadius(40);
        shadow1.setChoke(0.4);
        shadow1.setColor(c1.darker().darker());
        shadow2.setHeight(30);
        shadow2.setWidth(30);
        shadow2.setRadius(40);
        shadow2.setChoke(0.4);
        shadow2.setColor(c2.darker().darker());
        this.squareDimension = squareDimension;
        for(int i = 0; i < 8; i++){
            for (int j = 0; j<8; j++){
                int[] coords = {j, i};
                Color c = c2;
                if((i+j)%2 == 0){
                    c = c1;
                }
                board[j][i] = new Square(c, squareDimension, coords, Piece.defaultSetup[i][j]);
                add(board[j][i],j ,7-i);
                if (board[j][i].getContent() != null){
                    add(board[j][i].getContent(), j ,7-i);
                }
            }
        }
        whiteKing = new int[]{4, 0};
        blackKing = new int[]{4, 7};
        castlingRights.add(board[0][0]);
        castlingRights.add(board[7][0]);
        castlingRights.add(board[0][7]);
        castlingRights.add(board[7][7]);
        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int x = (int)mouseEvent.getX()/squareDimension;
                int y = 7-(int)mouseEvent.getY()/squareDimension;
                Piece taken = null;
                if (board[x][y].getContent() != null && board[x][y].getContent().getColor() == currentPlayer){
                    start = board[x][y];
                    resetPossibilities();
                    possibilities = possibleMoves(start);
                    if(!possibilities.isEmpty()){
                        displayPossibilities();
                    }
                }
                else if (start != null){
                    end = board[x][y];
                    boolean truth = false;
                    for (Move move : possibilities){
                        if (move.getEnd() == end){
                            truth = true;
                            taken = move(start, end);
                        }
                    }
                    start = null;
                    end = null;
                    resetPossibilities();
                    possibilities = null;
                    if (truth){
                        if (!promotion) {
                            endTurn(taken);
                        }
                    }
                }
            }
        });
    }

    public void endTurn(Piece taken){
        fireEvent(new Turn(currentPlayer, taken));
        deleteGhost();
        currentPlayer = !currentPlayer;
        if (currentPlayer && checkmate(true)) {
            fireEvent(new Winner(false));
        } else if (currentPlayer && checkmate(false)) {
            fireEvent(new Winner(true));
        }
    }
    public Square[][] getBoard() {
        return board;
    }

    public void setBoard(Square[][] board) {
        this.board = board;
    }

    public int getSquareDimension() {
        return squareDimension;
    }

    public void setSquareDimension(int squareDimension) {
        this.squareDimension = squareDimension;
    }

    private void displayPossibilities(){
        for(Move move : possibilities){
            if((move.getEnd().getCoords()[0]+move.getEnd().getCoords()[1])%2 == 0){
                move.getEnd().setEffect(shadow1);
            }
            else{
                move.getEnd().setEffect(shadow2);
            }
        }
    }
    private void resetPossibilities(){
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                board[i][j].setEffect(null);
            }
        }
    }
    private ArrayList<Move> possibleMoves(Square start){
        boolean legality;
        byte kingMove;
        Square end;
        Piece target;
        ArrayList<Move> possibleMoves = new ArrayList<>();
        if(start.getContent() instanceof King){
            if(start.getContent().getColor() && board[1][0].getContent() == null && board[2][0].getContent() == null && board[3][0].getContent() == null && castlingRights.contains(board[0][0])){
                possibleMoves.add(new Move(board[2][0].getContent(), start, board[2][0]));
            } else if (start.getContent().getColor() && board[5][0].getContent() == null && board[6][0].getContent() == null && castlingRights.contains(board[7][0])) {
                possibleMoves.add(new Move(board[6][0].getContent(), start, board[6][0]));
            } else if(!start.getContent().getColor() && board[1][7].getContent() == null && board[2][7].getContent() == null && board[3][7].getContent() == null && castlingRights.contains(board[0][7])){
                possibleMoves.add(new Move(board[2][7].getContent(), start, board[2][7]));
            } else if(!start.getContent().getColor() && board[5][7].getContent() == null && board[6][7].getContent() == null && castlingRights.contains(board[7][7])){
                possibleMoves.add(new Move(board[6][7].getContent(), start, board[6][7]));
            }
        }
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                legality = true;
                kingMove = 0;
                end = board[i][j];
                target = end.getContent();
                if (start.getContent().validMove(start, end, board)) {
                    if (start.getContent() instanceof King){
                        if(start.getContent().getColor()){
                            whiteKing = end.getCoords();
                            kingMove = 1;
                        }
                        else{
                            blackKing = end.getCoords();
                            kingMove = -1;
                        }
                    }
                    end.setContent(start.getContent());
                    start.setContent(null);
                    if (currentPlayer) {
                        if(check(whiteKing[0], whiteKing[1])){
                            legality = false;
                        }
                    }
                    else{
                        if(check(blackKing[0], blackKing[1])){
                            legality = false;
                        }
                    }
                    if (legality){
                        possibleMoves.add(new Move(end.getContent(), start, end));
                    }
                    if (kingMove == 1){
                        whiteKing = start.getCoords();
                    }
                    else if (kingMove == -1){
                        blackKing = start.getCoords();
                    }
                    start.setContent(end.getContent());
                    end.setContent(target);
                }
            }
        }
        return possibleMoves;
    }

    private Piece move(Square start, Square end) {
        Piece taken = null;
        if (start.getContent() instanceof King){
            if(start.getContent().getColor()){
                whiteKing = end.getCoords();
                if (castlingRights.contains(board[0][0])){
                    castlingRights.remove(board[0][0]);
                }
                if (castlingRights.contains(board[7][0])){
                    castlingRights.remove(board[7][0]);
                }
            }
            else{
                blackKing = end.getCoords();
                if (castlingRights.contains(board[0][7])){
                    castlingRights.remove(board[0][7]);
                }
                if (castlingRights.contains(board[7][7])){
                    castlingRights.remove(board[7][7]);
                }
            }
        }
        else if(start.getContent() instanceof Rook){
            int x = start.getCoords()[0];
            int y = end.getCoords()[1];
            if (castlingRights.contains(board[x][y])){
                castlingRights.remove(board[x][y]);
            }
        }
        else if (start.getContent() instanceof Pawn && abs(start.getCoords()[1]-end.getCoords()[1]) == 2){
            int[] coords = start.getCoords();
            board[coords[0]][coords[1]+(end.getCoords()[1]-coords[1])/2].setGhostContent(new Pawn(start.getContent().getColor()));
        }
        else if(start.getContent() instanceof Pawn){
            if ((start.getContent().getColor() && end.getCoords()[1] == 7) || (!start.getContent().getColor() && end.getCoords()[1] == 0)){
                promotion = true;
            }
        }
        if(start.getContent() instanceof King && abs(start.getCoords()[0]-end.getCoords()[0])==2){
            if(end.getCoords()[0] == 2){
                board[3][end.getCoords()[1]].setContent(board[0][end.getCoords()[1]].getContent());
                board[0][end.getCoords()[1]].setContent(null);
                GridPane.setColumnIndex(board[3][end.getCoords()[1]].getContent(), GridPane.getColumnIndex(board[3][end.getCoords()[1]]));
                GridPane.setRowIndex(board[3][end.getCoords()[1]].getContent(), GridPane.getRowIndex(board[3][end.getCoords()[1]]));
                board[3][end.getCoords()[1]].getContent().toFront();
            }
            else if(end.getCoords()[0] == 6){
                board[5][end.getCoords()[1]].setContent(board[7][end.getCoords()[1]].getContent());
                board[7][end.getCoords()[1]].setContent(null);
                GridPane.setColumnIndex(board[5][end.getCoords()[1]].getContent(), GridPane.getColumnIndex(board[5][end.getCoords()[1]]));
                GridPane.setRowIndex(board[5][end.getCoords()[1]].getContent(), GridPane.getRowIndex(board[5][end.getCoords()[1]]));
                board[5][end.getCoords()[1]].getContent().toFront();
            }
        }
        if (end.getContent() != null) {
            getChildren().remove(end.getContent());
            taken = end.getContent();
        }
        end.setContent(start.getContent());
        start.setContent(null);
        GridPane.setColumnIndex(end.getContent(), GridPane.getColumnIndex(end));
        GridPane.setRowIndex(end.getContent(), GridPane.getRowIndex(end));
        end.getContent().toFront();
        if (promotion){
            fireEvent(new Promoting(end));
            promotionTaken = taken;
        }
        return taken;
    }
    private boolean check(int x, int y) {
        boolean result = false;
        for(int i = 0; i<8; i++) {
            for(int j = 0; j<8; j++) {
                if (board[i][j].getContent()!= null && board[i][j].getContent().validMove(board[i][j], board[x][y], board)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private boolean checkmate(boolean color) {
        boolean result = false;
        ArrayList<Move> possible = new ArrayList<>();
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                if (board[i][j].getContent()!= null && board[i][j].getContent().getColor() == color){
                    possible.addAll(possibleMoves(board[i][j]));
                }
            }
        }
        if (possible.isEmpty()){
            result = true;
        }
        return result;
    }

    private void deleteGhost(){
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                if(board[i][j].getGhostContent() != null && currentPlayer != board[i][j].getGhostContent().getColor()){
                    board[i][j].setGhostContent(null);
                }
            }
        }
    }
}
