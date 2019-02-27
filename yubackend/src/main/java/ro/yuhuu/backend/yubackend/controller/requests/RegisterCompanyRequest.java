package ro.yuhuu.backend.yubackend.controller.requests;

import ro.yuhuu.backend.yubackend.model.Company;
import ro.yuhuu.backend.yubackend.model.User;

import java.io.Serializable;

public class RegisterCompanyRequest implements Serializable {
    private User user;
    private Company company;


    public User getUser() {
        return user;
    }

    public Company getCompany() {
        return company;
    }
}
