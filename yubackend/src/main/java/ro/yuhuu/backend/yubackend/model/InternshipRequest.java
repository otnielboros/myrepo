package ro.yuhuu.backend.yubackend.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="applicant_internship_request")
public class InternshipRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "internship_request_id")
    private Long id;

    private String logoUrl;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private InternshipRequestStatus internshipRequestStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public Internship getInternship() {
        return internship;
    }

    public void setInternship(Internship internship) {
        this.internship = internship;
    }

    public InternshipRequestStatus getInternshipRequestStatus() {
        return internshipRequestStatus;
    }

    public void setInternshipRequestStatus(InternshipRequestStatus internshipRequestStatus) {
        this.internshipRequestStatus = internshipRequestStatus;
    }

    public static class Builder {

        private InternshipRequest internshipRequest;

        public Builder() {
            internshipRequest = new InternshipRequest();
        }

        public Builder withStatus(InternshipRequestStatus internshipRequestStatus) {
            internshipRequest.internshipRequestStatus = internshipRequestStatus;
            return this;
        }

        public Builder withInternship(Internship internship){
            internshipRequest.internship = internship;
            return this;
        }

        public Builder withApplicant(Applicant applicant){
            internshipRequest.applicant = applicant;
            return this;
        }

        public Builder withLogoUrl(String logoUrl){
            internshipRequest.logoUrl=logoUrl;
            return this;
        }

        public InternshipRequest build(){
            return internshipRequest;
        }
    }
}
