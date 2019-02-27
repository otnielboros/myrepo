package ro.yuhuu.backend.yubackend.validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.yuhuu.backend.yubackend.controller.requests.CreateInternshipRequest;
import ro.yuhuu.backend.yubackend.exceptions.RegisterApplicantException;
import ro.yuhuu.backend.yubackend.model.Internship;
import ro.yuhuu.backend.yubackend.service.GenericService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class RegisterInternshipValidator {

    @Autowired
    private GenericService userService;

    @Before("execution(* ro.yuhuu.backend.yubackend.controller.InternshipController.createInternship(..))")
    public void validateRegistration(JoinPoint joinPoint) throws RegisterApplicantException {
        List<String> fieldList = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            CreateInternshipRequest createInternshipRequest = mapper.convertValue(joinPoint.getArgs()[0], CreateInternshipRequest.class);
            Internship internship = createInternshipRequest.getInternship();

            if(!checkInternshipTitle(internship.getTitle())){
                fieldList.add("title");
                messages.add("Not valid title");
            }


            if (!checkDate(internship.getStartDate(),internship.getEndDate())){
                fieldList.add("start_date,end_date");
                messages.add("End date cannot be before start date!");
            }

        }
        catch (Exception ignored){
            throw new RegisterApplicantException("Not valid registration");
        }

        if(fieldList.size()>0 || messages.size()>0){
            throw new RegisterApplicantException("Not valid registration",fieldList,messages);
        }
    }

    private boolean checkInternshipTitle(String title){
        return title != null && title.length() <= 128;
    }


    private boolean checkDate(LocalDate date1,LocalDate date2){
        if (date1.isAfter(date2))
            return false;
        return true;
    }
}
