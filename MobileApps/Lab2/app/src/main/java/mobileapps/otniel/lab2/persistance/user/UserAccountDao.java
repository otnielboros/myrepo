package mobileapps.otniel.lab2.persistance.user;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


@Dao
public interface UserAccountDao {

    @Insert
    void insert(UserAccount account);

    @Query("SELECT * FROM useraccounts WHERE useraccounts.userId LIKE :username")
    UserAccount getAccount(String username);

    @Delete
    void delete(UserAccount account);
}
