package ro.yuhuu.backend.yubackend.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.yuhuu.backend.yubackend.controller.requests.RegisterApplicantRequest;
import ro.yuhuu.backend.yubackend.controller.requests.RegisterCompanyRequest;
import ro.yuhuu.backend.yubackend.exceptions.NotValidCompanyException;
import ro.yuhuu.backend.yubackend.exceptions.RegisterApplicantException;
import ro.yuhuu.backend.yubackend.exceptions.RegisterCompanyException;
import ro.yuhuu.backend.yubackend.model.*;
import ro.yuhuu.backend.yubackend.repository.AddressRepository;
import ro.yuhuu.backend.yubackend.repository.ContactRepository;
import ro.yuhuu.backend.yubackend.service.GenericService;

@Api("RegisterController")
@SuppressWarnings("ALL")
@RestController
public class RegisterController {

    @Autowired
    private GenericService userService;

    @PostMapping(value = "/register/applicant")
    public ResponseEntity registerApplicant(@RequestBody RegisterApplicantRequest registerApplicantRequest){
        Applicant applicantResult =
        userService.registerApplicant(
                registerApplicantRequest.getUser(),
                registerApplicantRequest.getApplicant()
        );
        if(applicantResult==null)
            return new ResponseEntity(HttpStatus.CONFLICT);
        return new ResponseEntity(applicantResult,HttpStatus.CREATED);
    }

    @PostMapping(value = "/register/company")
    public ResponseEntity registerCompany(@RequestBody RegisterCompanyRequest registerCompanyRequest) throws NotValidCompanyException {
        Company companyResult =
                userService.registerCompany(
                        registerCompanyRequest.getUser(),
                        registerCompanyRequest.getCompany()
                );
        if(companyResult==null)
            return new ResponseEntity(HttpStatus.CONFLICT);
        return new ResponseEntity(companyResult,HttpStatus.CREATED);
    }


    @PostMapping(value = "/register/checkusername")
    public ResponseEntity checkUsername(@RequestBody User user){
        boolean exists = userService.checkUsernameExists(user);
        return new ResponseEntity(exists,HttpStatus.OK);
    }
    @PostMapping(value = "/register/checkemail")
    public ResponseEntity checkEmail(@RequestBody User user){
        boolean exists = userService.checkEmailExists(user);
        return new ResponseEntity(exists,HttpStatus.OK);
    }

    @ExceptionHandler(RegisterApplicantException.class)
    public @ResponseBody ResponseEntity handleRegisterApplicantExceptions(RegisterApplicantException exception) {
        return new ResponseEntity(exception,HttpStatus.CONFLICT);
    }


    @ExceptionHandler(RegisterCompanyException.class)
    public @ResponseBody ResponseEntity handleRegisterCompanyExceptions(RegisterCompanyException exception) {
        return new ResponseEntity(exception,HttpStatus.CONFLICT);
    }
}
