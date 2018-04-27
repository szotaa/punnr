package pl.szotaa.punnr.user.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.szotaa.punnr.user.exception.UserAlreadyExistsException;

@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(){
        return "redirect:/user/register?exists";
    }
}
