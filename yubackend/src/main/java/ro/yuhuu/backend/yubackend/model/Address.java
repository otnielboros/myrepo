package ro.yuhuu.backend.yubackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "address")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private Long id;
    private Double latitude;
    private Double longitude;
    private String country;
    private String county;
    private String sector;
    private String postalCode;
    private String town;
    private String street;
    private String number;
    private String block;
    private String entrance;
    private String floor;
    private String apartment;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Override
    public String toString() {
        String rez = "";
        if(street!=null && !street.equals(""))
            rez+=street+" ";
        if(number!=null && !number.equals(""))
            rez+=number+" ";
        if(town!=null && !town.equals(""))
            rez+=town+" ";
        if(county!=null && !county.equals(""))
            rez+=county+" ";
        if(country!=null && !country.equals(""))
            rez+=country+" ";
        return rez;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public static class Builder{
        private Long id;
        private Double latitude;
        private Double longitude;
        private String country;
        private String county;
        private String sector;
        private String postalCode;
        private String town;
        private String street;
        private String number;
        private String block;
        private String entrance;
        private String floor;
        private String apartment;
        private Contact contact;

        public Builder(){}
        public Builder(Long id){
            this.id=id;
        }
        public Builder withLatitude(Double latitude){
            this.latitude=latitude;
            return this;
        }
        public Builder withContact(Contact contact){
            this.contact=contact;
            return this;
        }
        public Builder withLongitude(Double longitude){
            this.longitude=longitude;
            return this;
        }
        public Builder withTown(String town){
            this.town=town;
            return this;
        }
        public Builder withCountry(String country){
            this.country=country;
            return this;
        }
        public Builder withCounty(String county){
            this.county=county;
            return this;
        }
        public Builder withSector(String sector){
            this.sector=sector;
            return this;
        }
        public Builder withPostalCode(String postalCode){
            this.postalCode=postalCode;
            return this;
        }
        public Builder withStreet(String street){
            this.street=street;
            return this;
        }
        public Builder withNumber(String number){
            this.number=number;
            return this;
        }
        public Builder withBlock(String block){
            this.block=block;
            return this;
        }
        public Builder withEntrance(String entrance){
            this.entrance=entrance;
            return this;
        }
        public Builder withFloor(String floor){
            this.floor=floor;
            return this;
        }
        public Builder withApartment(String apartment){
            this.apartment=apartment;
            return this;
        }
        public Address build(){
            Address address = new Address();
            address.apartment=this.apartment;
            address.floor=this.floor;
            address.entrance=this.entrance;
            address.block=this.block;
            address.number=this.number;
            address.street=this.street;
            address.postalCode=this.postalCode;
            address.town=this.town;
            address.county=this.county;
            address.country=this.country;
            address.sector=this.sector;
            address.latitude=this.latitude;
            address.longitude=this.longitude;
            address.id=this.id;
            address.contact=this.contact;
            return address;
        }
    }
}
