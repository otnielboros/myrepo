package ro.yuhuu.backend.yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.yuhuu.backend.yubackend.model.Photo;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo,Long> {

    Optional<Photo> findPhotoByContact_Id(Long id);
}
