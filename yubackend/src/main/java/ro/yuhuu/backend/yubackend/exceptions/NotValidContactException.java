package ro.yuhuu.backend.yubackend.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class NotValidContactException extends Exception {

    public NotValidContactException(String message){
        super(message);
    }

}


