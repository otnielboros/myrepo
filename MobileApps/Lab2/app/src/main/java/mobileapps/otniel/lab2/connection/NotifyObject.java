package mobileapps.otniel.lab2.connection;

import java.io.Serializable;

import mobileapps.otniel.lab2.viewobjects.Task;

public class NotifyObject implements Serializable {
    private String operation;
    private Task task;

    public NotifyObject() {
    }

    public NotifyObject(String operation, Task task) {
        this.operation = operation;
        this.task = task;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
