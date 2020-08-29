package pl.euvic.squash.model.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reservation", catalog = "postgres")
@Where(clause = "deleted='false'")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idReservation;

    @Column
    private Date startTime;

    @Column
    private Date endTime;

    @ManyToOne
    private User user;

    @ManyToOne
    private Court court;

    @Column
    private Boolean deleted;

    public Reservation() {
    }

    public Reservation(Date startTime, Date endTime, User user, Court court, Boolean deleted) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.court = court;
        this.deleted = deleted;
    }

    public Integer getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Integer idReservation) {
        this.idReservation = idReservation;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public Boolean isUserOwner(User user) {
        return this.user.equals(user);
    }

}
