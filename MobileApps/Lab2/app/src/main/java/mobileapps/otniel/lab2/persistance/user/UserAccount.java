package mobileapps.otniel.lab2.persistance.user;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "useraccounts")
public class UserAccount {

//    public static final String TABLE_NAME = "useraccounts";

    @PrimaryKey
    @NonNull
    String userId;
    String password;

    public String getTokenCode() {
        return tokenCode;
    }

    public void setTokenCode(String tokenCode) {
        this.tokenCode = tokenCode;
    }

    String tokenCode;


    public UserAccount()
    {

    }
    public UserAccount(String username, String pwd)
    {
        this.userId = username;
        this.password = pwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }





}

