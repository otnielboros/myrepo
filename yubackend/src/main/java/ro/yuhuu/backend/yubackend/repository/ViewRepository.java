package ro.yuhuu.backend.yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.yuhuu.backend.yubackend.model.Company;
import ro.yuhuu.backend.yubackend.model.User;
import ro.yuhuu.backend.yubackend.model.ViewsCounter;

import java.util.Optional;

@Repository
public interface ViewRepository extends JpaRepository<ViewsCounter,Long> {
    Optional<ViewsCounter> findViewByUserAndCompany(User user, Company company);
}
