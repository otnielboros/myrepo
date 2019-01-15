package mobileapps.otniel.lab2.persistance.task;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import mobileapps.otniel.lab2.viewobjects.Task;


@Dao
public interface TaskDao {
    @Insert
    void insert(Task taskPersistance);

    @Query("SELECT * FROM tasks WHERE tasks.id =:id")
    Task getTask(int id);
    @Query("SELECT * FROM tasks")
    List<Task> getTasks();

    @Query("UPDATE tasks SET description=:description,importance=:importance WHERE tasks.id=:id")
    void update(int id,String description,int importance);

    @Delete
    void delete(Task task);
}
