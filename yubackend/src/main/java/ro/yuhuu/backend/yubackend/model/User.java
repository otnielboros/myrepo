package ro.yuhuu.backend.yubackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "user_id")
    private Long id;

    @Column (unique = true, length = 128)
    private String username;

    @Column (unique = true, length = 128)
    private String email;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;


    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private Company company;


    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private Applicant applicant;

    private Boolean active;

    @ManyToMany(
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER
    )
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(
            mappedBy = "creator",
            cascade = CascadeType.DETACH,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Comment> comments = new HashSet<>();


    @ManyToMany(
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER
    )

    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setCreator(this);
    }

    public void removeComment(Comment comment){
        this.comments.remove(comment);
        comment.setCreator(null);
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "view",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id"))

    public void addRole(Role role){
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Company getCompany() {
        return company;
    }

    public User setCompany(Company company) {
        this.company = company;
        company.setUser(this);
        return this;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public User setApplicant(Applicant applicant) {
        this.applicant = applicant;
        applicant.setUser(this);
        return this;
    }


    public static class Builder{

        private User user;
        public Builder(){
            user = new User();
        }

        public Builder withId(Long id){
            user.id = id;
            return this;
        }
        public Builder withUsername(String username){
            user.username=username;
            return this;
        }
        public Builder withEmail(String email){
            user.email=email;
            return this;
        }
        public Builder withPassword(String password){
            user.password=password;
            return this;
        }
        public Builder isActive(Boolean active){
            user.active=active;
            return this;
        }
        public Builder withRolesList(Set<Role> roles){
            user.roles=roles;
            return this;
        }
        public Builder addRole(Role role){
            user.roles.add(role);
            role.addUser(user);
            return this;
        }
        public Builder setCompany(Company company){
            user.company=company;
            return this;
        }
        public Builder setApplicant(Applicant applicant){
            user.applicant=applicant;
            return this;
        }
        public User build(){
            return user;
        }

    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", roles=" + roles +
                '}';
    }
}

