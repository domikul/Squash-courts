package pl.euvic.squash.model.response;

import pl.euvic.squash.model.entity.Court;

public class CourtRestModel {

    private Integer courtId;
    private Integer clubId;
    private String description;
    private Boolean active;

    public CourtRestModel() {
    }

    public CourtRestModel(Court court) {
        this.courtId = court.getIdCourt();
        this.clubId = court.getClub().getIdClub();
        this.description = court.getDescription();
        this.active = court.getActive();
    }

    public Integer getIdCourt() {
        return courtId;
    }

    public void setIdCourt(Integer idCourt) {
        this.courtId = idCourt;
    }

    public Integer getClubId() {
        return clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
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
}
