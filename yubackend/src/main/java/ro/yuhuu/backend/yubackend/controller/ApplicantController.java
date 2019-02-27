package ro.yuhuu.backend.yubackend.controller;

import io.swagger.annotations.Api;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.yuhuu.backend.yubackend.exceptions.*;
import ro.yuhuu.backend.yubackend.model.*;
import ro.yuhuu.backend.yubackend.service.GenericService;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
@Api("ApplicantController")
@RestController
@RequestMapping("/api/applicant")
@CrossOrigin(origins = "*")
public class ApplicantController {
    @Autowired
    private GenericService userService;

    @GetMapping("/details/{id}")
    public @ResponseBody ResponseEntity getApplicantDetails(@PathVariable Long id) throws NotValidApplicantException {
        return new ResponseEntity(userService.getApplicantByApplicantId(id), HttpStatus.OK);
    }

    @GetMapping("/lastregistered/{number}")
    public @ResponseBody ResponseEntity getLastXRegisteredApplicants(@PathVariable Long number) throws NotValidApplicantException {
        return new ResponseEntity(userService.getLastXRegisteredApplicants(number), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @GetMapping("/internshipRequests")
    public @ResponseBody
    ResponseEntity getAllInternshipRequests(){
        return new ResponseEntity(userService.getAllInternshipRequestsForApplicant(),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @PutMapping("/{id}/education")
    public @ResponseBody
    ResponseEntity updateApplicantEducation(@PathVariable Long id, @RequestBody Education education) throws NotValidApplicantException, NotAllowedApplicantException {
        return new ResponseEntity(userService.updateApplicantEducation(id,education),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @PutMapping("/{id}")
    public @ResponseBody
    ResponseEntity updateApplicant(@PathVariable Long id, @RequestBody Applicant applicant) throws NotValidApplicantException, NotAllowedApplicantException {
        return new ResponseEntity(userService.updateApplicant(id,applicant), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @PutMapping("/{id}/contact")
    public @ResponseBody
    ResponseEntity updateApplicantContact(@PathVariable Long id, @RequestBody Contact contact) throws NotValidApplicantException, NotAllowedApplicantException {
        return new ResponseEntity(userService.updateApplicantContact(id,contact), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @PutMapping("/{id}/email")
    public @ResponseBody
    ResponseEntity updateApplicantEmail(@PathVariable Long id,@RequestBody User user) throws NotValidApplicantException, NotAllowedApplicantException, UpdateException {
        return new ResponseEntity(userService.updateApplicantEmail(id,user),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @DeleteMapping("/{id}/skills/{skillId}")
    ResponseEntity deleteApplicantSkill(@PathVariable Long id,@PathVariable Long skillId) throws NotAllowedApplicantException, NotValidApplicantException, NotValidSkillException {
        return new ResponseEntity(userService.deleteApplicantSkill(id,skillId),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @DeleteMapping("/{id}/educations/{educationId}")
    ResponseEntity deleteApplicantEducation(@PathVariable Long id,@PathVariable Long educationId) throws NotAllowedApplicantException, NotValidApplicantException, NotValidEducationException {
        return new ResponseEntity(userService.deleteApplicantEducation(id,educationId),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @PostMapping("/{applicantId}/cv")
    ResponseEntity createApplicantCV(@PathVariable Long applicantId, @RequestParam("file") MultipartFile cvFile) throws NotValidContactException, NotValidCVException, NotValidCVUploadException, WrongFormatException {
        userService.checkCVIsPDF(cvFile.getOriginalFilename());
        userService.createApplicantCVIfNotExist(applicantId);
        return new ResponseEntity(userService.uploadApplicantCV(applicantId, cvFile), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @DeleteMapping("/{applicantId}/cv")
    ResponseEntity deleteApplicantCV(@PathVariable Long applicantId) throws NotValidContactException, NotValidCVException, NotAllowedDeletingCVException {
        return new ResponseEntity(userService.deleteApplicantCV(applicantId), HttpStatus.OK);
    }
    
    @GetMapping("/{applicantId}/cv")
    ResponseEntity getApplicantCV(@PathVariable Long applicantId) throws NotValidContactException, NotValidCVException {
        return new ResponseEntity(userService.getApplicantCV(applicantId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @PutMapping("/{applicantId}/cv")
    ResponseEntity updateApplicantCV(@PathVariable Long applicantId, @RequestParam("file") MultipartFile cvFile) throws NotValidContactException, NotValidCVException, NotValidCVUploadException, NotAllowedDeletingCVException, WrongFormatException {
        userService.checkCVIsPDF(cvFile.getOriginalFilename());
        userService.deleteApplicantCV(applicantId);
        return new ResponseEntity(userService.uploadApplicantCV(applicantId, cvFile), HttpStatus.OK);
    }


    @GetMapping("/{applicantId}/photo")
    ResponseEntity getApplicantPhoto(@PathVariable Long applicantId) throws NotAllowedApplicantException, NotValidApplicantException, NotValidContactException, NotValidPhotoException {
        return new ResponseEntity(userService.getApplicantPhoto(applicantId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @DeleteMapping("/{applicantId}/photo")
    ResponseEntity deleteApplicantPhoto(@PathVariable Long applicantId) throws NotValidContactException, NotAllowedDeletingPhotoException {
        return new ResponseEntity(userService.deleteApplicantPhoto(applicantId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @DeleteMapping("/internshipRequest/{internshipRequestId}/cancel")
    ResponseEntity cancelInternshipRequest(@PathVariable Long internshipRequestId) throws NotAllowedApplicantException, NotValidInternshipRequestException {
        return new ResponseEntity(userService.cancelInternshipRequest(internshipRequestId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @PostMapping("/{applicantId}/photo")
    ResponseEntity createApplicantPhoto(@PathVariable Long applicantId, @RequestParam("file") MultipartFile photoFile) throws NotAllowedApplicantException, NotValidApplicantException, NotValidImageUploadException, NotValidContactException, NotValidPhotoException {
        userService.createApplicantPhotoIfNotExist(applicantId);
        Photo photo = userService.uploadPhotoForApplicant(applicantId, photoFile);

        return new ResponseEntity(photo, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @PutMapping("/{applicantId}/photo")
    ResponseEntity updateApplicantPhoto(@PathVariable Long applicantId, @RequestParam("file") MultipartFile photoFile) throws NotAllowedApplicantException, NotValidApplicantException, NotValidImageUploadException, NotValidContactException, NotAllowedDeletingPhotoException, NotValidPhotoException {
        userService.deleteApplicantPhoto(applicantId);
        return new ResponseEntity(userService.uploadPhotoForApplicant(applicantId, photoFile), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @PutMapping("/{id}/skill")
    public @ResponseBody
    ResponseEntity updateApplicantSkills(@PathVariable Long id, @RequestBody Skill skill) throws NotValidApplicantException, NotAllowedApplicantException, NotValidSkillException {
        return new ResponseEntity(userService.updateApplicantSkills(id,skill),HttpStatus.OK);
    }


    @GetMapping("/{id}/skills")
    public @ResponseBody
    Set<Skill> getApplicantSkills(@PathVariable Long id) throws NotValidApplicantException, NotAllowedApplicantException {
        return userService.getApplicantSkills(id);
    }

    @GetMapping("/{id}/educations")
    public @ResponseBody
    List<Education> getApplicantEducations(@PathVariable Long id) throws NotValidApplicantException, NotAllowedApplicantException {
        return userService.getApplicantEducations(id);
    }

    @ExceptionHandler(NotValidPhotoException.class)
    public @ResponseBody
    ResponseEntity handleNotValidPhotoException(NotValidPhotoException exception) {
        return new ResponseEntity(exception, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NotValidApplicantException.class)
    public @ResponseBody
    ResponseEntity handleNotValidApplicantException(NotValidApplicantException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotAllowedApplicantException.class)
    public @ResponseBody
    ResponseEntity handleNotAllowedApplicantException(NotAllowedApplicantException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidSkillException.class)
    public @ResponseBody
    ResponseEntity handleNotNotValidSkillException(NotValidSkillException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidEducationException.class)
    public @ResponseBody
    ResponseEntity handleNotValidEducationException(NotValidEducationException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UpdateException.class)
    public @ResponseBody
    ResponseEntity handleUpdateException(UpdateException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WrongFormatException.class)
    public @ResponseBody
    ResponseEntity handleWrongFormatException(WrongFormatException exception) {
        return new ResponseEntity(exception, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(NotValidContactException.class)
    public @ResponseBody
    ResponseEntity handleNotValidContactException(NotValidContactException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidImageUploadException.class)
    public @ResponseBody
    ResponseEntity handleNotValidImageUploadException(NotValidImageUploadException exception) {
        return new ResponseEntity(exception, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(NotValidCVUploadException.class)
    public @ResponseBody
    ResponseEntity handleNotValidCVUploadException(NotValidCVUploadException exception) {
        return new ResponseEntity(exception, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NotAllowedDeletingCVException.class)
    public @ResponseBody
    ResponseEntity handleNotAllowedDeletingCVException(NotAllowedDeletingCVException exception) {
        return new ResponseEntity(exception, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NotValidCVException.class)
    public @ResponseBody
    ResponseEntity handleNotValidCVException(NotValidCVException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotAllowedDeletingPhotoException.class)
    public @ResponseBody
    ResponseEntity handleNotAllowedDeletingPhotoException(NotAllowedDeletingPhotoException exception) {
        return new ResponseEntity(exception, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NotValidInternshipRequestException.class)
    public @ResponseBody
    ResponseEntity handleNotValidInternshipRequest(NotValidInternshipRequestException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }


}
