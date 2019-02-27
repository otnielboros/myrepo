package ro.yuhuu.backend.yubackend.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class NotValidInternshipRequestException extends Exception{
    public NotValidInternshipRequestException(String message){
        super(message);
    }
}
