package pl.euvic.squash.model.service;

import org.springframework.stereotype.Service;
import pl.euvic.squash.exception.NotAuthenticatedException;
import pl.euvic.squash.exception.WrongRequestException;
import pl.euvic.squash.model.entity.Court;
import pl.euvic.squash.model.entity.Reservation;
import pl.euvic.squash.model.entity.User;
import pl.euvic.squash.model.repository.CourtRepository;
import pl.euvic.squash.model.repository.ReservationRepository;
import pl.euvic.squash.model.response.ReservationRestModel;
import pl.euvic.squash.model.scheduling.MailDataSchedule;
import pl.euvic.squash.model.scheduling.ReservationDataSchedule;
import pl.euvic.squash.security.helper.JwtHelper;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import static pl.euvic.squash.exception.ExceptionMessage.*;
import static pl.euvic.squash.model.enumeration.RoleEnum.ADMIN;

@Service
public class ReservationService {

    private final JwtHelper jwtHelper;
    private final CourtRepository courtRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationDataSchedule reservationDataSchedule;
    private final MailDataSchedule mailDataSchedule;

    public ReservationService(JwtHelper jwtHelper, CourtRepository courtRepository, ReservationRepository reservationRepository, ReservationDataSchedule reservationDataSchedule, MailDataSchedule mailDataSchedule) {
        this.jwtHelper = jwtHelper;
        this.courtRepository = courtRepository;
        this.reservationRepository = reservationRepository;
        this.reservationDataSchedule = reservationDataSchedule;
        this.mailDataSchedule = mailDataSchedule;
    }


    public List<ReservationRestModel> getOwn(String token) {
        User currentUser = jwtHelper.getUserFromToken(token);
        return reservationRepository.findAllByUser(currentUser)
                .stream()
                .map(ReservationRestModel::new)
                .collect(Collectors.toList());

    }

    public List<ReservationRestModel> getCourtReservations(Integer courtId) {

        Court currentCourt = courtRepository.findById(courtId)
                .orElseThrow(() -> new WrongRequestException(COURT_NOT_EXIST.getMessage()));

        return reservationRepository.findAllByCourt(currentCourt)
                .stream()
                .map(ReservationRestModel::new)
                .collect(Collectors.toList());

    }

    public ReservationRestModel addReservation(ReservationRestModel reservationRestModel, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        List<Reservation> usersReservation = currentUser.getListOfReservation();

        Court reservedCourt = courtRepository.findById(reservationRestModel.getCourtId())
                .orElseThrow(() -> new WrongRequestException(COURT_NOT_EXIST.getMessage()));


        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(reservationRestModel.getStartTime());

        if (calendar.get(Calendar.MINUTE) != 0)
            throw new WrongRequestException(WRONG_TIME.getMessage());

        if (reservationRepository.existsByStartTimeAndCourt(reservationRestModel.getStartTime(), reservedCourt))
            throw new WrongRequestException(RESERVATION_IS_PRESENT.getMessage());

        if (reservationRestModel.getStartTime().before(new Date()))
            throw new WrongRequestException(TIME_BEFORE_CURRENT_DATE.getMessage());

        if (usersReservation.size() == 2)
            throw new WrongRequestException(TOO_MANY_RESERVATIONS.getMessage());

        if (reservedCourt.getActive().equals(false))
            throw new WrongRequestException(NOT_ACTIVE.getMessage());

        Reservation newReservation = new Reservation(reservationRestModel.getStartTime(),
                Date.from(reservationRestModel.getStartTime().toInstant().plus(Duration.ofHours(1))), currentUser, reservedCourt, false);


        ReservationRestModel restModel = new ReservationRestModel(reservationRepository.save(newReservation));
        reservationDataSchedule.onReservationAdded(newReservation);
        mailDataSchedule.onReservationAdded(newReservation);
        return restModel;
    }


    public void deleteReservation(Integer idReservation, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        Reservation currentReservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new WrongRequestException(RESERVATION_NOT_EXIST.getMessage()));

        if (!currentReservation.isUserOwner(currentUser) && !currentUser.getRole().getRoleName().equals(ADMIN))
            throw new NotAuthenticatedException(WRONG_TOKEN.getMessage());

        reservationDataSchedule.onReservationDeleting(currentReservation);
        mailDataSchedule.onReservationDeleting(currentReservation);

        currentReservation.setDeleted(true);
        reservationRepository.save(currentReservation);


    }
}
