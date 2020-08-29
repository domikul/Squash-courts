package pl.euvic.squash.model.service;

import org.springframework.stereotype.Service;
import pl.euvic.squash.exception.NotAuthenticatedException;
import pl.euvic.squash.exception.WrongRequestException;
import pl.euvic.squash.model.entity.Club;
import pl.euvic.squash.model.entity.Court;
import pl.euvic.squash.model.entity.User;
import pl.euvic.squash.model.enumeration.RoleEnum;
import pl.euvic.squash.model.repository.ClubRepository;
import pl.euvic.squash.model.response.ClubRestModel;
import pl.euvic.squash.security.helper.JwtHelper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.euvic.squash.exception.ExceptionMessage.*;

@Service
public class ClubService {

    private final JwtHelper jwtHelper;
    private final ClubRepository clubRepository;
    private final ConnectionDeleter connectionDeleter;

    public ClubService(JwtHelper jwtHelper, ClubRepository clubRepository, ConnectionDeleter connectionDeleter) {
        this.jwtHelper = jwtHelper;
        this.clubRepository = clubRepository;
        this.connectionDeleter = connectionDeleter;
    }

    public List<ClubRestModel> getAll() {
        return clubRepository.findAll().stream()
                .map(ClubRestModel::new)
                .collect(Collectors.toList());
    }


    public List<ClubRestModel> getByCity(String city) {
        return clubRepository.findAllByCity(city).stream()
                .map(ClubRestModel::new)
                .collect(Collectors.toList());
    }


    public ClubRestModel addClub(ClubRestModel clubRestModel, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        checkClub(clubRestModel, currentUser);

        Club currentClub = new Club(clubRestModel.getName(), clubRestModel.getAdress(), clubRestModel.getCity(), clubRestModel.getPostalCode(), false);
        return new ClubRestModel(clubRepository.save(currentClub));

    }


    public ClubRestModel updateClub(ClubRestModel clubRestModel, Integer idClub, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        Club currentClub = clubRepository.findById(idClub)
                .orElseThrow(() -> new WrongRequestException(CLUB_NOT_EXIST.getMessage()));

        checkClub(clubRestModel, currentUser);

        currentClub.setAdress(clubRestModel.getAdress());
        currentClub.setCity(clubRestModel.getCity());
        currentClub.setName(clubRestModel.getName());
        currentClub.setPostalCode(clubRestModel.getPostalCode());

        return new ClubRestModel(clubRepository.save(currentClub));

    }

    private void checkClub(ClubRestModel clubRestModel, User currentUser) {
        Optional<Club> adressOptional = clubRepository.findByAdress(clubRestModel.getAdress());
        Optional<Club> nameOptional = clubRepository.findByName(clubRestModel.getName());

        if (!currentUser.getRole().getRoleName().equals(RoleEnum.ADMIN) && !currentUser.getRole().getRoleName().equals(RoleEnum.MANAGER))
            throw new NotAuthenticatedException(WRONG_TOKEN.getMessage());

        if (adressOptional.isPresent() || nameOptional.isPresent())
            throw new WrongRequestException(CLUB_ALREADY_EXIST.getMessage());


    }

    public void deleteClub(Integer idClub, String token) {

        Club currentClub = clubRepository.findById(idClub).orElseThrow(() ->
                new WrongRequestException(CLUB_NOT_EXIST.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);

        if (!currentUser.getRole().getRoleName().equals(RoleEnum.ADMIN) && !currentUser.getRole().getRoleName().equals(RoleEnum.MANAGER))
            throw new NotAuthenticatedException(WRONG_TOKEN.getMessage());


        List<Court> courtList = currentClub.getListOfCourts();

        if (!courtList.isEmpty()) {
            connectionDeleter.deleteCourts(courtList);
            currentClub.setListOfCourts(Collections.emptyList());
        }

        currentClub.setDeleted(true);
        clubRepository.save(currentClub);

    }


}
