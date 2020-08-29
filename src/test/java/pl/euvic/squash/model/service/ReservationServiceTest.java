package pl.euvic.squash.model.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import pl.euvic.squash.exception.WrongRequestException;
import pl.euvic.squash.model.entity.*;
import pl.euvic.squash.model.enumeration.RoleEnum;
import pl.euvic.squash.model.repository.ClubRepository;
import pl.euvic.squash.model.repository.CourtRepository;
import pl.euvic.squash.model.repository.ReservationRepository;
import pl.euvic.squash.model.response.ReservationRestModel;
import pl.euvic.squash.model.scheduling.MailDataSchedule;
import pl.euvic.squash.model.scheduling.ReservationDataSchedule;
import pl.euvic.squash.security.helper.JwtHelper;

import java.time.Instant;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static pl.euvic.squash.exception.ExceptionMessage.RESERVATION_IS_PRESENT;
import static pl.euvic.squash.exception.ExceptionMessage.TOO_MANY_RESERVATIONS;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    private Court court;
    private User tokenUser;
    private Role role;
    private Club club;
    private List<Reservation> usersReservation = Collections.emptyList();
    private List<Reservation> clubReservations = Collections.emptyList();

    private List<Court> courtList = Collections.emptyList();

    private ReservationRestModel reservationRestModel = new ReservationRestModel();

    final Reservation reservation[] = new Reservation[1];

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationService reservationService;

    @Mock
    JwtHelper jwtHelper;

    @Mock
    ClubRepository clubRepository;

    @Mock
    CourtRepository courtRepository;

    @Mock
    ReservationDataSchedule reservationDataSchedule;

    @Mock
    MailDataSchedule mailDataSchedule;

    @Mock
    Calendar calendar;
    @Mock
    GregorianCalendar gregorianCalendar;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {


        role = new Role();
        role.setIdRole(3);
        role.setRoleName(RoleEnum.USER);

        tokenUser = new User();
        tokenUser.setIdUser(10);
        tokenUser.setFirstName("userTest");
        tokenUser.setLastName("userTest");
        tokenUser.setEmail("email@korty.pl");
        tokenUser.setRole(role);
        tokenUser.setListOfReservation(usersReservation);
        tokenUser.setDeleted(false);
        tokenUser.setPhoneNumber(123456789);
        Mockito.when(jwtHelper.getUserFromToken("")).thenReturn(tokenUser);

        club = new Club();
        club.setAdress("adress");
        club.setDeleted(false);
        club.setPostalCode("44-100");
        club.setName("club");
        club.setCity("city");
        club.setIdClub(10);
        club.setListOfCourts(courtList);
        Mockito.when(clubRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(club));


        court = new Court();
        court.setIdCourt(10);
        court.setDeleted(false);
        court.setActive(true);
        court.setDescription("desc");
        court.setClub(club);
        court.setListOfReservation(clubReservations);
        Mockito.when(courtRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(court));

        reservation[0] = new Reservation();
        reservation[0].setDeleted(false);
        reservation[0].setCourt(court);
        reservation[0].setIdReservation(15);
        reservation[0].setUser(tokenUser);
        Mockito.when(reservationRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(reservation[0]));

    }


    @Test
    public void addReservation_createNew_isOk() {

        Mockito.when(reservationRepository.existsByStartTimeAndCourt(Mockito.any(), Mockito.any())).thenReturn(false);

        Date startDate = Date.from(Instant.parse("2020-08-30T20:00:00.000Z"));
        Date endDate = Date.from(Instant.parse("2020-08-30T21:00:00.000Z"));
        final Reservation reservation = new Reservation(startDate, endDate, tokenUser, court, false);
        final ReservationRestModel reservationRestModel = new ReservationRestModel(reservation);

        Mockito.when(reservationRepository.save(Mockito.anyObject())).thenAnswer(i -> i.getArguments()[0]);

        ReservationRestModel savedReservation = reservationService.addReservation(reservationRestModel, "");

        assertThat(savedReservation.getCourtId(), is(10));
        assertThat(savedReservation.getUserId(), is(10));
        assertThat(savedReservation.getDeleted(), is(false));
        assertThat(savedReservation.getStartTime(), is(startDate));
        assertThat(savedReservation.getEndTime(), is(endDate));
    }

    @Test
    public void addReservation_reservationAlreadyExist() {

        Date startDate = Date.from(Instant.parse("2020-08-30T20:00:00.000Z"));
        Date endDate = Date.from(Instant.parse("2020-08-30T21:00:00.000Z"));
        Mockito.when(reservationRepository.existsByStartTimeAndCourt(Mockito.any(), Mockito.any())).thenReturn(true);
        final Reservation reservation = new Reservation(startDate, endDate, tokenUser, court, false);
        final ReservationRestModel reservationRestModel = new ReservationRestModel(reservation);

        thrown.expect(WrongRequestException.class);
        thrown.expectMessage(is(RESERVATION_IS_PRESENT.getMessage()));

        reservationService.addReservation(reservationRestModel, "");

    }

    @Test
    public void addReservation_userHaveTwoReservations() {


        Date startDate = Date.from(Instant.parse("2020-08-30T20:00:00.000Z"));
        Date endDate = Date.from(Instant.parse("2020-08-30T21:00:00.000Z"));
        Mockito.when(reservationRepository.existsByStartTimeAndCourt(Mockito.any(), Mockito.any())).thenReturn(false);
        final Reservation reservation = new Reservation(startDate, endDate, tokenUser, court, false);
        final ReservationRestModel reservationRestModel = new ReservationRestModel(reservation);
        List<Reservation> uRes = new ArrayList<>();


        Mockito.doAnswer(it -> {
            Reservation r = it.getArgumentAt(0, Reservation.class);
            uRes.add(r);
            return r;
        }).when(reservationRepository).save(Mockito.any(Reservation.class));

        tokenUser.setListOfReservation(uRes);

        for (int i = 0; i < 2; i++) {
            reservationService.addReservation(reservationRestModel, "");
        }

        thrown.expect(WrongRequestException.class);
        thrown.expectMessage(is(TOO_MANY_RESERVATIONS.getMessage()));
        reservationService.addReservation(reservationRestModel, "");

    }

}