package yubackend.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yubackend.config.SecurityConfig;
import yubackend.model.Task;
import yubackend.model.User;
import yubackend.repository.TaskRepository;
import yubackend.repository.UserRepository;
import yubackend.service.GenericService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class GenericServiceImpl implements GenericService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;


    @PostConstruct
    private void loadData(){
        User u1=new User();
        u1.setUsername("user");
        u1.setPassword(SecurityConfig.passwordEncoder().encode("user"));
        userRepository.save(u1);

        Task t1=new Task("Lab - Mobile",10);
        Task t2=new Task("Lab - LFTC",5);
        Task t3=new Task("Lab - PPD",5);
        Task t4=new Task("Lab - DM",10);
        Task t5=new Task("Lab - APP",5);
        taskRepository.save(t1);
        taskRepository.save(t2);
        taskRepository.save(t3);
        taskRepository.save(t4);
        taskRepository.save(t5);

    }


    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public List<Task> findAllTasks() {
       return taskRepository.findAll();
    }

    @Override
    public Optional<Task> findTask(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task delete(int id) {
        Optional<Task> t=taskRepository.findById(id);
        if(t.isPresent()){
            Task t2=t.get();
            taskRepository.delete(t2);
            return t2;
        }
        return null;
    }


}
