package pl.szotaa.punnr.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.szotaa.punnr.game.service.GameRoomService;

import java.security.Principal;

@Controller
@RequestMapping("/room")
@RequiredArgsConstructor
public class GameRoomController {

    private final GameRoomService gameRoomService;

    @GetMapping("/new")
    public ModelAndView startNewGame(){
        String gameId = gameRoomService.create();
        return new ModelAndView("redirect:/room/" + gameId);
    }

    @GetMapping("/{gameId}")
    public ModelAndView joinGame(@PathVariable String gameId, Principal principal){
        gameRoomService.join(gameId, principal.getName());
        return new ModelAndView("room", "gameId", gameId);
    }
}
