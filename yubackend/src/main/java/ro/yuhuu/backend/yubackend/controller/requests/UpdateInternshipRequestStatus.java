package ro.yuhuu.backend.yubackend.controller.requests;

import ro.yuhuu.backend.yubackend.model.InternshipRequestStatus;

public class UpdateInternshipRequestStatus {
    private InternshipRequestStatus internshipRequestStatus;
    private String subject;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public InternshipRequestStatus getInternshipRequestStatus() {
        return internshipRequestStatus;
    }

    public void setInternshipRequestStatus(InternshipRequestStatus internshipRequestStatus) {
        this.internshipRequestStatus = internshipRequestStatus;
    }
}
