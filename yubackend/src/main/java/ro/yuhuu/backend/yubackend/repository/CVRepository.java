package ro.yuhuu.backend.yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.yuhuu.backend.yubackend.model.CV;

import java.util.Optional;

public interface CVRepository extends JpaRepository<CV,Long> {

    Optional<CV> findCVByContact_Id(Long contactId);
}
