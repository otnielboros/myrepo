package ro.yuhuu.backend.yubackend.model;



import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roleString")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private  int roleId;


    @Enumerated(EnumType.STRING)
    @Column (name = "roleString",unique = true, length = 128)
    private RoleString roleString;


    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "roles")
    @JsonIgnore
    List<User> users = new ArrayList<>();


    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public RoleString getRoleString() {
        return roleString;
    }

    public void setRoleString(RoleString roleString) {
        this.roleString = roleString;
    }

    @JsonIgnore
    public List<User> getUsers(){
        return users;
    }

    public void setUsers(List<User> users){
        this.users=users;
    }

    public void addUser(User user){
        this.users.add(user);
    }


    public static class Builder {
        private  int roleId;
        private RoleString roleString;
        List<User> users;

        public Builder(){
            this.users= new ArrayList<>();
        }
        public Builder withId(int roleId){
            this.roleId=roleId;
            return this;
        }
        public Builder withRoleString(RoleString roleString){
            this.roleString=roleString;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof Role){
                Role role = (Role) o;
                return this.roleString.toString().equals(role.getRoleString().toString());
            }
            return false;
        }

        public Builder withUsersList(List<User> users){
            this.users=users;
            return this;
        }
        public Builder addUser(User user){
            this.users.add(user);
            return this;
        }
        public Role build(){
            Role role = new Role();
            role.roleId=this.roleId;
            role.roleString=this.roleString;
            role.users=this.users;
            return role;
        }
    }
}
