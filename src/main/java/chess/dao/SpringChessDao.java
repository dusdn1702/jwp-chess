package chess.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import chess.domain.ChessGame;
import chess.domain.board.Point;
import chess.domain.piece.Color;
import chess.domain.piece.PieceType;
import chess.domain.piece.kind.Piece;
import chess.dto.BoardDto;
import chess.dto.UserDto;

@Repository
public class SpringChessDao {
    private final JdbcTemplate jdbcTemplate;

    public SpringChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserDto findByUserId(String userId) {
        String sql = "SELECT * FROM user WHERE user_id = ?";
        RowMapper<UserDto> rowMapper = (resultSet, rowNum) -> {
            UserDto userDto = new UserDto(
                resultSet.getString("user_name"),
                resultSet.getString("user_password")
            );
            return userDto;
        };
        return jdbcTemplate.queryForObject(sql, rowMapper, userId);
    }

    public int deleteBoard(String userId) {
        String sql = "DELETE FROM board WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    public int addUser(UserDto userDto) {
        String sql = "INSERT INTO user (user_name, user_password) VALUES (?, ?)";
        return jdbcTemplate.update(sql, userDto.getName(), userDto.getPwd());
    }

    public String findUserIdByUserName(String userName) {
        String sql = "SELECT user_id FROM user WHERE user_name = ?";
        return jdbcTemplate.queryForObject(sql, String.class, userName);
    }

    public int addBoard(String userId, String boardInfo, String makeNextColor) {
        String sql = "INSERT INTO board (user_id, board_info, color) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, userId, boardInfo, makeNextColor);
    }

    public String findUserIdByUser(UserDto userDto) {
        String sql = "SELECT user_id FROM user WHERE user_name = ? AND user_password = ?";
        return jdbcTemplate.queryForObject(sql, String.class, userDto.getName(), userDto.getPwd());
    }

    public BoardDto findBoard(String userId) {
        String sql = "SELECT * FROM board WHERE user_id = ?";
        RowMapper<BoardDto> rowMapper = (resultSet, rowNum) -> {
            Map<Point, Piece> chessBoard = new HashMap<>();
            String info = resultSet.getString("board_info");
            IntStream.rangeClosed(0, 7)
                .forEach(i -> IntStream.rangeClosed(0, 7).forEachOrdered(j -> chessBoard.put(
                    Point.valueOf(i, j), PieceType.findPiece(String.valueOf(info.charAt(i * 8 + j)))
                    ))
                );
            return new BoardDto(chessBoard);
        };
        return jdbcTemplate.queryForObject(sql, rowMapper, userId);
    }

    public Color findBoardNextTurn(String userId) {
        String sql = "SELECT color FROM board WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> Color.valueOf(rs.getString("color")), userId);
    }

    public void saveBoard(String userId, ChessGame chessGame, String color) {
        StringBuilder boardInfo = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardInfo.append(chessGame.getBoard().get(Point.valueOf(i, j)).getName());
            }
        }
        addBoard(userId, boardInfo.toString(), color);
    }
}
