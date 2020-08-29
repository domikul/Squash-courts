package pl.euvic.squash.model.service;

import org.elasticsearch.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.euvic.squash.exception.NotAuthenticatedException;
import pl.euvic.squash.exception.WrongRequestException;
import pl.euvic.squash.model.entity.Reservation;
import pl.euvic.squash.model.entity.User;
import pl.euvic.squash.model.enumeration.RoleEnum;
import pl.euvic.squash.model.repository.RoleRepository;
import pl.euvic.squash.model.repository.UserRepository;
import pl.euvic.squash.model.response.UserRestModel;
import pl.euvic.squash.security.helper.JwtHelper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.euvic.squash.exception.ExceptionMessage.*;
import static pl.euvic.squash.model.enumeration.RoleEnum.*;

@Service
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtHelper jwtHelper;
    private final ConnectionDeleter connectionDeleter;

    public UserService(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtHelper jwtHelper, ConnectionDeleter connectionDeleter) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtHelper = jwtHelper;
        this.connectionDeleter = connectionDeleter;
    }

    public Boolean isAdminOrWorker(User user) {
        return (user.getRole().getRoleName().equals(ADMIN) || user.getRole().getRoleName().equals(WORKER));
    }


    public UserRestModel registerUser(UserRestModel userRestModel) {

        User user = new User();

        Optional<User> emailOptional = userRepository.findByEmail(userRestModel.getEmail());
        Optional<User> phoneOptional = userRepository.findByPhoneNumber(userRestModel.getPhoneNumber());

        if (String.valueOf(userRestModel.getPhoneNumber()).length() != 9)
            throw new WrongRequestException(INCORRECT_PHONE_NUMBER.getMessage());
        if (emailOptional.isPresent())
            throw new WrongRequestException(WRONG_EMAIL.getMessage());
        else if (phoneOptional.isPresent())
            throw new WrongRequestException(WRONG_PHONE_NUMBER.getMessage());
        else {
            user.setFirstName(userRestModel.getFirstName());
            user.setLastName(userRestModel.getLastName());
            user.setEmail(userRestModel.getEmail());
            user.setPhoneNumber(userRestModel.getPhoneNumber());
            user.setPassword(bCryptPasswordEncoder.encode(userRestModel.getPassword()));
            user.setDeleted(false);
            user.setRole(roleRepository.findById(RoleEnum.valueOf(USER.toString()).ordinal() + 1).orElseThrow(() ->
                    new ResourceNotFoundException(ROLE_NOT_FOUND.getMessage())));

            userRepository.save(user);

        }

        return new UserRestModel(user);
    }

    public UserRestModel registerByAdmin(UserRestModel userRestModel, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        User user = new User();

        Optional<User> emailOptional = userRepository.findByEmail(userRestModel.getEmail());
        Optional<User> phoneOptional = userRepository.findByPhoneNumber(userRestModel.getPhoneNumber());


        if (!currentUser.getRole().getRoleName().equals(ADMIN))
            throw new NotAuthenticatedException(WRONG_TOKEN.getMessage());
        if (emailOptional.isPresent())
            throw new WrongRequestException(WRONG_EMAIL.getMessage());
        if (String.valueOf(userRestModel.getPhoneNumber()).length() != 9)
            throw new WrongRequestException(INCORRECT_PHONE_NUMBER.getMessage());
        else if (phoneOptional.isPresent())
            throw new WrongRequestException(WRONG_PHONE_NUMBER.getMessage());
        else {
            user.setFirstName(userRestModel.getFirstName());
            user.setLastName(userRestModel.getLastName());
            user.setEmail(userRestModel.getEmail());
            user.setPhoneNumber(userRestModel.getPhoneNumber());
            user.setPassword(bCryptPasswordEncoder.encode(userRestModel.getPassword()));
            user.setDeleted(false);
            user.setRole(roleRepository.findById(userRestModel.getRoleId()).orElseThrow(() ->
                    new ResourceNotFoundException(ROLE_NOT_FOUND.getMessage())
            ));
            userRepository.save(user);

        }

        return new UserRestModel(user);
    }


    public List<UserRestModel> getAll(String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        if (isAdminOrWorker(currentUser)) {
            return userRepository.findAll().stream()
                    .map(UserRestModel::new)
                    .collect(Collectors.toList());
        } else {
            throw new NotAuthenticatedException(WRONG_TOKEN.getMessage());
        }
    }


    public UserRestModel getById(String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        return userRepository.findById(currentUser.getIdUser()).map(UserRestModel::new).get();

    }

    public UserRestModel update(UserRestModel userRestModel, Integer idUser, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        User editedUser = userRepository.findById(idUser).orElseThrow(() ->
                new ResourceNotFoundException(USER_NOT_FOUND.getMessage()));

        if (!currentUser.getIdUser().equals(idUser) && !currentUser.getRole().getRoleName().equals(ADMIN))
            throw new WrongRequestException(WRONG_TOKEN.getMessage());

        if (String.valueOf(userRestModel.getPhoneNumber()).length() != 9)
            throw new WrongRequestException(INCORRECT_PHONE_NUMBER.getMessage());

        else if (!editedUser.getEmail().equals(userRestModel.getEmail())) {
            Optional<User> emailOptional = userRepository.findByEmail(userRestModel.getEmail());
            if (emailOptional.isPresent())
                throw new WrongRequestException(WRONG_EMAIL.getMessage());

        } else if (!editedUser.getPhoneNumber().equals(userRestModel.getPhoneNumber())) {
            Optional<User> phoneOptional = userRepository.findByPhoneNumber(userRestModel.getPhoneNumber());
            if (phoneOptional.isPresent())
                throw new WrongRequestException(WRONG_PHONE_NUMBER.getMessage());
        }

        editedUser.setFirstName(userRestModel.getFirstName());
        editedUser.setLastName(userRestModel.getLastName());
        editedUser.setEmail(userRestModel.getEmail());
        editedUser.setPhoneNumber(userRestModel.getPhoneNumber());
        if (userRestModel.getPassword() != null)
            editedUser.setPassword(bCryptPasswordEncoder.encode(userRestModel.getPassword()));

        return new UserRestModel(userRepository.save(editedUser));

    }

    public void delete(Integer idUser, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        User editedUser = userRepository.findById(idUser).orElseThrow(() ->
                new ResourceNotFoundException(USER_NOT_FOUND.getMessage()));

        if (!currentUser.getIdUser().equals(idUser) && !currentUser.getRole().getRoleName().equals(ADMIN))
            throw new WrongRequestException(WRONG_TOKEN.getMessage());

        List<Reservation> reservationList = editedUser.getListOfReservation();

        if (!reservationList.isEmpty()) {
            connectionDeleter.deleteReservations(reservationList);
            editedUser.setListOfReservation(Collections.emptyList());
        }

        editedUser.setDeleted(true);
        userRepository.save(editedUser);

    }

}

