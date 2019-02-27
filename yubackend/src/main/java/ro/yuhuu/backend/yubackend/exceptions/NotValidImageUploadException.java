package ro.yuhuu.backend.yubackend.exceptions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class NotValidImageUploadException extends Exception {

    public NotValidImageUploadException(String message){
        super(message);
    }
}
