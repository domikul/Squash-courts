package pl.euvic.squash.model.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.euvic.squash.model.entity.Reservation;
import pl.euvic.squash.model.repository.ReservationRepository;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ReservationDataSchedule {

    private final Map<Integer, Date> expirationDates = new ConcurrentHashMap<>();
    private final ReservationRepository reservationRepository;

    public ReservationDataSchedule(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostConstruct
    void populateData() {
        reservationRepository.findAll().forEach(reservation -> {
            expirationDates.put(reservation.getIdReservation(), reservation.getEndTime());
        });
    }

    public void onReservationAdded(Reservation reservation) {
        expirationDates.put(reservation.getIdReservation(), reservation.getEndTime());
    }

    public void onReservationDeleting(Reservation reservation) {
        expirationDates.remove(reservation.getIdReservation());
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void checkReservationTiming() {

        Date currentDate = new Date();

        List<Integer> reservationIds = expirationDates.entrySet().stream()
                .filter(it -> it.getValue().before(currentDate))
                .map(Map.Entry::getKey).collect(Collectors.toList());

        if (!reservationIds.isEmpty()) {
            List<Reservation> reservationsToDelete = reservationRepository.findAllById(reservationIds);

            for (Reservation reservations : reservationsToDelete) {
                reservations.setDeleted(true);
                expirationDates.remove(reservations.getIdReservation());
            }
            reservationRepository.saveAll(reservationsToDelete);
        }
    }

}
