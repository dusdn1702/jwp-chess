package chess.domain.piece.kind;

import static chess.domain.piece.Direction.*;

import java.util.Arrays;
import java.util.List;

import chess.domain.piece.Color;
import chess.domain.piece.Direction;

public final class Bishop extends Piece {
    public static final String BISHOP_NAME = "b";
    public static final int BISHOP_SCORE = 3;

    private static final List<Direction> bishopDirection = Arrays.asList(SOUTH_EAST, SOUTH_WEST, NORTH_EAST,
        NORTH_WEST);

    public Bishop(Color color) {
        super(BISHOP_NAME, color);
    }

    @Override
    public void checkCorrectDirection(Direction direction) {
        if (!bishopDirection.contains(direction)) {
            throw new IllegalArgumentException("이동할 수 없는 방향입니다.");
        }
    }

    @Override
    public double score() {
        return BISHOP_SCORE;
    }
}
