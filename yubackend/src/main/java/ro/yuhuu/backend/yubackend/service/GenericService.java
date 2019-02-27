package ro.yuhuu.backend.yubackend.service;

import com.google.maps.model.LatLng;
import org.springframework.web.multipart.MultipartFile;
import ro.yuhuu.backend.yubackend.controller.requests.UpdateInternshipRequestStatus;
import ro.yuhuu.backend.yubackend.exceptions.*;
import ro.yuhuu.backend.yubackend.model.*;

import java.util.List;
import java.util.Set;

public interface GenericService {

    Applicant getApplicantByUserId(Long id) throws NotValidApplicantException, NotAllowedApplicantException;

    boolean checkUsernameExists(User user);

    Company getCompanyByUserId(Long id) throws NotValidCompanyException;

    boolean checkEmailExists(User user);

    Set<Internship> getAllInternshipsForCompany(Long id) throws NotValidCompanyException, NotAllowedCompanyException;

    Applicant registerApplicant(User user, Applicant applicant);

    Company registerCompany(User user, Company company) throws NotValidCompanyException;

    Internship createInternship(Company company,Internship internship) throws NotValidCompanyException;

    Company getCompanyDetailsById(Long id) throws NotValidCompanyException, NotAllowedCompanyException;

    Set<Internship> getInternshipsByTags(Set<Tag> tags);

    abstract User getAuthenticatedUser();

    Applicant updateApplicantEducation(Long id,Education education) throws NotValidApplicantException, NotAllowedApplicantException;

    Applicant updateApplicantSkills(Long id,Skill skill) throws NotValidApplicantException, NotAllowedApplicantException, NotValidSkillException;

    Set<Skill> getApplicantSkills(Long id) throws NotValidApplicantException, NotAllowedApplicantException;

    List<Education> getApplicantEducations(Long id) throws NotValidApplicantException, NotAllowedApplicantException;

    Applicant deleteApplicantSkill(Long id,Long skillId) throws NotValidApplicantException, NotAllowedApplicantException, NotValidSkillException;

    Photo uploadPhotoForApplicant(Long id, MultipartFile photoFile) throws NotValidApplicantException, NotAllowedApplicantException, NotValidImageUploadException, NotValidContactException, NotValidPhotoException;

    Photo getApplicantPhoto(Long applicantId) throws NotValidApplicantException, NotAllowedApplicantException, NotValidContactException, NotValidPhotoException;

    Photo deleteApplicantPhoto(Long applicantId) throws NotValidContactException, NotAllowedDeletingPhotoException;

    CV uploadApplicantCV(Long applicantId, MultipartFile cvFile) throws NotValidContactException, NotValidCVException, NotValidCVUploadException;

    CV getApplicantCV(Long applicantId) throws NotValidContactException, NotValidCVException;

    void checkCVIsPDF(String path) throws WrongFormatException;

    CV deleteApplicantCV(Long applicantId) throws NotValidContactException, NotValidCVException, NotAllowedDeletingCVException;

    Applicant deleteApplicantEducation(Long id,Long educationId) throws NotValidApplicantException, NotAllowedApplicantException, NotValidEducationException;

    List<Company> getAllCompanies();

    void incrementViewCounter(Long companyId) throws NotValidCompanyException;

    Double getDistanceFromUserToCompany(Long companyId) throws NotValidCompanyException, NotValidAddressException;

    List<Skill> getAllSkills();

    Applicant updateApplicant(Long id,Applicant applicant) throws NotValidApplicantException, NotAllowedApplicantException;

    Contact updateApplicantContact(Long id,Contact contact) throws NotValidApplicantException, NotAllowedApplicantException;

    boolean createApplicantCVIfNotExist(Long applicantId) throws NotValidContactException;

    boolean createApplicantPhotoIfNotExist(Long applicantId) throws NotValidContactException;

    boolean createCompanyPhotoIfNotExist(Long companyId) throws NotValidContactException;

    LatLng getCoordinatesForCompany(Long companyId) throws NotValidCompanyException;

    Company updateCompanyProfile(Long companyId, Company newCompany) throws NotValidCompanyException, NotAllowedApplicantException;

    User updateApplicantEmail(Long id, User user) throws NotValidApplicantException, NotAllowedApplicantException, UpdateException;

    List<Internship> getAllInternships();

    Company updateCompanyContactAndAddress(Long companyId, Company newCompany) throws NotValidCompanyException, NotAllowedApplicantException, NotValidContactException;

    User updateCompanyEmail(Long id,User user) throws NotValidCompanyException, NotAllowedCompanyException, UpdateException;

    Photo getInternshipLogo(Long id) throws NotValidInternshipException;

    Set<Skill> getAllSkillsForInternship(Long id) throws NotValidInternshipException;

    Set<Requirement> getAllRequirementsForInternship(Long id) throws NotValidInternshipException;

    Set<Attribute> getAllAttributesForInternship(Long id) throws NotValidInternshipException;

    Set<Tag> getAllTagsForInternship(Long id) throws NotValidInternshipException;

    Internship getInternshipDetailsById(Long id) throws NotValidInternshipException;

    Internship updateInternshipSimpleDetails(Long internshipId,Internship internship) throws NotValidInternshipException, UpdateException;

    Tag addTagToInternship(Long internshipId,Tag tag) throws NotValidInternshipException, UpdateException, NotValidTagException;

    Tag removeTagFromInternship(Long internshipId,Long tagId) throws NotValidInternshipException, UpdateException, NotValidTagException;

    Skill addSkillToInternship(Long internshipId,Skill skill) throws NotValidInternshipException, UpdateException, NotValidSkillException;

    Skill removeSkillFromInternship(Long internshipId,Long skillId) throws NotValidInternshipException, UpdateException, NotValidSkillException;

    Requirement addRequirementToInternship(Long internshipId,Requirement requirement) throws NotValidInternshipException, UpdateException;

    Requirement removeRequirementFromInternship(Long internshipId,Long requirementId) throws NotValidInternshipException, UpdateException, NotValidRequirementException;

    Attribute addAttributeToInternship(Long internshipId,Attribute attribute) throws NotValidInternshipException, UpdateException;

    Attribute removeAttributeFromInternship(Long internshipId,Long attributeId) throws NotValidInternshipException, UpdateException, NotValidAttributeException;

    Internship removeInternship(Long internshipId) throws NotValidInternshipException, UpdateException;

    Photo uploadPhotoForComapny(Long companyId, MultipartFile photoFile) throws NotValidCompanyException, NotAllowedApplicantException, NotValidContactException, NotValidImageUploadException;

    Photo getCompanyPhoto(Long companyId) throws NotValidContactException, NotValidPhotoException;

    Photo deleteCompanyPhoto(Long companyId) throws NotValidContactException, NotAllowedDeletingPhotoException;

    Comment addComment(Long internshipId,Comment comment) throws NotValidInternshipException;

    Comment addReplyComment(Comment comment,Long parentCommentId) throws NotValidCommentException;

    List<Comment> getAllCommentsForInternship(Long internshipId) throws NotValidInternshipException;

    Comment removeComment(Long commentId) throws NotValidCommentException, NotAllowedApplicantException;

    Comment likeComment(Long commentId) throws NotValidCommentException;

    Comment dislikeComment(Long commentId) throws NotValidCommentException;

    Company getCompanyByInternshipId(Long internshipId) throws NotValidInternshipException;

    void sendEmail(String destination, String subject, String content);

    InternshipRequest applyForInternship(Long internshipId) throws NotValidInternshipException, NotAllowedApplicantException;

    Set<Applicant> getApplicantsForSpecificInternship(Long internshipId) throws NotValidInternshipException, NotAllowedCompanyException;

    Boolean updateInternshipRequestStatus(Long internshipRequestId, UpdateInternshipRequestStatus updateInternshipRequestStatus) throws NotValidInternshipRequestException, NotAllowedCompanyException;

    Applicant getApplicantByApplicantId(Long applicantId) throws NotValidApplicantException;

    List<Internship> getLast7DaysInternshipsByTags(Set<Tag> tags);

    List<Company> getSortedCompaniesByNoEmployees(Integer nrItems);

    List<InternshipRequest> getAllInternshipRequestsForApplicant();

    List<InternshipRequest> getAllInternshipRequestsForCompany();

    Boolean cancelInternshipRequest(Long internshipRequestId) throws NotValidInternshipRequestException, NotAllowedApplicantException;

    List<InternshipDTOResponse> getAllInternshipDTOs();

    List<InternshipRequest> getAllInternshipRequestsForSpecificInternship(Long internshipId) throws NotValidInternshipException, NotAllowedCompanyException;

    List<Applicant> getLastXRegisteredApplicants(Long number);
}
