package ro.yuhuu.backend.yubackend.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class NotValidAttributeException extends Exception{
    public NotValidAttributeException(String message){
        super(message);
    }
}

