package pl.euvic.squash.model.service;

import org.springframework.stereotype.Service;
import pl.euvic.squash.model.entity.Court;
import pl.euvic.squash.model.entity.Reservation;
import pl.euvic.squash.model.repository.CourtRepository;
import pl.euvic.squash.model.repository.ReservationRepository;

import java.util.List;

@Service
public class ConnectionDeleter {

    private final ReservationRepository reservationRepository;
    private final CourtRepository courtRepository;

    public ConnectionDeleter(ReservationRepository reservationRepository, CourtRepository courtRepository) {
        this.reservationRepository = reservationRepository;
        this.courtRepository = courtRepository;
    }

    /*
        void deleteUserTimer(Task task, User user);

        public void deleteTimers(List<Timer> timersToDelete)

    void deleteTasks(List<Task> tasksToDelete, User user);
     */

    public void deleteReservations(List<Reservation> reservationList) {
        for (Reservation r : reservationList) {
            r.setDeleted(true);
            reservationRepository.save(r);
        }
    }

    public void deleteCourts(List<Court> courtList) {
        for (Court c : courtList) {
            List<Reservation> reservations = c.getListOfReservation();
            if (!reservations.isEmpty())
                deleteReservations(reservations);

            c.setDeleted(true);
            courtRepository.save(c);
        }
    }

}
