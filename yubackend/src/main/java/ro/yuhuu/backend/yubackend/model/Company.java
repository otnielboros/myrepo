package ro.yuhuu.backend.yubackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity(name = "company")
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;

    private Long views=0l;


    public void setInternships(Set<Internship> internships) {
        this.internships = internships;
    }

    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, optional = true)
    private Contact contact;

    private Long dimension;

    @Lob @Basic(fetch=FetchType.EAGER)
    private String description;

    public Set<Internship> getInternships() {
        return internships;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Internship> internships = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "view",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))

    public void addInternship(Internship internship){
        this.internships.add(internship);
        internship.setCompany(this);
    }

    public void removeInternship(Internship internship){
        this.internships.remove(internship);
        internship.setCompany(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        contact.setCompany(this);
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getDimension() {
        return dimension;
    }

    public void setDimension(Long dimension) {
        this.dimension = dimension;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Builder{
        private Company company;
        public Builder(){
            company = new Company();
        }
        public Builder withId(Long id){
            company.id=id;
            return this;
        }
        public Builder setUser(User user){
            company.user=user;
            return this;
        }
        public Builder withName(String name){
            company.name=name;
            return this;
        }
        public Builder withContact(Contact contact){
            company.setContact(contact);
            return this;
        }
        public Builder withDimension(Long dimension){
            company.dimension=dimension;
            return this;
        }
        public Builder withDescription(String description){
            company.description=description;
            return this;
        }
        public Builder addInternship(Internship internship){
            company.addInternship(internship);
            return this;
        }
        public Company build(){
            return company;
        }
    }
}
