package pl.szotaa.punnr.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.szotaa.punnr.user.domain.User;
import pl.szotaa.punnr.user.service.UserService;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public ModelAndView getRegistrationForm(){
        return new ModelAndView("register", "user", new User());
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute User user){
        userService.register(user);
        return "redirect:/";
    }
}
