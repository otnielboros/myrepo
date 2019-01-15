package mobileapps.otniel.lab2.persistance.task;

import java.util.List;
import java.util.concurrent.ExecutionException;

import mobileapps.otniel.lab2.viewobjects.Task;

public class TaskRepository {
    private final TaskDao taskDao;
    private static TaskRepository instance;
    //private LiveData<Task> userAccountLiveData;
    private TaskRepository(TaskDao taskDao)
    {
        this.taskDao = taskDao;
    }

    public static TaskRepository getInstance(TaskDao taskDao)
    {
        if(instance == null)
        {
            instance = new TaskRepository(taskDao);
        }
        return instance;
    }

    public void insertTask(int id,String description,int importance)
    {
        Task taskPersistance = new Task(id, description,importance);
//        Task taskPersistance=new Task();
//        taskPersistance.setDescription(description);
//        taskPersistance.setId(id);
//        taskPersistance.setImportance(importance);
        taskDao.insert(taskPersistance);
    }

    public Task getTask(int id){
        return taskDao.getTask(id);
    }

    public List<Task> getAll() throws ExecutionException, InterruptedException {
        return new getAllAsyncTask(taskDao).execute().get();
    }

    public void updateTask(Task task){
        taskDao.update(task.getId(),task.getDescription(),task.getImportance());
    }

    public void update(List<Task> tasks){
        for(Task t:tasks) {
            taskDao.update(t.getId(), t.getDescription(), t.getImportance());
        }
    }

    public void delete(Task task){
        taskDao.delete(task);
    }

    private static class getAllAsyncTask extends android.os.AsyncTask<Void, Void, List<Task>> {

        private TaskDao mAsyncTaskDao;


        getAllAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<Task> doInBackground(Void... voids) {

            return mAsyncTaskDao.getTasks();
        }
    }
}
