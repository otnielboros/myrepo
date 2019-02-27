package ro.yuhuu.backend.yubackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "applicant")
public class Applicant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "applicant_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "applicant",fetch = FetchType.EAGER)
    private Set<InternshipRequest> internshipRequests = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "applicant_skill",
            joinColumns = @JoinColumn(name = "applicant_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    public Set<Skill> getSkills() {
        return skills;
    }

    public Set<InternshipRequest> getInternshipRequests() {
        return internshipRequests;
    }

    public void setInternshipRequests(Set<InternshipRequest> internshipRequests) {
        this.internshipRequests = internshipRequests;
    }

    public void addSkill(Skill skill){
        this.skills.add(skill);
        skill.getApplicants().add(this);
    }

    public void removeSkill(Skill skill){
        this.skills.remove(skill);
        skill.getApplicants().remove(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToOne(mappedBy = "applicant", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = true)
    private Contact contact;

    public Set<Education> getEducations() {
        return educations;
    }

    @JsonIgnore
    @OneToMany(
            mappedBy = "applicant",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Education> educations = new HashSet<>();

    public void addEducation(Education education){
        this.educations.add(education);
        education.setApplicant(this);
    }

    public void removeEducation(Education education){
        this.educations.remove(education);
    }

    public void removeInternshipRequest(InternshipRequest internshipRequest){
        this.internshipRequests.remove(internshipRequest);
    }

    @JsonIgnore
    @OneToMany(
            mappedBy = "applicant",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Experience> experiences = new HashSet<>();

    public void addExperience(Experience experience){
        this.experiences.add(experience);
        experience.setApplicant(this);
    }


    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinTable(name = "applicant_internship",
            joinColumns = @JoinColumn(name = "applicant_id"),
            inverseJoinColumns = @JoinColumn(name = "internship_id")
    )
    private Set<Internship> internships = new HashSet<>();

    @JsonIgnore
    public Set<Internship> getInternship(){
        return this.internships;
    }

    public void removeInternship(Internship internship){
        this.internships.remove(internship);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        contact.setApplicant(this);
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public static class Builder{
        private Applicant applicant;

        public Builder(){
            applicant = new Applicant();
        }
        public Builder withFirstName(String firstName){
            applicant.firstName=firstName;
            return this;
        }
        public Builder withLastName(String lastName){
            applicant.lastName=lastName;
            return this;
        }
        public Builder withBirthday(LocalDate birthday){
            applicant.birthday=birthday;
            return this;
        }
        public Builder withDescription(String description){
            applicant.description=description;
            return this;
        }
        public Applicant build(){
            return applicant;
        }

    }

    @Override
    public String toString() {
        return "Applicant{" +
                "id=" + id +
                ", user=" + user +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", skills=" + skills +
                ", educations=" + educations +
                ", internships=" + internships +
                '}';
    }
}
