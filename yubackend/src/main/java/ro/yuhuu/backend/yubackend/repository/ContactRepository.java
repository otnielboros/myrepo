package ro.yuhuu.backend.yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.yuhuu.backend.yubackend.model.Applicant;
import ro.yuhuu.backend.yubackend.model.Contact;

import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {
     Optional<Contact> findByApplicantId(Long id);
     Optional<Contact> findByCompanyId(Long id);
}
