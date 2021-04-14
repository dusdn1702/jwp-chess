package chess.service;

import static chess.domain.Status.*;
import static chess.domain.piece.Color.*;

import chess.dao.ChessDao;
import chess.dao.SQLConnection;
import chess.domain.ChessGame;
import chess.domain.board.Point;
import chess.domain.piece.Color;
import chess.domain.piece.kind.Piece;
import chess.dto.BoardDto;
import chess.dto.RequestDto;
import chess.dto.UserDto;

public class ChessService {
    private final ChessDao chessDAO;

    public ChessService() {
        chessDAO = new ChessDao(new SQLConnection());
    }

    public void restartChess(String userId) {
        chessDAO.deleteBoard(userId);
    }

    public ChessGame matchGame(UserDto userDto) {
        String userId = chessDAO.findUserIdByUser(userDto);
        BoardDto boardDto = chessDAO.findBoard(userId);
        Color color = chessDAO.findBoardNextTurn(userId);
        return makeChessGame(boardDto, color);
    }

    public String matchBoardImageSource(String userBody, String requestBody) {
        UserDto userDto = chessDAO.findByUserId(userBody);
        return matchPiece(userDto, requestBody).getName();
    }

    public String matchPieceName(RequestDto requestDto) {
        UserDto userDto = chessDAO.findByUserId(requestDto.getSecondInfo());
        String point = requestDto.getFirstInfo();
        return matchPiece(userDto, point).getName();
    }

    private ChessGame makeChessGame(BoardDto boardDto, Color color) {
        if (boardDto == null) {
            return new ChessGame();
        }
        return new ChessGame(boardDto.getBoard(), color);
    }

    public void addBoard(String userId, String boardInfo) {
        chessDAO.addBoard(userId, boardInfo, makeNextColor(userId));
    }

    public String makeNextColor(String userId) {
        UserDto userDto = chessDAO.findByUserId(userId);
        return matchGame(userDto).color();
    }

    public String makeCurrentColor(RequestDto requestDto) {
        UserDto userDto = chessDAO.findByUserId(requestDto.getSecondInfo());
        String point = requestDto.getFirstInfo();
        // return matchPiece(userDto, point)
        if (matchPiece(userDto, point).isSameTeam(BLACK)) {
            return BLACK.name();
        }
        return WHITE.name();
    }

    private Piece matchPiece(UserDto userDto, String point) {
        return matchGame(userDto).getBoard().get(Point.of(point));
    }

    public int move(RequestDto requestDto) {
        String source = requestDto.getFirstInfo().substring(0, 2);
        String target = requestDto.getFirstInfo().substring(2, 4);
        UserDto userDto = chessDAO.findByUserId(requestDto.getSecondInfo());
        try {
            ChessGame playerGame = matchGame(userDto);
            playerGame.playTurn(Point.of(source), Point.of(target));
            if (playerGame.isEnd()) {
                chessDAO.saveBoard(chessDAO.findUserIdByUser(userDto), playerGame, playerGame.nextTurn().name());
                return RESET_CONTENT.code();
            }
            chessDAO.saveBoard(chessDAO.findUserIdByUser(userDto), playerGame, playerGame.color());
            return OK.code();
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            return NO_CONTENT.code();
        }
    }

    public double score(RequestDto requestDto) {
        UserDto userDto = chessDAO.findByUserId(requestDto.getSecondInfo());
        String colorName = requestDto.getFirstInfo();
        return matchGame(userDto).calculateScore(Color.valueOf(colorName)).getScore();
    }

    public void addUser(String requestName, String requestPassword) {
        UserDto userDto = new UserDto(requestName, requestPassword);
        chessDAO.addUser(userDto);
    }

    public UserDto requestLoginUser(String requestName, String requestPassword) {
        return new UserDto(requestName, requestPassword);
    }

    public String makeUserID(String body) {
        return chessDAO.findUserIdByUserName(body);
    }

    public UserDto findUserWithId(String userId) {
        return chessDAO.findByUserId(userId);
    }
}
