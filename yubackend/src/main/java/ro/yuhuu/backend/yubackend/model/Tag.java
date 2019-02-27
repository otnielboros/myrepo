package ro.yuhuu.backend.yubackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    private Long id;

    @Column (unique = true, length = 128)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags",fetch = FetchType.EAGER)
    private Set<Internship> internships = new HashSet<>();

    @JsonIgnore
    public Set<Internship> getInternships() {
        return internships;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
