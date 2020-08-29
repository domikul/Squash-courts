package pl.euvic.squash.model.response;

import pl.euvic.squash.model.entity.Role;

public class RoleRestModel {

    private Integer idRole;
    private String roleName;

    public RoleRestModel() {
    }

    public RoleRestModel(Role role) {
        this.idRole = role.getIdRole();
        this.roleName = role.getRoleName().name();
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
