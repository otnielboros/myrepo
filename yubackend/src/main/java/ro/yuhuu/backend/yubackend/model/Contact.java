package ro.yuhuu.backend.yubackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "contact")
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contact_id")
    private Long id;
    private String phoneNumber;
    private String facebookLink;
    private String website;
    private String linkedinLink;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "contact")
    private Address address;

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "contact")
    private Photo photo;

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "contact")
    private CV cv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLinkedinLink() {
        return linkedinLink;
    }

    public void setLinkedinLink(String linkedinLink) {
        this.linkedinLink = linkedinLink;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address=address;
        address.setContact(this);
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
        photo.setContact(this);
    }

    public CV getCv() {
        return cv;
    }

    public void setCv(CV cv) {
        this.cv = cv;
        cv.setContact(this);
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public static class Builder{
        private Contact contact;
        public Builder(){
            contact = new Contact();
        }

        public Builder withId(Long id){
            this.contact.id=id;
            return this;
        }
        public Builder withPhoto(Photo photo){
            this.contact.photo=photo;
            photo.setContact(contact);
            return this;
        }
        public Builder withPhoneNumber(String phoneNumber){
            this.contact.phoneNumber=phoneNumber;
            return this;
        }
        public Builder withFacebookLink(String facebookLink){
            this.contact.facebookLink=facebookLink;
            return this;
        }
        public Builder withWebsite(String website){
            this.contact.website=website;
            return this;
        }
        public Builder withLinkedinLink(String linkedinLink){
            this.contact.linkedinLink=linkedinLink;
            return this;
        }
        public Builder withAddress(Address address){
            this.contact.address=address;
            address.setContact(contact);
            return this;
        }

        public Builder withCV(CV cv){
            this.contact.cv = cv;
            cv.setContact(contact);
            return this;
        }

        public Contact build(){

            return contact;
        }
    }

}
