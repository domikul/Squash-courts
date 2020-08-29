package pl.euvic.squash.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.euvic.squash.exception.WrongRequestException;
import pl.euvic.squash.model.response.UserRestModel;
import pl.euvic.squash.model.service.UserService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<UserRestModel>> getAllUsers(@RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok(userService.getAll(token));
    }

    @GetMapping(value = "/account")
    public ResponseEntity<UserRestModel> getOwnData(@RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok(userService.getById(token));
    }


    @PostMapping(value = "/register")
    public ResponseEntity<UserRestModel> registerUser(@RequestBody UserRestModel userRestModel)
            throws WrongRequestException {

        return ResponseEntity.ok(userService.registerUser(userRestModel));
    }


    @PutMapping(value = "/{idUser}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRestModel> updateUser(@RequestBody UserRestModel userRestModel,
                                                    @PathVariable Integer idUser,
                                                    @RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok(userService.update(userRestModel, idUser, token));
    }


    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{idUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteUser(@PathVariable Integer idUser,
                                              @RequestHeader(value = "Authorization") String token) {

        userService.delete(idUser, token);
        return new ResponseEntity<>(idUser, HttpStatus.OK);
    }

}
