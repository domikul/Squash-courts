package pl.euvic.squash.model.service;

import org.springframework.stereotype.Service;
import pl.euvic.squash.model.repository.RoleRepository;
import pl.euvic.squash.model.response.RoleRestModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleRestModel> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleRestModel::new)
                .collect(Collectors.toList());
    }
}
