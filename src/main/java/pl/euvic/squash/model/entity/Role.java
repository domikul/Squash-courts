package pl.euvic.squash.model.entity;

import pl.euvic.squash.model.enumeration.RoleEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "role", catalog = "postgres")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idRole;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

    @OneToMany(mappedBy = "role", targetEntity = User.class)
    private List<User> listOfUsers;

    public Role() {
    }

    public Role(RoleEnum roleName, List<User> listOfUsers) {
        this.roleName = roleName;
        this.listOfUsers = listOfUsers;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public RoleEnum getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleEnum roleName) {
        this.roleName = roleName;
    }

    public List<User> getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(List<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }
}
