package pl.euvic.squash.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.euvic.squash.model.entity.Court;
import pl.euvic.squash.model.entity.Reservation;
import pl.euvic.squash.model.entity.User;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Boolean existsByStartTimeAndCourt(Date startTime, Court court);

    List<Reservation> findAllByUser(User user);

    List<Reservation> findAllByCourt(Court court);
}
