package ro.yuhuu.backend.yubackend.exceptions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class NotValidUserException extends Exception{
    public NotValidUserException(String message){
        super(message);
    }
}
