package ro.yuhuu.backend.yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.yuhuu.backend.yubackend.model.Requirement;

import java.util.Optional;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement,Long> {
    Optional<Requirement> findByNameEquals(String name);

}
