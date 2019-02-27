package ro.yuhuu.backend.yubackend.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class UpdateException extends Exception{
    public UpdateException(String message){
        super(message);
    }
}

