package ro.yuhuu.backend.yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.yuhuu.backend.yubackend.model.Role;
import ro.yuhuu.backend.yubackend.model.RoleString;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleStringEquals(RoleString roleString);
}
