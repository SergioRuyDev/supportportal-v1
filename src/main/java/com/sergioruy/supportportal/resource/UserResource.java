package com.sergioruy.supportportal.resource;

import com.sergioruy.supportportal.exception.domain.EmailExistException;
import com.sergioruy.supportportal.exception.domain.ExceptionHandling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserResource extends ExceptionHandling {

    @GetMapping("/home")
    public String showUser() throws EmailExistException{
        //return "application works";
        throw new EmailExistException("This email address is already taken.");
    }
}
