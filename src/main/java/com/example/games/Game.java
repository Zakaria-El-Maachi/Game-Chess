package com.example.games;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game extends Application {

    private Text currentPlayer;
    Timer wtimer;
    Timer btimer;
    private FlowPane whitep;
    private FlowPane blackp;
    private int duration = 300;
    private int increment = 5;
    private ArrayList<Move> moves = new ArrayList<Move>();
    private boolean result;

    private static class Timer extends AnimationTimer{
        public Label timerLabel = new Label();
        private long start;
        private long elapsed;
        private int duration;
        private int increment;


        public Timer(int duration, int increment){
            timerLabel.setFont(new Font("Arial", 50));
            this.start = System.nanoTime();
            this.duration = duration;
            this.increment = increment;
            this.elapsed = 0;
        }
        @Override
        public void handle(long l) {
            elapsed += l-start;
            start = l;
            int sec = (int)(elapsed/1000000000);
            timerLabel.setText(String.format("%02d:%02d", (duration-sec)/60, (duration-sec)%60));
        }
        public void update(){
            this.start = System.nanoTime();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        HBox root = new HBox();
        root.setAlignment(Pos.CENTER);
        Board board = new Board(Color.SANDYBROWN, Color.WHITE, 60);
        VBox info = new VBox();
        info.setAlignment(Pos.CENTER);
        whitep = new FlowPane();
        blackp = new FlowPane();
        currentPlayer = new Text("White Turn");
        currentPlayer.setFont(new Font("Arial", 20));
        wtimer = new Timer(duration, increment);
        btimer = new Timer(duration, increment);
        info.getChildren().addAll(blackp, btimer.timerLabel, currentPlayer, wtimer.timerLabel, whitep);
        root.getChildren().addAll(board, info);
        Scene scene = new Scene(root);
        stage.setTitle("Chess Game !");
        stage.setScene(scene);
        stage.show();
        wtimer.start();
        stage.addEventHandler(Board.Winner.winner, event ->{
            Text win = new Text();
            if (event.color){
                win.setText("White Wins !");
            }
            else{
                win.setText("Black Wins !");
            }
            win.setFont(new Font("Arial", 50));
            win.toFront();
            root.getChildren().remove(info);
            root.getChildren().add(win);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event2 -> stage.close()));
            timeline.play();
        });
        stage.addEventHandler(Board.Turn.changeTurns, turn -> {
            if (turn.turn) {
                if (turn.taken != null) {
                    whitep.getChildren().add(turn.taken);
                }
                currentPlayer.setText("Black Turn");
                wtimer.stop();
                btimer.update();
                btimer.start();
            } else {
                if (turn.taken != null) {
                    blackp.getChildren().add(turn.taken);
                }
                currentPlayer.setText("White Turn");
                btimer.stop();
                wtimer.update();
                wtimer.start();
            }
        });
        stage.addEventHandler(Board.Promoting.promotion, event -> {
            VBox options = new VBox();
            options.setAlignment(Pos.CENTER);
            ToggleGroup list = new ToggleGroup();
            RadioButton btn1 = new RadioButton("Queen");
            btn1.setOnAction(selection ->{
                board.getChildren().remove(event.promotionSquare.getContent());
                event.promotionSquare.setContent(new Queen(event.promotionSquare.getContent().getColor()));
                GridPane.setColumnIndex(event.promotionSquare.getContent(), GridPane.getColumnIndex(event.promotionSquare));
                GridPane.setRowIndex(event.promotionSquare.getContent(), GridPane.getRowIndex(event.promotionSquare));
                board.getChildren().add(event.promotionSquare.getContent());
                board.promotion = false;
                board.endTurn(board.promotionTaken);
                root.getChildren().remove(options);
            });
            RadioButton btn2 = new RadioButton("Rook");
            btn2.setOnAction(selection ->{
                board.getChildren().remove(event.promotionSquare.getContent());
                event.promotionSquare.setContent(new Rook(event.promotionSquare.getContent().getColor()));
                GridPane.setColumnIndex(event.promotionSquare.getContent(), GridPane.getColumnIndex(event.promotionSquare));
                GridPane.setRowIndex(event.promotionSquare.getContent(), GridPane.getRowIndex(event.promotionSquare));
                board.getChildren().add(event.promotionSquare.getContent());
                board.promotion = false;
                board.endTurn(board.promotionTaken);
                root.getChildren().remove(options);
            });
            RadioButton btn3 = new RadioButton("Bishop");
            btn3.setOnAction(selection ->{
                board.getChildren().remove(event.promotionSquare.getContent());
                event.promotionSquare.setContent(new Bishop(event.promotionSquare.getContent().getColor()));
                GridPane.setColumnIndex(event.promotionSquare.getContent(), GridPane.getColumnIndex(event.promotionSquare));
                GridPane.setRowIndex(event.promotionSquare.getContent(), GridPane.getRowIndex(event.promotionSquare));
                board.getChildren().add(event.promotionSquare.getContent());
                board.promotion = false;
                board.endTurn(board.promotionTaken);
                root.getChildren().remove(options);
            });
            RadioButton btn4 = new RadioButton("Knight");
            btn4.setOnAction(selection ->{
                board.getChildren().remove(event.promotionSquare.getContent());
                event.promotionSquare.setContent(new Knight(event.promotionSquare.getContent().getColor()));
                GridPane.setColumnIndex(event.promotionSquare.getContent(), GridPane.getColumnIndex(event.promotionSquare));
                GridPane.setRowIndex(event.promotionSquare.getContent(), GridPane.getRowIndex(event.promotionSquare));
                board.getChildren().add(event.promotionSquare.getContent());
                board.promotion = false;
                board.endTurn(board.promotionTaken);
                root.getChildren().remove(options);
            });
            list.getToggles().addAll(btn1, btn2, btn3, btn4);
            options.getChildren().addAll(btn1, btn2, btn3, btn4);
            root.getChildren().add(options);
        });
    }

    public static void main(String[] args) {
        launch();
    }





}