package yubackend.model;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TaskPage implements Serializable {
    private Map<Integer,Task> tasks;

    public TaskPage(Map<Integer,Task> tasks) {
        this.tasks = tasks;
    }

    public TaskPage(){

    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<Integer, Task> tasks) {
        this.tasks = tasks;
    }
}
