package pl.euvic.squash.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.euvic.squash.model.entity.Role;
import pl.euvic.squash.model.enumeration.RoleEnum;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByRoleName(RoleEnum roleEnum);
}
