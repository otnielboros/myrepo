package ro.yuhuu.backend.yubackend.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name="viewscounter",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"user_id","company_id"})
)
public class ViewsCounter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "view_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    private LocalDate lastViewDay;

    public LocalDate getLastViewDay() {
        return lastViewDay;
    }

    public void setLastViewDay(LocalDate lastViewDay) {
        this.lastViewDay = lastViewDay;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static class Builder{
        private ViewsCounter viewsCounter;
        public Builder(){
            viewsCounter = new ViewsCounter();
        }
        public Builder withUser(User user){
            viewsCounter.user=user;
            return this;
        }
        public Builder withCompany(Company company){
            viewsCounter.company=company;
            return this;
        }
        public Builder withLocalDate(LocalDate localDate){
            viewsCounter.lastViewDay=localDate;
            return this;
        }
        public ViewsCounter build(){
            return viewsCounter;
        }
    }
}
