package ro.yuhuu.backend.yubackend.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.yuhuu.backend.yubackend.controller.requests.CreateInternshipRequest;
import ro.yuhuu.backend.yubackend.exceptions.*;
import ro.yuhuu.backend.yubackend.model.*;
import ro.yuhuu.backend.yubackend.service.GenericService;

import java.util.Set;

@Api("InternshipController")
@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/api/internship")
@CrossOrigin(origins = "http://localhost:63343")
public class InternshipController {
    @Autowired
    private GenericService userService;

    @GetMapping("/all/company/{id}")
    public @ResponseBody
    ResponseEntity getAllInternshipsForCompany(@PathVariable Long id) throws NotValidCompanyException, NotAllowedCompanyException {
            return new ResponseEntity(userService.getAllInternshipsForCompany(id),HttpStatus.OK);
    }

    @GetMapping("/{internshipId}/company")
    public @ResponseBody
    ResponseEntity getCompanyByInternshipId(@PathVariable Long internshipId) throws NotValidInternshipException {
        return new ResponseEntity(userService.getCompanyByInternshipId(internshipId),HttpStatus.OK);
    }


    @GetMapping("/details/{id}")
    public @ResponseBody
    ResponseEntity getInternshipDetails(@PathVariable Long id) throws NotValidInternshipException{
        return new ResponseEntity(userService.getInternshipDetailsById(id),HttpStatus.OK);
    }

    @PutMapping("/sendemail")
    public @ResponseBody ResponseEntity sendEmail(){

        userService.sendEmail("chise_b@yahoo.com", "test", "This is a simple test");
        return new ResponseEntity(HttpStatus.OK);
    }

    @ExceptionHandler(NotValidCompanyException.class)
    public @ResponseBody
    ResponseEntity handleNotValidCompanyException(NotValidCompanyException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @PostMapping("/all/tags")
    public @ResponseBody
    ResponseEntity getAllInternshipsByTags(@RequestBody Set<Tag> tags){
        return new ResponseEntity(userService.getInternshipsByTags(tags),HttpStatus.OK);
    }

    @GetMapping("/allinternships")
    public @ResponseBody
    ResponseEntity findAllInternships(){
        return new ResponseEntity(userService.getAllInternshipDTOs(),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @GetMapping("/{internshipId}/internshipRequests")
    ResponseEntity getAllInternshipRequestsForSpecificInternship(@PathVariable Long internshipId) throws NotValidInternshipException, NotAllowedCompanyException {
        return new ResponseEntity(userService.getAllInternshipRequestsForSpecificInternship(internshipId),HttpStatus.OK);
    }

    @GetMapping("/all")
    public @ResponseBody
    ResponseEntity getAllInternships(){
        return new ResponseEntity(userService.getAllInternships(),HttpStatus.OK);
    }

    @GetMapping("/{id}/logo")
    public @ResponseBody
    ResponseEntity getInternshipLogo(@PathVariable Long id) throws NotValidInternshipException {
        return new ResponseEntity(userService.getInternshipLogo(id),HttpStatus.OK);
    }

    @PostMapping("/all/tags/lastweek")
    public @ResponseBody ResponseEntity getAllInternshipsFromLastWeekByTags(@RequestBody Set<Tag> tags){
        return new ResponseEntity(userService.getLast7DaysInternshipsByTags(tags), HttpStatus.OK);
    }

    @GetMapping("/{id}/tags")
    public @ResponseBody
    ResponseEntity getTagsForInternship(@PathVariable Long id) throws NotValidInternshipException {
        return new ResponseEntity(userService.getAllTagsForInternship(id),HttpStatus.OK);
    }

    @GetMapping("/{id}/skills")
    public @ResponseBody
    ResponseEntity getSkillsForInternship(@PathVariable Long id) throws NotValidInternshipException {
        return new ResponseEntity(userService.getAllSkillsForInternship(id),HttpStatus.OK);
    }

    @GetMapping("/{id}/requirements")
    public @ResponseBody
    ResponseEntity getRequirementsForInternship(@PathVariable Long id) throws NotValidInternshipException {
        return new ResponseEntity(userService.getAllRequirementsForInternship(id),HttpStatus.OK);
    }

    @GetMapping("/{id}/attributes")
    public @ResponseBody
    ResponseEntity getAttributesForInternship(@PathVariable Long id) throws NotValidInternshipException {
        return new ResponseEntity(userService.getAllAttributesForInternship(id),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @PutMapping("/{id}")
    public @ResponseBody
    ResponseEntity updateInternshipSimpleDetails(@PathVariable Long id,@RequestBody Internship internship) throws NotValidInternshipException, UpdateException {
        return new ResponseEntity(userService.updateInternshipSimpleDetails(id,internship),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @PutMapping("/{id}/tag")
    public @ResponseBody
    ResponseEntity updateInternshipTags(@PathVariable Long id,@RequestBody Tag tag) throws NotValidInternshipException, UpdateException, NotValidTagException {
        return new ResponseEntity(userService.addTagToInternship(id,tag),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @DeleteMapping("/{id}/tags/{tagId}")
    public @ResponseBody
    ResponseEntity removeTagFromInternship(@PathVariable Long id,@PathVariable Long tagId) throws NotValidInternshipException, UpdateException, NotValidTagException {
        return new ResponseEntity(userService.removeTagFromInternship(id,tagId),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @PutMapping("/{id}/skill")
    public @ResponseBody
    ResponseEntity updateInternshipSkills(@PathVariable Long id,@RequestBody Skill skill) throws NotValidInternshipException, UpdateException,  NotValidSkillException {
        return new ResponseEntity(userService.addSkillToInternship(id,skill),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @DeleteMapping("/{id}/skills/{skillId}")
    public @ResponseBody
    ResponseEntity removeSkillFromInternship(@PathVariable Long id,@PathVariable Long skillId) throws NotValidInternshipException, UpdateException, NotValidSkillException {
        return new ResponseEntity(userService.removeSkillFromInternship(id,skillId),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @PutMapping("/{id}/requirement")
    public @ResponseBody
    ResponseEntity updateInternshipRequirements(@PathVariable Long id,@RequestBody Requirement requirement) throws NotValidInternshipException, UpdateException {
        return new ResponseEntity(userService.addRequirementToInternship(id,requirement),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @GetMapping("/{internshipId}/applicants")
    public @ResponseBody
    ResponseEntity getAllApplicantsForSpecificInternship(@PathVariable Long internshipId) throws NotValidInternshipException, NotAllowedCompanyException {
        return new ResponseEntity(userService.getApplicantsForSpecificInternship(internshipId),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @DeleteMapping("/{id}/requirements/{requirementId}")
    public @ResponseBody
    ResponseEntity removeRequirementFromInternship(@PathVariable Long id,@PathVariable Long requirementId) throws NotValidInternshipException, UpdateException, NotValidRequirementException {
        return new ResponseEntity(userService.removeRequirementFromInternship(id,requirementId),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @PutMapping("/{id}/attribute")
    public @ResponseBody
    ResponseEntity updateInternshipAttributes(@PathVariable Long id,@RequestBody Attribute attribute) throws NotValidInternshipException, UpdateException {
        return new ResponseEntity(userService.addAttributeToInternship(id,attribute),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('APPLICANT')")
    @PostMapping("/{internshipId}/apply")
    public @ResponseBody
    ResponseEntity applyForInternship(@PathVariable Long internshipId) throws NotValidInternshipException, NotAllowedApplicantException {
        return new ResponseEntity(userService.applyForInternship(internshipId),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @DeleteMapping("/{id}/attributes/{attributeId}")
    public @ResponseBody
    ResponseEntity removeAttributeFromInternship(@PathVariable Long id,@PathVariable Long attributeId) throws NotValidInternshipException, UpdateException,  NotValidAttributeException {
        return new ResponseEntity(userService.removeAttributeFromInternship(id,attributeId),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('COMPANY')")
    @PostMapping("/create")
    public @ResponseBody ResponseEntity createInternship(@RequestBody CreateInternshipRequest createInternshipRequest) throws NotValidCompanyException {
        Internship internshipResult =
                userService.createInternship(
                        createInternshipRequest.getCompany(),
                        createInternshipRequest.getInternship()
                );
        if (internshipResult==null)
            return new ResponseEntity(HttpStatus.CONFLICT);
        return new ResponseEntity(internshipResult,HttpStatus.CREATED);
    }

    @ExceptionHandler(NotAllowedCompanyException.class)
    public @ResponseBody
    ResponseEntity handleNotAllowedCompanyException(NotAllowedCompanyException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UpdateException.class)
    public @ResponseBody
    ResponseEntity handleUpdateException(UpdateException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidTagException.class)
    public @ResponseBody
    ResponseEntity handleNotValidTagException(NotValidTagException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidRequirementException.class)
    public @ResponseBody
    ResponseEntity handleNotValidRequirementException(NotValidRequirementException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidAttributeException.class)
    public @ResponseBody
    ResponseEntity handleNotValidAttributeException(NotValidAttributeException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidSkillException.class)
    public @ResponseBody
    ResponseEntity handleNotValidSkillException(NotValidSkillException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotValidInternshipException.class)
    public @ResponseBody
    ResponseEntity handleNotValidInternshipException(NotValidInternshipException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotAllowedApplicantException.class)
    public @ResponseBody
    ResponseEntity handleNotAllowedApplicantException(NotAllowedApplicantException exception) {
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    }
}
