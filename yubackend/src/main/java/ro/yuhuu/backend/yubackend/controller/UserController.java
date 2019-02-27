package ro.yuhuu.backend.yubackend.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.yuhuu.backend.yubackend.exceptions.NotAllowedApplicantException;
import ro.yuhuu.backend.yubackend.exceptions.NotValidApplicantException;
import ro.yuhuu.backend.yubackend.exceptions.NotValidCompanyException;
import ro.yuhuu.backend.yubackend.model.Applicant;
import ro.yuhuu.backend.yubackend.model.Company;
import ro.yuhuu.backend.yubackend.model.User;
import ro.yuhuu.backend.yubackend.service.GenericService;

@Api("UserController")
@SuppressWarnings("ALL")
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:63343")
public class UserController {

    @Autowired
    private GenericService genericService;
    @GetMapping("/user")
    public ResponseEntity getAuthenticatedUser(){
        User user =genericService.getAuthenticatedUser();
        return new ResponseEntity(user, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('COMPANY')")
    @GetMapping("/user/company/{id}")
    public ResponseEntity getAuthenticatedCompany(@PathVariable Long id) throws NotValidCompanyException {
        Company company = genericService.getCompanyByUserId(id);
        return new ResponseEntity(company, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('APPLICANT')")
    @GetMapping("/user/applicant/{id}")
    public ResponseEntity getAuthenticatedApplicant(@PathVariable Long id) throws NotValidApplicantException, NotAllowedApplicantException {
        Applicant applicant = genericService.getApplicantByUserId(id);
        return new ResponseEntity(applicant, HttpStatus.OK);
    }

    @ExceptionHandler(NotValidCompanyException.class)
    public @ResponseBody
    ResponseEntity handleNotValidCompanyException(NotValidCompanyException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NotValidApplicantException.class)
    public @ResponseBody
    ResponseEntity handleNotValidApplicantException(NotValidApplicantException exception) {
        return new ResponseEntity(exception,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NotAllowedApplicantException.class)
    public @ResponseBody
    ResponseEntity handleNotAllowedApplicantException(NotValidApplicantException exception){
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }


}
