package ro.yuhuu.backend.yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.yuhuu.backend.yubackend.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

}
