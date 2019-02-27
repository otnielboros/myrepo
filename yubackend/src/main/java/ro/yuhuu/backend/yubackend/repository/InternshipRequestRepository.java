package ro.yuhuu.backend.yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.yuhuu.backend.yubackend.model.Applicant;
import ro.yuhuu.backend.yubackend.model.Internship;
import ro.yuhuu.backend.yubackend.model.InternshipRequest;

import java.util.Optional;

@Repository
public interface InternshipRequestRepository extends JpaRepository<InternshipRequest,Long> {
    Optional<InternshipRequest> findInternshipRequestByApplicantAndInternship(Applicant applicant,Internship internship);
}
