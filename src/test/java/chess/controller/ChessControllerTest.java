package chess.controller;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import chess.dto.request.MoveRequestDto;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application.properties")
@Sql("classpath:initSetting.sql")
public class ChessControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("메인 화면 연결 확인")
    @Test
    void index() {
        RestAssured.given().log().all()
            .when().get("/")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.TEXT_HTML_VALUE)
            .body("html.head.title", equalTo("체스 방 입장"));
    }

    @DisplayName("방들 가져오는거 확인")
    @Test
    void getRooms() {
        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/rooms")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("roomNames", contains("hi"));
    }

    @DisplayName("기물 가져오는거 확인")
    @Test
    void postPieces() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/pieces/1")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("piecesInBoard.size()", is(1));
    }

    @DisplayName("보드 갱신 확인")
    @Test
    void putBoard() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/room?title=hello")
            .then().log().all();
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/pieces/2")
            .then().log().all();

        MoveRequestDto moveRequestDto = new MoveRequestDto("a2", "a4");
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(moveRequestDto)
            .when().put("/api/board/2")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("piecesInBoard.size()", is(64));
    }

    @DisplayName("점수 확인")
    @Test
    void getScore() {
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/score/1/WHITE")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("score.toString()", equalTo("1.0"));
    }
}
