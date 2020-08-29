package pl.euvic.squash.model.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "court", catalog = "postgres")
@Where(clause = "deleted='false'")
public class Court implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCourt;

    @Column
    private String description;

    @Column
    private Boolean active;

    @Column
    private Boolean deleted;

    @ManyToOne
    private Club club;

    @OneToMany(mappedBy = "court", targetEntity = Reservation.class)
    private List<Reservation> listOfReservation;

    public Court() {
    }

    public Court(String description, Club club, Boolean active, Boolean deleted) {
        this.description = description;
        this.club = club;
        this.active = active;
        this.deleted = deleted;
    }

    public Integer getIdCourt() {
        return idCourt;
    }

    public void setIdCourt(Integer idCourt) {
        this.idCourt = idCourt;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<Reservation> getListOfReservation() {
        return listOfReservation;
    }

    public void setListOfReservation(List<Reservation> listOfReservation) {
        this.listOfReservation = listOfReservation;
    }
}
