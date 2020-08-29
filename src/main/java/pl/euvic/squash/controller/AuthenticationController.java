package pl.euvic.squash.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.euvic.squash.exception.NotAuthenticatedException;
import pl.euvic.squash.exception.WrongRequestException;
import pl.euvic.squash.model.auth.Credentials;
import pl.euvic.squash.model.auth.Token;
import pl.euvic.squash.model.response.UserRestModel;
import pl.euvic.squash.model.service.UserService;
import pl.euvic.squash.security.helper.JwtHelper;

import static pl.euvic.squash.exception.ExceptionMessage.WRONG_CREDENTIALS;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtHelper jwtHelper;

    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService, JwtHelper jwtHelper) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping(path = "/login")
    public Token login(@RequestBody Credentials credentials) throws NotAuthenticatedException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
            return jwtHelper.getToken(credentials);

        } catch (AuthenticationException e) {
            System.out.println(WRONG_CREDENTIALS.getMessage());
            throw new NotAuthenticatedException(WRONG_CREDENTIALS.getMessage());
        }
    }

    @PostMapping(path = "/registration")
    public ResponseEntity<UserRestModel> registerUser(@RequestBody UserRestModel userRestModel, @RequestHeader(value = "Authorization") String token)
            throws WrongRequestException {

        return ResponseEntity.ok(userService.registerByAdmin(userRestModel, token));
    }

}
