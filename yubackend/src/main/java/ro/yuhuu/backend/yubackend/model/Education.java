package ro.yuhuu.backend.yubackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "education")
public class Education implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="education_id")
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String schoolTitle;
    private String degree;
    private String schoolLocation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "applicant_id")
    @JsonIgnore
    private Applicant applicant;


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

    public String getSchoolTitle() {
        return schoolTitle;
    }

    public void setSchoolTitle(String schoolTitle) {
        this.schoolTitle = schoolTitle;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSchoolLocation() {
        return schoolLocation;
    }

    public void setSchoolLocation(String schoolLocation) {
        this.schoolLocation = schoolLocation;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public static class Builder{
        private Long id;
        private String name;
        private LocalDate startDate;
        private LocalDate endDate;
        private String schoolTitle;
        private String degree;
        private String schoolLocation;
        private Applicant applicant;


        public Builder(){}
        public Builder(Long id){
            this.id=id;
        }
        public Builder withName(String name){
            this.name=name;
            return this;
        }

        public Builder withStartDate(LocalDate startDate){
            this.startDate=startDate;
            return this;
        }

        public Builder withEndDate(LocalDate endDate){
            this.endDate=endDate;
            return this;
        }

        public Builder withSchoolTitle(String schoolTitle){
            this.schoolTitle=schoolTitle;
            return this;
        }

        public Builder withDegree(String degree){
            this.degree=degree;
            return this;
        }

        public Builder withSchoolLocation(String schoolLocation){
            this.schoolLocation=schoolLocation;
            return this;
        }

        public Builder withApplicant(Applicant applicant){
            this.applicant=applicant;
            return this;
        }


        public Education build(){
            Education education = new Education();

            education.name = this.name;
            education.startDate = this.startDate;
            education.endDate = this.endDate;
            education.schoolTitle = this.schoolTitle;
            education.degree = this.degree;
            education.schoolLocation = this.schoolLocation;
            education.applicant = this.applicant;
            return education;
        }
    }
}
