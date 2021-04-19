package chess;

import chess.controller.SparkChessController;

import static spark.Spark.get;

public class SparkChessApplication {
    public static void main(String[] args) {
        SparkChessController sparkChessController = new SparkChessController();
        sparkChessController.run();
    }
}
