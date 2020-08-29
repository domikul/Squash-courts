package pl.euvic.squash.model.service;

import org.springframework.stereotype.Service;
import pl.euvic.squash.exception.NotAuthenticatedException;
import pl.euvic.squash.exception.WrongRequestException;
import pl.euvic.squash.model.entity.Club;
import pl.euvic.squash.model.entity.Court;
import pl.euvic.squash.model.entity.Reservation;
import pl.euvic.squash.model.entity.User;
import pl.euvic.squash.model.enumeration.RoleEnum;
import pl.euvic.squash.model.repository.ClubRepository;
import pl.euvic.squash.model.repository.CourtRepository;
import pl.euvic.squash.model.response.CourtRestModel;
import pl.euvic.squash.security.helper.JwtHelper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.euvic.squash.exception.ExceptionMessage.*;
import static pl.euvic.squash.model.enumeration.RoleEnum.USER;

@Service
public class CourtService {

    private final JwtHelper jwtHelper;
    private final ClubRepository clubRepository;
    private final CourtRepository courtRepository;
    private final ConnectionDeleter connectionDeleter;

    public CourtService(JwtHelper jwtHelper, ClubRepository clubRepository, CourtRepository courtRepository, ConnectionDeleter connectionDeleter) {
        this.jwtHelper = jwtHelper;
        this.clubRepository = clubRepository;
        this.courtRepository = courtRepository;
        this.connectionDeleter = connectionDeleter;
    }

    public List<CourtRestModel> getAll() {
        return courtRepository.findAll().stream()
                .map(CourtRestModel::new)
                .collect(Collectors.toList());
    }

    public List<CourtRestModel> getCourtsInClub(Integer clubId) {
        Club currentClub = clubRepository.findById(clubId)
                .orElseThrow(() -> new WrongRequestException(CLUB_NOT_EXIST.getMessage()));

        return courtRepository.findAllByClub(currentClub)
                .stream().map(CourtRestModel::new)
                .collect(Collectors.toList());

    }

    public CourtRestModel addCourt(CourtRestModel courtRestModel, String token) {
        User currentUser = jwtHelper.getUserFromToken(token);

        if (currentUser.getRole().getRoleName().equals(USER))
            throw new NotAuthenticatedException(WRONG_TOKEN.getMessage());

        Club currentClub = clubRepository.findById(courtRestModel.getClubId())
                .orElseThrow(() -> new WrongRequestException(CLUB_NOT_EXIST.getMessage()));

        Court newCourt = new Court(courtRestModel.getDescription(), currentClub, true, false);
        return new CourtRestModel(courtRepository.save(newCourt));
    }

    public CourtRestModel updateCourt(CourtRestModel courtRestModel, Integer idCourt, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        if (currentUser.getRole().getRoleName().equals(USER))
            throw new NotAuthenticatedException(WRONG_TOKEN.getMessage());

        Court currentCourt = courtRepository.findById(idCourt)
                .orElseThrow(() -> new WrongRequestException(COURT_NOT_EXIST.getMessage()));

        currentCourt.setDescription(courtRestModel.getDescription());
        currentCourt.setActive(courtRestModel.getActive());

        return new CourtRestModel(courtRepository.save(currentCourt));
    }

    public void deleteCourt(Integer idCourt, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        Court currentCourt = courtRepository.findById(idCourt)
                .orElseThrow(() -> new WrongRequestException(COURT_NOT_EXIST.getMessage()));

        if (!currentUser.getRole().getRoleName().equals(RoleEnum.ADMIN) && !currentUser.getRole().getRoleName().equals(RoleEnum.MANAGER))
            throw new NotAuthenticatedException(WRONG_TOKEN.getMessage());

        List<Reservation> reservationList = currentCourt.getListOfReservation();

        if (!reservationList.isEmpty()) {
            connectionDeleter.deleteReservations(reservationList);
            currentCourt.setListOfReservation(Collections.emptyList());
        }

        currentCourt.setDeleted(true);
        courtRepository.save(currentCourt);

    }

}
