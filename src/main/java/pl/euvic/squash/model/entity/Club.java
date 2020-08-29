package pl.euvic.squash.model.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "club", catalog = "postgres")
@Where(clause = "deleted='false'")
public class Club implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idClub;

    @Column
    private String name;

    @Column
    private String adress;

    @Column
    private String city;

    @Column
    private String postalCode;

    @Column
    private Boolean deleted;

    @OneToMany(mappedBy = "club", targetEntity = Court.class)
    private List<Court> listOfCourts;

    public Club() {
    }

    public Club(String name, String adress, String city, String postalCode, Boolean deleted) {
        this.name = name;
        this.adress = adress;
        this.city = city;
        this.postalCode = postalCode;
        this.deleted = deleted;
    }

    public Integer getIdClub() {
        return idClub;
    }

    public void setIdClub(Integer idClub) {
        this.idClub = idClub;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<Court> getListOfCourts() {
        return listOfCourts;
    }

    public void setListOfCourts(List<Court> listOfCourts) {
        this.listOfCourts = listOfCourts;
    }
}
