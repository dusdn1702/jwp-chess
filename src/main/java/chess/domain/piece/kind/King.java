package chess.domain.piece.kind;

import chess.domain.board.Point;
import chess.domain.piece.Color;

public final class King extends Piece {
    public static final String KING_NAME = "k";
    private static final int KING_SCORE = 0;

    public King(Color color) {
        super(KING_NAME, color);
    }

    @Override
    public void checkCorrectDistance(Point sourcePoint, Point targetPoint, Piece target) {
        int distance = sourcePoint.calculateDistance(targetPoint);

        if (distance != MOVE_STRAIGHT_ONE_SQUARE && distance != MOVE_DIAGONAL_ONE_SQUARE) {
            throw new IllegalArgumentException(IMPOSSIBLE_ROUTE_ERROR_MESSAGE);
        }
    }

    @Override
    public double score() {
        return KING_SCORE;
    }

    @Override
    public boolean isKing() {
        return true;
    }
}
