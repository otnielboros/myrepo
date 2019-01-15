package mobileapps.otniel.lab2.viewobjects;

import java.io.Serializable;
import java.util.List;


public class Page{
    private int number;
    private List<Task> tasks;

    public Page(){

    }

    public Page(int number,List<Task> tasks){
        this.number=number;
        this.tasks=tasks;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getNumber() {
        return number;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return "Page{" +
                "number=" + number +
                ", tasks=" + tasks +
                '}';
    }
}