package ro.yuhuu.backend.yubackend.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class RegisterApplicantException extends Exception{
    private List<String> fieldList;
    private List<String> messages;


    public RegisterApplicantException(String message){
        super(message);
    }
    public RegisterApplicantException(String message, List<String> fieldList, List<String> messages)
    {
        super(message);
        this.fieldList=fieldList;
        this.messages=messages;
    }
    public List<String> getFieldList() {
        return fieldList;
    }

    public List<String> getMessages() {
        return messages;
    }
}
