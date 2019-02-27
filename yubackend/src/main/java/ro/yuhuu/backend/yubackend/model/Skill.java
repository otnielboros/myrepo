package ro.yuhuu.backend.yubackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "skill")
public class Skill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="skill_id")
    private Long id;
    @Column (unique = true, length = 128)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "skills",fetch = FetchType.EAGER)
    private Set<Applicant> applicants = new HashSet<>();

    @JsonIgnore
    public Set<Applicant> getApplicants() {
        return applicants;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "skills",fetch = FetchType.EAGER)
    private Set<Internship> internships = new HashSet<>();

    @JsonIgnore
    public Set<Internship> getInternship(){
        return internships;
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
}
