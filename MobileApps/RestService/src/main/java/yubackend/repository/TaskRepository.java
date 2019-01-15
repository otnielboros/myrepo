package yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yubackend.model.Task;

public interface TaskRepository extends JpaRepository<Task,Integer> {
}
