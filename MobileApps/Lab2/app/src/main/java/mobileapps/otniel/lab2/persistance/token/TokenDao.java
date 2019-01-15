package mobileapps.otniel.lab2.persistance.token;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import mobileapps.otniel.lab2.viewobjects.Task;
import retrofit2.http.GET;

@Dao
public interface TokenDao {
    @Query("UPDATE token SET access_token=:token WHERE id=:id")
    void update(String token,int id);

    @Insert
    void insert(Token token);

    @Query("SELECT * FROM token WHERE token.id =:id")
    Token getToken(int id);


}
