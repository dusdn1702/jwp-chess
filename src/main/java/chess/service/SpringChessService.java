package chess.service;

import static chess.domain.Status.*;
import static chess.domain.piece.Color.*;

import org.springframework.stereotype.Service;

import chess.dao.SpringChessDao;
import chess.domain.ChessGame;
import chess.domain.board.Point;
import chess.domain.piece.Color;
import chess.domain.piece.kind.Piece;
import chess.dto.BoardDto;
import chess.dto.RequestDto;
import chess.dto.UserDto;

@Service
public class SpringChessService {
    private final SpringChessDao springChessDao;

    public SpringChessService(SpringChessDao springChessDao) {
        this.springChessDao = springChessDao;
    }

    public UserDto findUserWithId(String userId) {
        return springChessDao.findByUserId(userId);
    }

    public void restartChess(String userId) {
        springChessDao.deleteBoard(userId);
    }

    public UserDto requestLoginUser(String name, String password) {
        return new UserDto(name, password);
    }

    public void addUser(String name, String password) {
        UserDto userDto = new UserDto(name, password);
        springChessDao.addUser(userDto);
    }

    public String makeUserID(String userName) {
        return springChessDao.findUserIdByUserName(userName);
    }

    public void addBoard(String userId, String boardInfo) {
        springChessDao.addBoard(userId, boardInfo, makeNextColor(userId));
    }

    public String makeNextColor(String userId) {
        UserDto userDto = springChessDao.findByUserId(userId);
        return matchGame(userDto).color();
    }

    public ChessGame matchGame(UserDto userDto) {
        String userId = springChessDao.findUserIdByUser(userDto);
        BoardDto boardDto = springChessDao.findBoard(userId);
        Color color = springChessDao.findBoardNextTurn(userId);
        return makeChessGame(boardDto, color);
    }

    private ChessGame makeChessGame(BoardDto boardDto, Color color) {
        if (boardDto == null) {
            return new ChessGame();
        }
        return new ChessGame(boardDto.getBoard(), color);
    }

    public String matchPieceName(RequestDto requestDto) {
        UserDto userDto = springChessDao.findByUserId(requestDto.getSecondInfo());
        String point = requestDto.getFirstInfo();
        return matchPiece(userDto, point).getName();
    }

    private Piece matchPiece(UserDto userDto, String point) {
        return matchGame(userDto).getBoard().get(Point.of(point));
    }

    public int move(RequestDto requestDto) {
        String source = requestDto.getFirstInfo().substring(0, 2);
        String target = requestDto.getFirstInfo().substring(2, 4);
        UserDto userDto = springChessDao.findByUserId(requestDto.getSecondInfo());
        try {
            ChessGame playerGame = matchGame(userDto);
            playerGame.playTurn(Point.of(source), Point.of(target));
            if (playerGame.isEnd()) {
                springChessDao.saveBoard(springChessDao.findUserIdByUser(userDto), playerGame, playerGame.nextTurn().name());
                return RESET_CONTENT.code();
            }
            springChessDao.saveBoard(springChessDao.findUserIdByUser(userDto), playerGame, playerGame.color());
            return OK.code();
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            return NO_CONTENT.code();
        }
    }

    public String makeCurrentColor(RequestDto requestDto) {
        UserDto userDto = springChessDao.findByUserId(requestDto.getSecondInfo());
        String point = requestDto.getFirstInfo();
        if (matchPiece(userDto, point).isSameTeam(BLACK)) {
            return BLACK.name();
        }
        return WHITE.name();    }

    public String matchBoardImageSource(String secondInfo, String firstInfo) {
        UserDto userDto = springChessDao.findByUserId(secondInfo);
        return matchPiece(userDto, firstInfo).getName();
    }

    public double score(RequestDto requestDto) {
        UserDto userDto = springChessDao.findByUserId(requestDto.getSecondInfo());
        String colorName = requestDto.getFirstInfo();
        return matchGame(userDto).calculateScore(Color.valueOf(colorName)).getScore();
    }
}
