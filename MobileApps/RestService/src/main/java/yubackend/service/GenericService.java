package yubackend.service;

import yubackend.model.Task;
import yubackend.model.User;

import java.util.List;
import java.util.Optional;

public interface GenericService {
    User findByUsername(String username);

    List<User> findAllUsers();

    User save(User user);

    List<Task> findAllTasks();

    Optional<Task> findTask(int id);

    Task save(Task task);

    Task delete(int id);

}
