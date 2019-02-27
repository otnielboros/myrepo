package ro.yuhuu.backend.yubackend.controller.requests;

import ro.yuhuu.backend.yubackend.model.Company;
import ro.yuhuu.backend.yubackend.model.Internship;

import java.io.Serializable;


public class CreateInternshipRequest implements Serializable {
    private Company company;
    private Internship internship;

    public Company getCompany() {
        return company;
    }

    public Internship getInternship() {
        return internship;
    }
}
