package ro.yuhuu.backend.yubackend.exceptions;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties({"stackTrace","cause","suppressed","localizedMessage"})
public class RegisterCompanyException extends Exception{
    private List<String> fieldList;
    private List<String> messages;


    public RegisterCompanyException(String message){
        super(message);
    }
    public RegisterCompanyException(String message, List<String> fieldList, List<String> messages)
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
