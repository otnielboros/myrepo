package mobileapps.otniel.lab2.persistance.token;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Token.class},version = 2)
public abstract class TokenDatabase extends RoomDatabase {
    public abstract TokenDao tokenDao();
    public static TokenDatabase INSTANCE;

    public static TokenDatabase getAppDatabase(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TokenDatabase.class, "token_db").allowMainThreadQueries().build();

        }

        return INSTANCE;

    }

    public static void destroyInstance()
    {
        INSTANCE = null;
    }

}
