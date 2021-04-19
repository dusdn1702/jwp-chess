package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import chess.dto.RequestDto;
import chess.dto.UserDto;
import chess.service.SpringChessService;

@RestController
@RequestMapping("api/")
public class SpringChessController {

    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping(path = "/chess",  produces = "application/json")
    public ModelAndView getChess(ModelAndView model, @RequestParam("userId") String userId) {
        UserDto userWithId = springChessService.findUserWithId(userId);
        model.addObject("user", userWithId);
        model.setViewName("chess.hbs");
        return model;
    }

    @PostMapping(path = "/restart")
    public ModelAndView restart(ModelAndView model, @RequestParam String userId){
        springChessService.restartChess(userId);
        model.setViewName("chess");
        return model;
    }

    @PostMapping(path = "/user")
    private ModelAndView postUser(ModelAndView model, @RequestParam("name") String name, @RequestParam("password") String password) {
        UserDto loginUser = springChessService.requestLoginUser(name, password);
        if (loginUser == null) {
            model.setViewName("start");
            return model;
        }
        model.addObject("user", loginUser);
        model.setViewName("chess");
        return model;
    }

    @GetMapping(path = "/signup")
    private ModelAndView signup(ModelAndView model, @RequestParam("name") String name, @RequestParam("password") String password) {
        springChessService.addUser(name, password);
        model.setViewName("start");
        return model;
    }

    @PostMapping(path = "/userId")
    public String postUserId(@RequestParam("userName") String userName) {
        return springChessService.makeUserID(userName);
    }

    @PostMapping(path = "/save")
    private ModelAndView exit(ModelAndView model, @RequestBody RequestDto requestDto) {
        springChessService.addBoard(requestDto.getSecondInfo(), requestDto.getFirstInfo());
        model.setViewName("start");
        return model;
    }

    @PutMapping(path = "/piece")
    private String putPiece(@RequestBody RequestDto requestDto) {
        return springChessService.matchPieceName(requestDto);
    }

    @PutMapping(path = "/move")
    private int putMove(@RequestBody RequestDto requestDto) {
        return springChessService.move(requestDto);
    }

    @PostMapping(path = "/color")
    private String postColor(@RequestBody RequestDto requestDto) {
        return springChessService.makeCurrentColor(requestDto);
    }

    @PostMapping(path = "/turn")
    private String postTurn(@RequestParam("userId") String userId) {
        return springChessService.makeNextColor(userId);
    }

    @PostMapping(path = "/score")
    private double postScore(@RequestBody RequestDto requestDto) {
        return springChessService.score(requestDto);
    }

    @PostMapping(path = "/board")
    private String postBoard(@RequestBody RequestDto requestDto) {
        return springChessService.matchBoardImageSource(requestDto.getSecondInfo(), requestDto.getFirstInfo());
    }
}
