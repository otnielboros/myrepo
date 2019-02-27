package ro.yuhuu.backend.yubackend.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class NotAllowedApplicantException extends Exception{
    public NotAllowedApplicantException(String message){
        super(message);
    }
}
