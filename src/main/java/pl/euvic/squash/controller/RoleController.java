package pl.euvic.squash.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.euvic.squash.model.response.RoleRestModel;
import pl.euvic.squash.model.service.RoleService;

import java.util.List;

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = "/roles")
    public ResponseEntity<List<RoleRestModel>> getRoles() {
        final List<RoleRestModel> roleList = roleService.getAllRoles();
        return ResponseEntity.ok(roleList);
    }

}
