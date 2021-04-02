package com.sergioruy.supportportal.resource;

import com.sergioruy.supportportal.domain.HttpResponse;
import com.sergioruy.supportportal.domain.User;
import com.sergioruy.supportportal.domain.UserPrincipal;
import com.sergioruy.supportportal.exception.domain.*;
import com.sergioruy.supportportal.service.UserService;
import com.sergioruy.supportportal.utility.JWTTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

import java.io.IOException;
import java.util.List;

import static com.sergioruy.supportportal.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping(path = {"/", "/user"})
public class UserResource extends ExceptionHandling {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;


    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException,
            EmailExistException, MessagingException {
       User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
       return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addNewUser(@RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("username") String username,
                                           @RequestParam("email") String email,
                                           @RequestParam("role") String role,
                                           @RequestParam("isActive") String isActive,
                                           @RequestParam("isNonLocked") String isNonLocked,
                                           @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
        User newUser = userService.addNewUser(firstName, lastName, username, email, role, Boolean.parseBoolean(isNonLocked)
        , Boolean.parseBoolean(isActive), profileImage);
        return new ResponseEntity<>(newUser, OK);

    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestParam("currentUsername") String currentUsername,
                                           @RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("username") String username,
                                           @RequestParam("email") String email,
                                           @RequestParam("role") String role,
                                           @RequestParam("isActive") String isActive,
                                           @RequestParam("isNonLocked") String isNonLocked,
                                           @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
            throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
        User updatedUser = userService.updateUser(currentUsername, firstName, lastName, username, email, role,
                Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
        return new ResponseEntity<>(updatedUser, OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        User user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/resetPassword/{email}")
    public ResponseEntity<List<User>> resetPassword(@PathVariable("email") String email)
            throws MessagingException, EmailNotFoundException {
        userService.resetPassword(email);
        return response(OK, "Email sent to: " + email);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus ok, String s) {
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, OK);
    }




    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
