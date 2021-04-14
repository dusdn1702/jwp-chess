package chess.domain.board;

import static chess.domain.board.Board.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import chess.domain.piece.Direction;

public final class Point {
    private static final int ASCII_CODE_GAP = 97;
    private static final int RANK_GAP = 8;
    private static final int RANK_INDEX = 1;
    private static final int LETTER_INDEX = 0;
    private static final char MINIMUM_LETTER = 'a';
    private static final char MAXIMUM_LETTER = 'h';
    private static final int MINIMUM_RANK = 1;
    private static final int MAXIMUM_RANK = 8;
    private static final int MINIMUM_INDEX = 0;
    private static final int MAXIMUM_INDEX = 7;

    private static final List<Point> points;

    static {
        points = IntStream.rangeClosed(MINIMUM_LETTER, MAXIMUM_LETTER)
            .mapToObj(i -> (char)i)
            .flatMap(row ->
                IntStream.rangeClosed(MINIMUM_RANK, MAXIMUM_RANK)
                    .mapToObj(column -> new Point(row, column))
            )
            .collect(Collectors.toList());
    }

    private final PositionValue row;
    private final PositionValue column;

    private Point(char letter, int rank) {
        row = new PositionValue(convertRankToIndex(rank));
        column = new PositionValue(convertLetterToIndex(letter));
    }

    public static Point valueOf(int row, int column) {
        return points.get(makeIndex(row, column));
    }

    public static Point of(String point) {
        return of(point.charAt(LETTER_INDEX), Character.getNumericValue(point.charAt(RANK_INDEX)));
    }

    private static Point of(char letter, int rank) {
        return points.get(makeIndex(convertRankToIndex(rank), convertLetterToIndex(letter)));
    }

    private static int makeIndex(int row, int column) {
        checkAvailableIndex(row);
        checkAvailableIndex(column);
        return BOARD_SIZE * column + (MAXIMUM_INDEX - row);
    }

    private static void checkAvailableIndex(int value) {
        if (value < MINIMUM_INDEX || value > MAXIMUM_INDEX) {
            throw new IllegalArgumentException("옳지 않은 좌표입니다.");
        }
    }

    private static int convertLetterToIndex(char letter) {
        return letter - ASCII_CODE_GAP;
    }

    private static int convertRankToIndex(int rank) {
        return RANK_GAP - rank;
    }

    public int calculateDistance(Point point) {
        return makeDoubleNumber(this.column.subtract(point.column)) + makeDoubleNumber(this.row.subtract(point.row));
    }

    private int makeDoubleNumber(int subtract) {
        return (int)Math.pow(subtract, 2);
    }

    public Direction makeDirection(Point point) {
        return Direction.findDirection(this, point);
    }

    public Point createNextPoint(Direction direction) {
        return valueOf(direction.addCurrentRow(this.row), direction.addCurrentColumn(this.column));
    }

    public boolean isSameRow(PositionValue row) {
        return this.row.equals(row);
    }

    public boolean isSameColumn(PositionValue column) {
        return this.column.equals(column);
    }

    public int subtractRow(Point point) {
        return this.row.subtract(point.row);
    }

    public int subtractColumn(Point point) {
        return this.column.subtract(point.column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Point point = (Point)o;
        return column == point.column &&
            row == point.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }
}
