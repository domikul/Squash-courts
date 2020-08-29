package pl.euvic.squash.model.response;

import pl.euvic.squash.model.entity.Club;

public class ClubRestModel {

    private Integer clubId;
    private String name;
    private String adress;
    private String city;
    private String postalCode;

    public ClubRestModel() {
    }

    public ClubRestModel(Club club) {
        this.clubId = club.getIdClub();
        this.name = club.getName();
        this.adress = club.getAdress();
        this.city = club.getCity();
        this.postalCode = club.getPostalCode();
    }

    public Integer getClubId() {
        return clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
