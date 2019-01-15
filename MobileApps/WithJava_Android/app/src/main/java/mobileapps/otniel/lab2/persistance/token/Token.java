package mobileapps.otniel.lab2.persistance.token;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
@Entity(tableName = "token")
public class Token {
    public Token(){

    }
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public Token(String access_token) {

        this.access_token = access_token;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public Token(@NonNull int id, String access_token) {
        this.id = id;
        this.access_token = access_token;
    }

    @PrimaryKey
    @NonNull
    int id;
    String access_token;

}
