package ro.yuhuu.backend.yubackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bouncycastle.cert.ocsp.Req;
import ro.yuhuu.backend.yubackend.service.CommentIdComparator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.time.LocalDate;

@Entity
@Table(name = "internship")
public class Internship implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internship_id")
    private Long id;
    private Boolean active;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate deadline;
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;
    private Integer freeSpots;
    @Enumerated(EnumType.STRING)
    private InternshipStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "internship",fetch = FetchType.EAGER)
    private Set<InternshipRequest> internshipRequests = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinTable(name = "internship_tag",
            joinColumns = @JoinColumn(name = "internship_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();


    public void addTag(Tag tag){
        this.tags.add(tag);
        tag.getInternships().add(this);
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;


    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinTable(name = "internship_skill",
            joinColumns = @JoinColumn(name = "internship_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinTable(name = "internship_attribute",
            joinColumns = @JoinColumn(name = "internship_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private Set<Attribute> attributes = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinTable(name = "internship_requirement",
            joinColumns = @JoinColumn(name = "internship_id"),
            inverseJoinColumns = @JoinColumn(name = "requirement_id")
    )
    private Set<Requirement> requirements  = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "internships",fetch = FetchType.EAGER)
    private Set<Applicant> applicants = new HashSet<>();

    @JsonIgnore
    public Set<Applicant> getApplicants() {
        return applicants;
    }

    @JsonIgnore
    @OneToMany(
            mappedBy = "internship",
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER
    )
    private Set<Comment> comments = new TreeSet<>(new CommentIdComparator());

    public Set<InternshipRequest> getInternshipRequests() {
        return internshipRequests;
    }

    public void removeInternshipRequest(InternshipRequest internshipRequest){
        this.internshipRequests.remove(internshipRequest);
    }

    public void removeApplicant(Applicant applicant){
        this.applicants.remove(applicant);
    }

    public void setInternshipRequests(Set<InternshipRequest> internshipRequests) {
        this.internshipRequests = internshipRequests;
    }

    public void setApplicants(Set<Applicant> applicants) {
        this.applicants = applicants;
    }


    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setInternship(this);
    }

    public void removeComment(Comment comment){
        this.comments.remove(comment);
        comment.setInternship(null);
        comment.getCreator().removeComment(comment);
    }


    public Set<Comment> getComments() {
        return comments;
    }

    public void addSkill(Skill skill){
        this.skills.add(skill);
        skill.getInternship().add(this);
    }

    public void addAttribute(Attribute attribute){
        this.attributes.add(attribute);
        attribute.getInternships().add(this);
    }

    public void addRequirement(Requirement requirement){
        this.requirements.add(requirement);
        requirement.getInternships().add(this);
    }

    public void removeRequirement(Requirement requirement){
        this.requirements.remove(requirement);
        requirement.getInternships().remove(this);
    }

    public void removeAttribute(Attribute attribute){
        this.attributes.remove(attribute);
        attribute.getInternships().remove(this);
    }

    public void removeSkill(Skill skill){
        this.skills.remove(skill);
        skill.getInternship().remove(this);
    }

    public void addAllTags(List<Tag> tags){
        for (Tag tag:tags) {
            this.tags.add(tag);
            tag.getInternships().add(this);
        }
    }
    public void removeTag(Tag tag){
        this.tags.remove(tag);
        tag.getInternships().remove(this);
    }

    public InternshipStatus getStatus() {
        return status;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public Integer getFreeSpots() {
        return freeSpots;
    }

    public void setFreeSpots(Integer freeSpots) {
        this.freeSpots = freeSpots;
    }

    public void setStatus(InternshipStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Skill> getSkills(){
        return this.skills;
    }

    public Set<Requirement> getRequirements(){
        return this.requirements;
    }

    public Set<Tag> getTags(){
        return this.tags;
    }

    public Set<Attribute> getAttributes(){
        return this.attributes;
    }

    public static class Builder{

        private Internship internship;

        public Builder(){
            internship = new Internship();
        }
        public Builder withId(Long id){
            internship.id=id;
            return this;
        }

        public Builder withStatus(InternshipStatus status){
            internship.status=status;
            return this;
        }
        public Builder withFreeSpots(Integer freeSpots){
            internship.freeSpots=freeSpots;
            return this;
        }
        public Builder withEmploymentType(EmploymentType employmentType){
            internship.employmentType=employmentType;
            return this;
        }

        public Builder withDeadline(LocalDate deadline){
            internship.deadline =  deadline;
            return this;
        }

        public Builder withCompany(Company company){
            internship.company=company;
            return this;
        }
        public Builder isActive(boolean active){
            internship.active=active;
            return this;
        }
        public Builder withTitle(String title){
            internship.title=title;
            return this;
        }
        public Builder withDescription(String description){
            internship.description=description;
            return this;
        }
        public Builder withStartDate(LocalDate date){
            internship.startDate=date;
            return this;
        }
        public Builder withEndDate(LocalDate date){
            internship.endDate=date;
            return this;
        }
        public Builder addSkill(Skill skill){
            internship.addSkill(skill);
            return this;
        }
        public Builder addTag(Tag tag){
            internship.addTag(tag);
            return this;
        }
        public Builder addRequirements(Requirement requirement){
            internship.addRequirement(requirement);
            return this;
        }
        public Builder addAttribute(Attribute attribute){
            internship.addAttribute(attribute);
            return this;
        }

        public Internship build(){
            return internship;
        }
    }


}