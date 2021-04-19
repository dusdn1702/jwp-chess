package chess.controller;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import chess.dto.RequestDto;
import chess.dto.UserDto;
import chess.service.SparkChessService;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class SparkChessController {
    private static final Gson GSON = new Gson();

    private final SparkChessService sparkChessService;

    public SparkChessController() {
        sparkChessService = new SparkChessService();
    }

    public void run() {
        staticFiles.location("/templates");
        port(8081);

        get("/", (req, res) -> render(new HashMap<>(), "start.hbs"));
        post("/chess", (req, res) -> startWithUser(req.body()));
        post("/restart", (req, res) -> restart(req));

        post("/user", (req, res) -> login(req));
        post("/signup", (req, res) -> signup(req));
        get("/adduser", (req, res) -> render(new HashMap<>(), "signup.hbs"));
        post("/userId", (req, res) -> sparkChessService.makeUserID(req.body()));

        post("/board", (req, res) -> makeBoard(req));
        post("/save", (req, res) -> exit(req));
        put("/piece", (req, res) -> GSON.toJson(sparkChessService.matchPieceName(GSON.fromJson(req.body(), RequestDto.class))));
        put("/move", (req, res) -> sparkChessService.move(GSON.fromJson(req.body(), RequestDto.class)));
        post("/color", (req, res) -> sparkChessService.makeCurrentColor(GSON.fromJson(req.body(), RequestDto.class)));
        post("/turn", (req, res) -> sparkChessService.makeNextColor(req.body()));
        post("/score", (req, res) -> sparkChessService.score(GSON.fromJson(req.body(), RequestDto.class)));
    }

    private String startWithUser(String userId) {
        Map<String, Object> model = new HashMap<>();
        UserDto userWithId = sparkChessService.findUserWithId(userId);
        model.put("user", userWithId);
        return render(model, "chess.hbs");
    }

    private String restart(Request req) {
        sparkChessService.restartChess(req.body());
        return render(new HashMap<>(), "chess.hbs");
    }

    private String login(Request req) {
        UserDto loginUser = sparkChessService.requestLoginUser(req.queryParams("name"), req.queryParams("password"));
        Map<String, Object> model = new HashMap<>();
        if (loginUser == null) {
            return render(model, "start.hbs");
        }
        model.put("user", loginUser);
        return render(model, "chess.hbs");
    }

    private String signup(Request req) {
        sparkChessService.addUser(req.queryParams("name"), req.queryParams("password"));
        return render(new HashMap<>(), "start.hbs");
    }

    private String exit(Request req) {
        RequestDto requestDto = GSON.fromJson(req.body(), RequestDto.class);
        sparkChessService.addBoard(requestDto.getSecondInfo(), requestDto.getFirstInfo());
        return render(new HashMap<>(), "start.hbs");
    }

    private String makeBoard(Request req) {
        RequestDto requestDto = GSON.fromJson(req.body(), RequestDto.class);
        return GSON.toJson(sparkChessService.matchBoardImageSource(requestDto.getSecondInfo(), requestDto.getFirstInfo()));
    }

    private String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
