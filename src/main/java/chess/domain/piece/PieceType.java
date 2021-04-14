package chess.domain.piece;

import static chess.domain.piece.Color.*;
import static chess.domain.piece.PieceType.Constants.*;
import static chess.domain.piece.kind.Pawn.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import chess.domain.piece.kind.Bishop;
import chess.domain.piece.kind.Empty;
import chess.domain.piece.kind.King;
import chess.domain.piece.kind.Knight;
import chess.domain.piece.kind.Pawn;
import chess.domain.piece.kind.Piece;
import chess.domain.piece.kind.Queen;
import chess.domain.piece.kind.Rook;

public enum PieceType {
    ROOK(INITIAL_COLUMN_OF_ROOK, Rook.ROOK_NAME) {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new Rook(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new Rook(WHITE);
            }
            return new Empty(NOTHING);
        }

        @Override
        public Piece create(int row, int column) {
            if (row == INITIAL_BLACK_ROW_WITHOUT_PAWN) {
                return new Rook(BLACK);
            }
            if (row == INITIAL_WHITE_ROW_WITHOUT_PAWN) {
                return new Rook(WHITE);
            }
            return createDefaultPieces(row);
        }
    },

    KNIGHT(INITIAL_COLUMN_OF_KNIGHT, Knight.KNIGHT_NAME) {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new Knight(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new Knight(WHITE);
            }
            return new Empty(NOTHING);
        }

        @Override
        public Piece create(int row, int column) {
            if (row == INITIAL_BLACK_ROW_WITHOUT_PAWN) {
                return new Knight(BLACK);
            }
            if (row == INITIAL_WHITE_ROW_WITHOUT_PAWN) {
                return new Knight(WHITE);
            }
            return createDefaultPieces(row);
        }
    },
    BISHOP(INITIAL_COLUMN_OF_BISHOP, Bishop.BISHOP_NAME) {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new Bishop(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new Bishop(WHITE);
            }
            return new Empty(NOTHING);
        }

        @Override
        public Piece create(int row, int column) {
            if (row == INITIAL_BLACK_ROW_WITHOUT_PAWN) {
                return new Bishop(BLACK);
            }
            if (row == INITIAL_WHITE_ROW_WITHOUT_PAWN) {
                return new Bishop(WHITE);
            }
            return createDefaultPieces(row);
        }
    },
    QUEEN(INITIAL_COLUMN_OF_QUEEN, Queen.QUEEN_NAME) {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new Queen(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new Queen(WHITE);
            }
            return new Empty(NOTHING);
        }

        @Override
        public Piece create(int row, int column) {
            if (row == INITIAL_BLACK_ROW_WITHOUT_PAWN) {
                return new Queen(BLACK);
            }
            if (row == INITIAL_WHITE_ROW_WITHOUT_PAWN) {
                return new Queen(WHITE);
            }
            return createDefaultPieces(row);
        }
    },
    KING(INITIAL_COLUMN_OF_KING, King.KING_NAME) {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new King(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new King(WHITE);
            }
            return new Empty(NOTHING);
        }

        @Override
        public Piece create(int row, int column) {
            if (row == INITIAL_BLACK_ROW_WITHOUT_PAWN) {
                return new King(BLACK);
            }
            if (row == INITIAL_WHITE_ROW_WITHOUT_PAWN) {
                return new King(WHITE);
            }
            return createDefaultPieces(row);
        }
    },
    PAWN(new ArrayList<>(), PAWN_NAME) {
        @Override
        public Piece of(String name) {
            if (name.equals(this.pieceName.toUpperCase())) {
                return new Pawn(BLACK);
            }
            if (name.equals(this.pieceName)) {
                return new Pawn(WHITE);
            }
            return new Empty(NOTHING);
        }

        @Override
        public Piece create(int row, int column) {
            return createDefaultPieces(row);
        }
    };

    protected final String pieceName;
    private final List<Integer> column;

    PieceType(List<Integer> column, String pieceName) {
        this.column = column;
        this.pieceName = pieceName;
    }

    private static Piece createDefaultPieces(int row) {
        if (row == INITIAL_BLACK_PAWN_ROW) {
            return new Pawn(BLACK);
        }
        if (row == INITIAL_WHITE_PAWN_ROW) {
            return new Pawn(WHITE);
        }
        return new Empty(NOTHING);
    }

    public static Piece findPiece(int row, int column) {
        return matchColumn(column).create(row, column);
    }

    public static Piece findPiece(String name) {
        return Arrays.stream(PieceType.values())
            .filter(piece -> piece.pieceName.equals(name) || piece.pieceName.equals(name.toLowerCase()))
            .map(pieceType -> pieceType.of(name))
            .findAny()
            .orElseGet(() -> new Empty(NOTHING));
    }

    private static PieceType matchColumn(int column) {
        return Arrays.stream(PieceType.values())
            .filter(piece -> piece.column.contains(column))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    public abstract Piece of(String name);

    public abstract Piece create(int row, int column);

    static final class Constants {
        static final List<Integer> INITIAL_COLUMN_OF_ROOK = Arrays.asList(0, 7);
        static final List<Integer> INITIAL_COLUMN_OF_KNIGHT = Arrays.asList(1, 6);
        static final List<Integer> INITIAL_COLUMN_OF_BISHOP = Arrays.asList(2, 5);
        static final List<Integer> INITIAL_COLUMN_OF_QUEEN = Collections.singletonList(3);
        static final List<Integer> INITIAL_COLUMN_OF_KING = Collections.singletonList(4);
        static final int INITIAL_WHITE_ROW_WITHOUT_PAWN = 7;
        static final int INITIAL_BLACK_ROW_WITHOUT_PAWN = 0;
    }
}
