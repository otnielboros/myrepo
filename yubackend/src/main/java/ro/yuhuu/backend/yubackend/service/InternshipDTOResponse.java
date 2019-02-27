package ro.yuhuu.backend.yubackend.service;

import ro.yuhuu.backend.yubackend.model.Company;
import ro.yuhuu.backend.yubackend.model.Internship;
import ro.yuhuu.backend.yubackend.model.Skill;

import java.util.Set;

public class InternshipDTOResponse {
    private Internship internship;
    private Company company ;
    private Set<Skill> skills;

    public InternshipDTOResponse(Internship internship,Company company){
        this.company=company;
        this.internship=internship;
        skills = internship.getSkills();
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public void setInternship(Internship internship) {
        this.internship = internship;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Internship getInternship() {
        return internship;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public Company getCompany() {
        return company;
    }
}
