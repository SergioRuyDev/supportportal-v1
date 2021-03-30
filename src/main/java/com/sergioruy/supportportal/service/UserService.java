package com.sergioruy.supportportal.service;

import com.sergioruy.supportportal.domain.User;
import com.sergioruy.supportportal.exception.domain.EmailExistException;
import com.sergioruy.supportportal.exception.domain.UserNotFoundException;
import com.sergioruy.supportportal.exception.domain.UsernameExistException;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}
