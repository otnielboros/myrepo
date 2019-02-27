package ro.yuhuu.backend.yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.yuhuu.backend.yubackend.model.Skill;

import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill,Long> {
    Optional<Skill> findByNameEquals(String name);
}


