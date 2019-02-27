package ro.yuhuu.backend.yubackend.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class NotValidInternshipException extends Exception{
    public NotValidInternshipException(String message){
        super(message);
    }
}
