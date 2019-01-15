package mobileapps.otniel.lab2.viewobjects;

import java.util.List;
import java.util.Map;

public class TaskPage {
    private Map<Integer,Task> tasks;

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<Integer, Task> tasks) {
        this.tasks = tasks;
    }
}
