package mobileapps.otniel.lab2.viewobjects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "tasks")
public class Task implements Serializable{
    private String description;
    private int importance;

    @PrimaryKey
    @NonNull
    private int id;

    public Task(int id, String description, int importance){
        this.id=id;
        this.description=description;
        this.importance=importance;
    }

    public Task(){}

    public void setId( int id) {
        this.id = id;
    }

    public int getImportance() {
        return importance;
    }

    public String getDescription() {
        return description;
    }

    public int getId(){
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    @Override
    public String toString(){
        return description+ " "+ importance+"\n";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Task){
            Task t=(Task)obj;
            return this.getId()==t.getId() && this.getImportance()==t.getImportance() && this.getDescription().equals(t.getDescription());
        }
        return false;
    }
}