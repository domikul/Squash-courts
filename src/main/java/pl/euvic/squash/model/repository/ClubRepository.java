package pl.euvic.squash.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.euvic.squash.model.entity.Club;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Integer> {

    Optional<Club> findByName(String name);

    Optional<Club> findByAdress(String adress);

    List<Club> findAllByCity(String city);

}
