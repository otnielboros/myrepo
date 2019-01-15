package yubackend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rx.Notification;
import rx.Observable;
import yubackend.config.MyMessageHandler;
import yubackend.model.NotifyObject;
import yubackend.model.Task;
import yubackend.model.TaskPage;
import yubackend.model.User;
import yubackend.service.GenericService;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ResourceController{
    @Autowired
    private GenericService userService;

    @RequestMapping(value ="/tasks",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('STANDARD_USER')")
    public ResponseEntity<TaskPage> getTasks(){
        List<Task> tasks=userService.findAllTasks();
        Map<Integer,Task> tasksMap=new HashMap<>();
        for(Task t:tasks){
            tasksMap.put(t.getId(),t);
        }
       //return new TaskPage(tasksMap);
        return new ResponseEntity(new TaskPage(tasksMap), HttpStatus.OK);
    }

    @RequestMapping(value="/tasks/{id}",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('STANDARD_USER')")
    public Task getTask(@PathVariable int id){
        Optional<Task> taskOptional=userService.findTask(id);
        if(taskOptional.isPresent())
            return taskOptional.get();
       return null;
    }


    @RequestMapping(value ="/tasks/{id}",method = RequestMethod.PATCH)
    @PreAuthorize("hasAuthority('STANDARD_USER')")
    public void updateTask(@PathVariable int id,@RequestBody Task task) throws IOException {
        System.out.println("UPDATE CALL");
        Optional<Task> currentOptionalTask=userService.findTask(id);
       if(currentOptionalTask.isPresent()){
           Task currentTask=currentOptionalTask.get();
           currentTask.setDescription(task.getDescription());
           currentTask.setImportance(task.getImportance());
           userService.save(currentTask);
       }
    }

    @RequestMapping(value ="/tasks/{id}",method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('STANDARD_USER')")
    public void deleteTask(@PathVariable int id){
        Task task1 = userService.delete(id);
        if (task1 != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                NotifyObject notifyObject=new NotifyObject("delete",task1);
                MyMessageHandler.sendVolunteerMessage(mapper.writeValueAsString(notifyObject));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @RequestMapping(value ="/tasks",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('STANDARD_USER')")
    public ResponseEntity createTask(@RequestBody Task task){
        Task task1 = userService.save(task);
        if (task1 != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                NotifyObject notifyObject=new NotifyObject("add",task1);
                MyMessageHandler.sendVolunteerMessage(mapper.writeValueAsString(notifyObject));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody User user){
       User user1 = userService.save(user);
       if(user1==null)
           return new ResponseEntity(HttpStatus.BAD_REQUEST);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
