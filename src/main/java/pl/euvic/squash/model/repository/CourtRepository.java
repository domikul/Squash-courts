package pl.euvic.squash.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.euvic.squash.model.entity.Club;
import pl.euvic.squash.model.entity.Court;

import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court, Integer> {
    List<Court> findAllByClub(Club club);
}
