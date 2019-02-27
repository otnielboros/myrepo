package ro.yuhuu.backend.yubackend.controller.requests;

import ro.yuhuu.backend.yubackend.model.Applicant;
import ro.yuhuu.backend.yubackend.model.User;

import java.io.Serializable;

public class RegisterApplicantRequest implements Serializable {
    private User user;
    private Applicant applicant;


    public User getUser() {
        return user;
    }

    public Applicant getApplicant() {
        return applicant;
    }
}
