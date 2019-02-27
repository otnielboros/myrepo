package ro.yuhuu.backend.yubackend.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.yuhuu.backend.yubackend.controller.requests.UpdateInternshipRequestStatus;
import ro.yuhuu.backend.yubackend.exceptions.*;
import ro.yuhuu.backend.yubackend.model.Company;
import ro.yuhuu.backend.yubackend.model.Photo;
import ro.yuhuu.backend.yubackend.model.User;
import ro.yuhuu.backend.yubackend.service.GenericService;

@Api("CompanyController")
@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "*")
public class CompanyController {
    @Autowired
    private GenericService userService;

    @PreAuthorize("hasAuthority('COMPANY')")
    @GetMapping("/{companyId}/photo")
    ResponseEntity getCompanyPhoto(@PathVariable Long companyId) throws NotValidContactException, NotValidPhotoException {
        return new ResponseEntity(userService.getCompanyPhoto(companyId), HttpStatus.OK);
    }

    @GetMapping("/sorted/dimension/{nrItems}")
    ResponseEntity getSortedCompaniesByNoEmployees(@PathVariable Integer nrItems){
        return new ResponseEntity(userService.getSortedCompaniesByNoEmployees(nrItems),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @GetMapping("/internshipRequests")
    ResponseEntity getAllInternshipRequestsForCompany(){
        return new ResponseEntity(userService.getAllInternshipRequestsForCompany(),HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('COMPANY')")
    @PutMapping("/{internshipRequestId}/status")
    public @ResponseBody
    ResponseEntity updateInternshipRequestStatus(@PathVariable Long internshipRequestId, @RequestBody UpdateInternshipRequestStatus updateInternshipRequestStatus) throws NotValidInternshipRequestException, NotAllowedCompanyException {
        return new ResponseEntity(userService.updateInternshipRequestStatus(internshipRequestId,updateInternshipRequestStatus),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @DeleteMapping("/{companyId}/photo")
    ResponseEntity deleteCompanyPhoto(@PathVariable Long companyId) throws NotValidContactException, NotAllowedDeletingPhotoException {
        return new ResponseEntity(userService.deleteCompanyPhoto(companyId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @PostMapping("/{companyId}/photo")
    ResponseEntity createCompanyPhoto(@PathVariable Long companyId, @RequestParam("file") MultipartFile photoFile) throws NotAllowedApplicantException, NotValidImageUploadException, NotValidContactException, NotValidCompanyException {

        userService.createCompanyPhotoIfNotExist(companyId);
        return new ResponseEntity(userService.uploadPhotoForComapny(companyId, photoFile), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @PutMapping("/{companyId}/photo")
    ResponseEntity updateCompanyPhoto(@PathVariable Long companyId, @RequestParam("file") MultipartFile photoFile) throws NotAllowedApplicantException, NotValidImageUploadException, NotValidContactException, NotAllowedDeletingPhotoException, NotValidCompanyException {
        userService.deleteCompanyPhoto(companyId);
        return new ResponseEntity(userService.uploadPhotoForComapny(companyId, photoFile), HttpStatus.OK);
    }


    @GetMapping("/details/{id}")
    public @ResponseBody
    ResponseEntity getCompanyDetailsById(@PathVariable Long id) throws NotValidCompanyException, NotAllowedCompanyException {
        return new ResponseEntity(userService.getCompanyDetailsById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @PutMapping("/{companyId}/contact")
    public ResponseEntity updateCompanyContact(@PathVariable Long companyId, @RequestBody Company newCompany) throws NotValidCompanyException, NotAllowedApplicantException, NotValidContactException {
        return new ResponseEntity(userService.updateCompanyContactAndAddress(companyId, newCompany), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @PutMapping("/{companyId}/profile")
    public ResponseEntity updateCompanyDetails(@PathVariable Long companyId, @RequestBody Company newCompany) throws NotValidCompanyException, NotAllowedApplicantException {

        return new ResponseEntity(userService.updateCompanyProfile(companyId, newCompany), HttpStatus.OK);
    }

    @PutMapping("/viewscounter/{id}")
    public void increaseCompanyViewCounter(@PathVariable Long id) throws NotValidCompanyException {
        userService.incrementViewCounter(id);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @DeleteMapping("/internship/{internshipId}")
    public @ResponseBody
    ResponseEntity removeInternship(@PathVariable Long internshipId) throws NotValidInternshipException, UpdateException {
        return new ResponseEntity(userService.removeInternship(internshipId),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @PutMapping("/{id}/email")
    public @ResponseBody
    ResponseEntity updateCompanyEmail(@PathVariable Long id,@RequestBody User user) throws NotValidCompanyException, NotAllowedCompanyException, UpdateException {
        return new ResponseEntity(userService.updateCompanyEmail(id,user),HttpStatus.OK);
    }

    @GetMapping("/distance/{id}")
    public @ResponseBody
    ResponseEntity getDistanceFromUserToCompany(@PathVariable Long id) throws NotValidCompanyException, NotValidAddressException {
        return new ResponseEntity(userService.getDistanceFromUserToCompany(id),HttpStatus.OK);
    }

    @GetMapping("/coordinates/{id}")
    public @ResponseBody
    ResponseEntity getCoordinatesForCompany(@PathVariable Long id) throws NotValidCompanyException {
        return new ResponseEntity(userService.getCoordinatesForCompany(id),HttpStatus.OK);
    }

    @GetMapping("/all")
    public @ResponseBody
    ResponseEntity getAllCompanies(){
        return new ResponseEntity(userService.getAllCompanies(),HttpStatus.OK);
    }

    @ExceptionHandler(NotValidCompanyException.class)
    public @ResponseBody
    ResponseEntity handleNotValidCompanyException(NotValidCompanyException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidPhotoException.class)
    public @ResponseBody
    ResponseEntity handleNotValidPhotoException(NotValidPhotoException exception) {
        return new ResponseEntity(exception, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NotAllowedCompanyException.class)
    public @ResponseBody
    ResponseEntity handleNotAllowedCompanyException(NotAllowedCompanyException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidUserException.class)
    public @ResponseBody
    ResponseEntity handleNotValidUserException(NotValidUserException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidAddressException.class)
    public @ResponseBody
    ResponseEntity handleNotValidAddressException(NotValidAddressException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidInternshipException.class)
    public @ResponseBody
    ResponseEntity handleNotValidInternshipException(NotValidInternshipException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UpdateException.class)
    public @ResponseBody
    ResponseEntity handleUpdateException(UpdateException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidInternshipRequestException.class)
    public @ResponseBody
    ResponseEntity handleNotValidInternshipRequest(NotValidInternshipRequestException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidContactException.class)
    public @ResponseBody
    ResponseEntity handleNotValidContactException(NotValidContactException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidImageUploadException.class)
    public @ResponseBody
    ResponseEntity handleNotValidImageUploadException(NotValidImageUploadException exception) {
        return new ResponseEntity(exception, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NotAllowedApplicantException.class)
    public @ResponseBody
    ResponseEntity handleNotAllowedApplicantException(NotAllowedApplicantException exception) {
        return new ResponseEntity(exception, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
