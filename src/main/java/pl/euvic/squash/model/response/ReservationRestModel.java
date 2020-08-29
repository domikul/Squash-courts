package pl.euvic.squash.model.response;

import pl.euvic.squash.model.entity.Reservation;

import java.util.Date;


public class ReservationRestModel {

    private Integer idReservation;
    private Date startTime;
    private Date endTime;
    private Integer userId;
    private Integer courtId;
    private Boolean deleted;

    public ReservationRestModel() {
    }

    public ReservationRestModel(Reservation reservation) {
        this.idReservation = reservation.getIdReservation();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.userId = reservation.getUser().getIdUser();
        this.courtId = reservation.getCourt().getIdCourt();
        this.deleted = reservation.getDeleted();
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
