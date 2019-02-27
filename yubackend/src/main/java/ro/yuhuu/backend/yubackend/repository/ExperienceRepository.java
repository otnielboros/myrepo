package ro.yuhuu.backend.yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.yuhuu.backend.yubackend.model.Experience;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience,Long> {
}
