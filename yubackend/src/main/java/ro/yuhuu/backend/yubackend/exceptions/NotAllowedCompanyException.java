package ro.yuhuu.backend.yubackend.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class NotAllowedCompanyException extends Exception{
    public NotAllowedCompanyException(String message){
        super(message);
    }
}

