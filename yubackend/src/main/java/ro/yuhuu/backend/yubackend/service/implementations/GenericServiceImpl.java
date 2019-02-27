package ro.yuhuu.backend.yubackend.service.implementations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.yuhuu.backend.yubackend.config.SecurityConfig;
import ro.yuhuu.backend.yubackend.controller.requests.UpdateInternshipRequestStatus;
import ro.yuhuu.backend.yubackend.exceptions.*;
import ro.yuhuu.backend.yubackend.model.*;
import ro.yuhuu.backend.yubackend.repository.*;
import ro.yuhuu.backend.yubackend.service.GenericService;
import ro.yuhuu.backend.yubackend.service.InternshipDTOResponse;
import ro.yuhuu.backend.yubackend.service.MailSender;

import javax.annotation.PostConstruct;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@Service
public class GenericServiceImpl implements GenericService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InternshipRepository internshipRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RequirementRepository requirementRepository;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private ExperienceRepository experienceRepository;
    @Autowired
    private CVRepository cvRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private InternshipRequestRepository internshipRequestRepository;

    @PostConstruct
    private void loadData() {
        initRoles();
        initDatabase();
    }


    private boolean checkTheUser(User user) {
        User authenticatedUser = getAuthenticatedUser();
        if (user != null && authenticatedUser != null)
            return authenticatedUser.getId() == user.getId();
        return false;
    }

    private boolean checkTheRoleForAuthenticatedUser(Role role) {
        User authenticatedUser = getAuthenticatedUser();
        return authenticatedUser.getRoles().contains(role);
    }


    @Override
    public Company getCompanyByUserId(Long id) throws NotValidCompanyException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            if (user.get().getCompany() != null)
                return user.get().getCompany();
        }
        throw new NotValidCompanyException("No company with this user ID!");
    }

    @Override
    public Applicant getApplicantByUserId(Long id) throws NotValidApplicantException, NotAllowedApplicantException {

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {

            if (!checkTheUser(user.get())) {
                throw new NotAllowedApplicantException("You don't have permissions to remove this data!");
            }

            if (user.get().getApplicant() != null)
                return user.get().getApplicant();
        }
        throw new NotValidApplicantException("No applicant with this user ID!");
    }

    @Override
    public boolean checkUsernameExists(User user) {
        return userRepository.findByUsername(user.getUsername()).isPresent();
    }


    @Override
    public boolean checkEmailExists(User user) {
        return userRepository.findByEmail(user.getEmail()).isPresent();
    }


    @Override
    public Set<Internship> getAllInternshipsForCompany(Long id) throws NotValidCompanyException, NotAllowedCompanyException {
        Company company1 = findCompanyById(id);
        return company1.getInternships();
    }

    private void initDefaultContactForApplicant(Applicant applicant) {
        Contact contact = new Contact.Builder()
                .withFacebookLink("")
                .withPhoneNumber("")
                .withWebsite("")
                .withLinkedinLink("")
                .build();
        applicant.setContact(contact);
        contactRepository.saveAndFlush(contact);
    }

    private void initDefaultAddressForContact(Contact contact) {
        Address address = new Address.Builder()
                .withNumber("")
                .withCountry("")
                .withCounty("")
                .withPostalCode("")
                .withSector("")
                .withStreet("")
                .withEntrance("")
                .withApartment("")
                .withBlock("")
                .withFloor("")
                .withTown("")
                .build();
        contact.setAddress(address);
        addressRepository.saveAndFlush(address);
    }


    private void setLatLngForAddress(Address address) {
        if (address != null && !address.toString().equals("")) {
            LatLng latLng = getCoordinatesForAddress(address);
            address.setLatitude(latLng.lat);
            address.setLongitude(latLng.lng);
            addressRepository.saveAndFlush(address);
        }
    }

    @Override
    public Applicant registerApplicant(User user, Applicant applicant) {
        user.setActive(true);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleStringEquals(RoleString.APPLICANT));
        user.setRoles(roles);
        user.setPassword(SecurityConfig.passwordEncoder().encode(user.getPassword()));
        User userResult = userRepository.save(user);
        userResult.setApplicant(applicant);
        Applicant currentApplicant = applicantRepository.saveAndFlush(applicant);
        Contact contact = currentApplicant.getContact();
        if (contact == null) {
            initDefaultContactForApplicant(currentApplicant);

            Contact currentContact = currentApplicant.getContact();

            initDefaultAddressForContact(currentContact);
            initDefaulPhotoForApplicant(currentContact);
            initDefaultCVForApplicant(currentContact);
        } else {
            setLatLngForAddress(contact.getAddress());
        }
        if (applicant.getDescription() == null) {
            applicant.setDescription("");
        }
        return currentApplicant;
    }

    private void initDefaultCVForApplicant(Contact contact) {

        CV cv = new CV();
        contact.setCv(cv);
        cvRepository.saveAndFlush(cv);
    }

    private void initDefaulPhotoForApplicant(Contact contact) {

        Photo photo = new Photo();
        contact.setPhoto(photo);

        photoRepository.saveAndFlush(photo);
    }

    @Override
    public Company registerCompany(User user, Company company) throws NotValidCompanyException {
        user.setActive(true);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleStringEquals(RoleString.APPLICANT));
        user.setRoles(roles);
        user.setPassword(SecurityConfig.passwordEncoder().encode(user.getPassword()));
        User userResult = userRepository.save(user);
        userResult.setCompany(company);
        Company currentCompany = companyRepository.saveAndFlush(company);
        Contact contact = currentCompany.getContact();

        if (contact != null) {
            setLatLngForAddress(contact.getAddress());
        } else {
            initDefaultContactForCompany(currentCompany);
            initDefaultAddressForCompany(currentCompany.getContact());
            initDefaultCVForCompany(currentCompany);
            initDefaulPhotoForCompany(currentCompany);

        }

        return currentCompany;
    }

    private void initDefaulPhotoForCompany(Company company) {

        Photo photo = new Photo();
        company.getContact().setPhoto(photo);
        photoRepository.saveAndFlush(photo);

    }

    private void initDefaultCVForCompany(Company company) {

        CV cv = new CV();
        company.getContact().setCv(cv);
        cvRepository.saveAndFlush(cv);

    }

    private void initDefaultAddressForCompany(Contact contact) {
        Address address = new Address.Builder()
                .withNumber("")
                .withCountry("")
                .withCounty("")
                .withPostalCode("")
                .withSector("")
                .withStreet("")
                .withEntrance("")
                .withApartment("")
                .withBlock("")
                .withFloor("")
                .withTown("")
                .build();
        contact.setAddress(address);
        addressRepository.saveAndFlush(address);
    }

    private void initDefaultContactForCompany(Company company) {
        Contact contact = new Contact.Builder()
                .withFacebookLink("")
                .withPhoneNumber("")
                .withWebsite("")
                .withLinkedinLink("")
                .build();
        company.setContact(contact);
        contactRepository.saveAndFlush(contact);
    }

    private Company findCompanyById(Long id) throws NotValidCompanyException {
        Optional<Company> companyResult = companyRepository.findById(id);
        if (!companyResult.isPresent()) {
            throw new NotValidCompanyException("Company with ID:" + id + " doesn't exist!");
        }

        Company company = companyResult.get();
        return company;
    }

    private Comment findCommentById(Long id) throws NotValidCommentException {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (!optionalComment.isPresent())
            throw new NotValidCommentException("Comment with ID:" + id + " doesn't exist!");

        Comment comment = optionalComment.get();
        return comment;
    }

    @Override
    public Internship createInternship(Company company, Internship internship) throws NotValidCompanyException {
        Company actualComapny = findCompanyById(company.getId());

        actualComapny.addInternship(internship);
        return internshipRepository.saveAndFlush(internship);


    }

    @Override
    public Company getCompanyDetailsById(Long id) throws NotValidCompanyException, NotAllowedCompanyException {
        Company actualCompany = findCompanyById(id);

        return actualCompany;
    }

    @Override
    public Set<Internship> getInternshipsByTags(Set<Tag> tags) {
        Set<Internship> result = new HashSet<>();
        for (Tag tag : tags) {
            Optional<Tag> currentTag = tagRepository.findByNameEquals(tag.getName());
            if (currentTag.isPresent())
                result.addAll(currentTag.get().getInternships());
        }
        return result;
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String string = (String) authentication.getPrincipal();
        User user = null;
        Optional<User> userUsername = userRepository.findByUsername(string);
        if (!userUsername.isPresent()) {
            Optional<User> userEmail = userRepository.findByEmail(string);
            if (userEmail.isPresent())
                user = userEmail.get();
        } else
            user = userUsername.get();
        return user;
    }

    private Applicant findApplicantById(Long id) throws NotValidApplicantException {

        Optional<Applicant> applicantOptional = applicantRepository.findById(id);
        if (!applicantOptional.isPresent()) {
            throw new NotValidApplicantException("Applicant with ID:" + id + " doesn't exist!");
        }
        Applicant applicant = applicantOptional.get();

        return applicant;

    }

    private Tag findTagByName(String name) throws NotValidTagException {
        Optional<Tag> tag = tagRepository.findByNameEquals(name);
        if (!tag.isPresent())
            throw new NotValidTagException("Tag with name:" + name + " does not exist!");
        return tag.get();
    }

    private Skill findSkillByName(String name) throws NotValidSkillException {
        Optional<Skill> skill = skillRepository.findByNameEquals(name);
        if (!skill.isPresent())
            throw new NotValidSkillException("Skill with name:" + name + " does not exist!");
        return skill.get();
    }

    private Skill findSkillById(Long id) throws NotValidSkillException {
        Optional<Skill> skill = skillRepository.findById(id);
        if (!skill.isPresent())
            throw new NotValidSkillException("Skill with ID:" + id + " does not exist!");
        return skill.get();
    }

    private Tag findTagById(Long id) throws NotValidTagException {
        Optional<Tag> tag = tagRepository.findById(id);
        if (!tag.isPresent())
            throw new NotValidTagException("Tag with ID:" + id + " does not exist!");
        return tag.get();
    }

    private Internship findInternshipById(Long id) throws NotValidInternshipException {
        Optional<Internship> optionalInternship = internshipRepository.findById(id);
        if (!optionalInternship.isPresent())
            throw new NotValidInternshipException("Internship with ID:" + id + " doesn't exist!");

        Internship internship = optionalInternship.get();
        return internship;
    }

    private Requirement findRequirementById(Long requirementId) throws NotValidRequirementException {
        Optional<Requirement> optionalRequirement = requirementRepository.findById(requirementId);
        if (!optionalRequirement.isPresent())
            throw new NotValidRequirementException("Requirement with ID:" + requirementId + " doesn't exist!");

        Requirement requirement = optionalRequirement.get();
        return requirement;
    }

    private Attribute findAttributeById(Long attributeId) throws NotValidAttributeException {
        Optional<Attribute> optionalAttribute = attributeRepository.findById(attributeId);
        if (!optionalAttribute.isPresent())
            throw new NotValidAttributeException("Attribute with ID:" + attributeId + " doesn't exist!");

        Attribute attribute = optionalAttribute.get();
        return attribute;
    }


    @Override
    public Applicant updateApplicantEducation(Long id, Education education) throws NotValidApplicantException, NotAllowedApplicantException {

        Applicant applicant = findApplicantById(id);

        if (!checkTheUser(applicant.getUser()))
            throw new NotAllowedApplicantException("You don't have permissions to modify this data!");

        applicant.addEducation(education);
        return applicantRepository.save(applicant);
    }

    @Override
    public Applicant updateApplicantSkills(Long id, Skill skill) throws NotValidApplicantException, NotAllowedApplicantException, NotValidSkillException {

        Applicant applicant = findApplicantById(id);

        if (!checkTheUser(applicant.getUser()))
            throw new NotAllowedApplicantException("You don't have permissions to modify this data!");

        Skill skillAux = findSkillByName(skill.getName());
        applicant.addSkill(skillAux);
        applicantRepository.save(applicant);
        return applicant;
    }

    @Override
    public Applicant deleteApplicantSkill(Long id, Long skillId) throws NotValidApplicantException, NotAllowedApplicantException, NotValidSkillException {
        Applicant applicant = findApplicantById(id);

        if (!checkTheUser(applicant.getUser()))
            throw new NotAllowedApplicantException("You don't have permissions to remove this data!");

        Skill skill = findSkillById(skillId);

        applicant.removeSkill(skill);
        return applicantRepository.save(applicant);
    }

    @Override
    public Applicant deleteApplicantEducation(Long id, Long educationId) throws NotValidApplicantException, NotAllowedApplicantException, NotValidEducationException {
        Applicant applicant = findApplicantById(id);

        if (!checkTheUser(applicant.getUser()))
            throw new NotAllowedApplicantException("You don't have permissions to remove this data!");

        Optional<Education> educationOptional = educationRepository.findById(educationId);
        if (!educationOptional.isPresent())
            throw new NotValidEducationException("Education with ID:" + educationId + " doesn't exist!");

        Education education = educationOptional.get();

        applicant.removeEducation(education);
        educationRepository.delete(education);
        return applicant;
    }

    @Override
    public Comment removeComment(Long commentId) throws NotValidCommentException, NotAllowedApplicantException {
        Comment comment = findCommentById(commentId);
        if (!checkTheUser(comment.getCreator()))
            throw new NotAllowedApplicantException("Not allowed to remove this comment!");

        Internship internship = comment.getInternship();
        if (internship != null) {
            internship.removeComment(comment);
            commentRepository.delete(comment);
        } else {
            Comment parent = comment.getParent();
            if (parent != null) {
                parent.removeSubComment(comment);
                commentRepository.delete(comment);
            }
        }
        return comment;
    }

    @Override
    public Company getCompanyByInternshipId(Long internshipId) throws NotValidInternshipException {
        Internship internship = findInternshipById(internshipId);
        return internship.getCompany();
    }

    @Override
    public void sendEmail(String destination, String subject, String content) {

        MailSender mailSender = new MailSender(destination, subject, content);
        mailSender.setDaemon(true);
        mailSender.start();
    }

    private InternshipRequest findInternshipRequestByApplicantAndInternship(Applicant applicant, Internship internship) {
        Optional<InternshipRequest> optionalInternshipRequest = internshipRequestRepository.findInternshipRequestByApplicantAndInternship(applicant, internship);
        if (!optionalInternshipRequest.isPresent())
            return null;
        return optionalInternshipRequest.get();
    }

    private InternshipRequest findInternshipRequestById(Long internshipRequestId) throws NotValidInternshipRequestException {
        Optional<InternshipRequest> optionalInternshipRequest = internshipRequestRepository.findById(internshipRequestId);
        if (!optionalInternshipRequest.isPresent())
            throw new NotValidInternshipRequestException("InternshipRequest with ID:" + internshipRequestId + " doesn't exist!");
        return optionalInternshipRequest.get();
    }

    @Override
    public InternshipRequest applyForInternship(Long internshipId) throws NotValidInternshipException, NotAllowedApplicantException {
        Internship internship = findInternshipById(internshipId);
        User user = getAuthenticatedUser();

        Applicant applicant = user.getApplicant();

        if (findInternshipRequestByApplicantAndInternship(applicant, internship) != null)
            throw new NotAllowedApplicantException("You can't apply again for this internship!");

        applicant.getInternship().add(internship);
        internship.getApplicants().add(applicant);

        return createInternshipRequest(internship, applicant, InternshipRequestStatus.PENDING);
    }

    @Override
    public Set<Applicant> getApplicantsForSpecificInternship(Long internshipId) throws NotValidInternshipException, NotAllowedCompanyException {
        Internship internship = findInternshipById(internshipId);
        User user = getAuthenticatedUser();
        Company loggedCompany = user.getCompany();
        Company currentCompany = internship.getCompany();
        if (!(currentCompany.getId() == loggedCompany.getId()))
            throw new NotAllowedCompanyException("You can't get this information.");

        return internship.getApplicants();
    }

    @Override
    public Boolean updateInternshipRequestStatus(Long internshipRequestId, UpdateInternshipRequestStatus updateInternshipRequestStatus) throws NotValidInternshipRequestException, NotAllowedCompanyException {
        InternshipRequest internshipRequest = findInternshipRequestById(internshipRequestId);
        Applicant applicant = internshipRequest.getApplicant();
        Internship internship = internshipRequest.getInternship();

        User user = getAuthenticatedUser();
        Company loggedCompany = user.getCompany();
        Company currentCompany = internship.getCompany();

        if (!(loggedCompany.getId() == currentCompany.getId()))
            throw new NotAllowedCompanyException("You are not allowed to modify this data!");

        String destination = applicant.getUser().getEmail();
        String content = updateInternshipRequestStatus.getContent();
        String subject = updateInternshipRequestStatus.getSubject();

        InternshipRequestStatus internshipRequestStatus = updateInternshipRequestStatus.getInternshipRequestStatus();

        internshipRequest.setInternshipRequestStatus(internshipRequestStatus);
        internshipRequestRepository.save(internshipRequest);
        sendEmail(destination, subject, content);

        return Boolean.valueOf(true);
    }

    @Override
    public Applicant getApplicantByApplicantId(Long applicantId) throws NotValidApplicantException {
        Applicant applicant = findApplicantById(applicantId);

        return applicant;
    }

    @Override
    public List<Internship> getLast7DaysInternshipsByTags(Set<Tag> tags) {

        Set<Internship> internships = getInternshipsByTags(tags);


        List<Internship> resultInternships = internships.stream().filter(
                intern -> intern.getActive() == true &&
                        intern.getStartDate().isAfter(LocalDate.now().minusWeeks(1).minusDays(1))
        )
                .collect(Collectors.toList());

        return resultInternships;

    }

    @Override
    public List<Company> getSortedCompaniesByNoEmployees(Integer nrItems) {
        List<Company> companyList = companyRepository.findAll();
        Collections.sort(companyList, new Comparator<Company>() {
            @Override
            public int compare(Company o1, Company o2) {
                if (o1.getDimension() == o2.getDimension())
                    return 0;
                return o1.getDimension() < o2.getDimension() ? 1 : -1;
            }
        });
        Integer actualNrOfItems = companyList.size();
        if (actualNrOfItems < nrItems)
            nrItems = actualNrOfItems;
        return (List<Company>) companyList.subList(0, nrItems);
    }

    @Override
    public List<InternshipRequest> getAllInternshipRequestsForApplicant() {
        User user = getAuthenticatedUser();
        Applicant applicant = user.getApplicant();

        Set<InternshipRequest> allInternshipRequests = applicant.getInternshipRequests();
        List<InternshipRequest> sortedList = new ArrayList<>(allInternshipRequests);

        Collections.sort(sortedList, new Comparator<InternshipRequest>() {
            @Override
            public int compare(InternshipRequest o1, InternshipRequest o2) {
                if (o1.getId().equals(o2.getId()))
                    return 0;
                return o1.getId() < o2.getId() ? -1 : 1;
            }
        });
        return sortedList;
    }

    @Override
    public List<InternshipRequest> getAllInternshipRequestsForCompany() {
        User user = getAuthenticatedUser();
        Company company = user.getCompany();

        Set<InternshipRequest> internshipRequestsForCompany = new HashSet<>();
        List<InternshipRequest> allInternshipRequests = internshipRequestRepository.findAll();
        for (InternshipRequest internshipRequest : allInternshipRequests) {
            if (internshipRequest.getInternship().getCompany().getId() == company.getId())
                internshipRequestsForCompany.add(internshipRequest);
        }

        List<InternshipRequest> sortedList = new ArrayList<>(internshipRequestsForCompany);

        Collections.sort(sortedList, new Comparator<InternshipRequest>() {
            @Override
            public int compare(InternshipRequest o1, InternshipRequest o2) {
                if (o1.getId().equals(o2.getId()))
                    return 0;
                return o1.getId() < o2.getId() ? -1 : 1;
            }
        });
        return sortedList;
    }

    @Override
    public Boolean cancelInternshipRequest(Long internshipRequestId) throws NotValidInternshipRequestException, NotAllowedApplicantException {
        InternshipRequest internshipRequest = findInternshipRequestById(internshipRequestId);

        User user = getAuthenticatedUser();
        Applicant applicant = user.getApplicant();

        Applicant internshipRequestApplicant = internshipRequest.getApplicant();
        Internship internship = internshipRequest.getInternship();
        if (!(applicant.getId() == internshipRequestApplicant.getId()))
            throw new NotAllowedApplicantException("You are not allowed to cancel this internship request!");

        applicant.removeInternshipRequest(internshipRequest);
        internship.removeInternshipRequest(internshipRequest);

        internshipRequest.setApplicant(null);
        internshipRequest.setInternship(null);

        applicant.removeInternship(internship);
        internship.removeApplicant(applicant);

        applicantRepository.save(applicant);
        internshipRepository.save(internship);

        internshipRequestRepository.delete(internshipRequest);

        return true;

    }

    @Override
    public List<InternshipDTOResponse> getAllInternshipDTOs() {
        List<Internship> internships = getAllInternships();
        List<InternshipDTOResponse> internshipDTOResponses = new ArrayList<>();
        for (Internship internship : internships) {
            internshipDTOResponses.add(new InternshipDTOResponse(internship, internship.getCompany()));
        }
        return internshipDTOResponses;
    }

    @Override
    public List<InternshipRequest> getAllInternshipRequestsForSpecificInternship(Long internshipId) throws NotValidInternshipException, NotAllowedCompanyException {
        User user = getAuthenticatedUser();
        Company company = user.getCompany();
        Internship internship = findInternshipById(internshipId);

        if (!(company.getId() == internship.getCompany().getId()))
            throw new NotAllowedCompanyException("You are not allowed to get this data!");

        Set<InternshipRequest> internshipRequestsForInternship = internship.getInternshipRequests();

        List<InternshipRequest> sortedList = new ArrayList<>(internshipRequestsForInternship);

        Collections.sort(sortedList, new Comparator<InternshipRequest>() {
            @Override
            public int compare(InternshipRequest o1, InternshipRequest o2) {
                if (o1.getInternshipRequestStatus().equals(InternshipRequestStatus.PENDING) && !(o2.getInternshipRequestStatus().equals(InternshipRequestStatus.PENDING))) {
                    return -1;
                } else {
                    if (!(o1.getInternshipRequestStatus().equals(InternshipRequestStatus.PENDING)) && o2.getInternshipRequestStatus().equals(InternshipRequestStatus.PENDING)) {
                        return 1;
                    }
                    return 0;
                }
            }
        });
        return sortedList;
    }

    @Override
    public List<Applicant> getLastXRegisteredApplicants(Long number) {
        List<Applicant> applicants = applicantRepository.findAll();
        int startIndex= (int) (applicants.size()-number);
        if(startIndex>=0){
            return applicants.subList(startIndex,applicants.size());
        }
        return applicants;
    }


    @Override
    public Comment likeComment(Long commentId) throws NotValidCommentException {

        Comment comment = findCommentById(commentId);
        User user = getAuthenticatedUser();
        Comment resultComent = userLikeComment(comment, user);

        return resultComent;

    }

    @Override
    public Comment dislikeComment(Long commentId) throws NotValidCommentException {
        Comment comment = findCommentById(commentId);
        User user = getAuthenticatedUser();
        Comment resultComent = userDislikeComment(comment, user);
        return resultComent;
    }

    private Contact findContactByCompanyId(Long companyId) throws NotValidContactException {
        Optional<Contact> contactOptional = contactRepository.findByCompanyId(companyId);

        if (!contactOptional.isPresent()) {
            throw new NotValidContactException("Error when trying to get the contact from the company with the ID " + companyId);
        }

        Contact contact = contactOptional.get();

        return contact;
    }

    private Contact findContactByApplicantId(Long applicantId) throws NotValidContactException {
        Optional<Contact> contactOptional = contactRepository.findByApplicantId(applicantId);

        if (!contactOptional.isPresent()) {
            throw new NotValidContactException("Error when trying to get the contact from the  applicant with the ID " + applicantId);
        }

        Contact contact = contactOptional.get();

        return contact;

    }

    private Photo findPhotoByContactId(Long contactId) throws NotValidContactException {
        Optional<Photo> photoOptional = photoRepository.findPhotoByContact_Id(contactId);

        if (!photoOptional.isPresent()) {
            throw new NotValidContactException("Error when trying to get the photo from the contact id:  " + contactId);
        }

        Photo photo = photoOptional.get();

        return photo;
    }

    @Override
    public Photo uploadPhotoForApplicant(Long applicantId, MultipartFile photoFile) throws NotValidApplicantException, NotAllowedApplicantException, NotValidImageUploadException, NotValidContactException, NotValidPhotoException {

        Applicant applicant = findApplicantById(applicantId);

        if (!checkTheUser(applicant.getUser()))
            throw new NotAllowedApplicantException("You don't have permissions to modify data!");

        Contact contact = findContactByApplicantId(applicantId);
        Photo photo = findPhotoByContactId(contact.getId());

        try {
            Map uploadResult = cloudinary.uploader().upload(photoFile.getBytes(), ObjectUtils.emptyMap());

            String photoUrl = (String) uploadResult.get("url");
            String public_id = (String) uploadResult.get("public_id");
            photo.setUrl(photoUrl);
            photo.setPublic_id(public_id);
            photo.setDeleted(false);

        } catch (IOException e) {
            throw new NotValidImageUploadException("Error when trying to upload the image");
        }

        photoRepository.saveAndFlush(photo);

        return photo;
    }

    @Override
    public Photo getApplicantPhoto(Long applicantId) throws NotValidContactException, NotValidPhotoException {

        Contact contact = findContactByApplicantId(applicantId);
        Photo photo = findPhotoByContactId(contact.getId());

        if (photo.isDeleted() == true) {
            throw new NotValidPhotoException("There is no photo uploaded.");
        }

        return photo;

    }

    @Override
    public Photo deleteApplicantPhoto(Long applicantId) throws NotValidContactException, NotAllowedDeletingPhotoException {

        Contact contact = findContactByApplicantId(applicantId);
        Photo photo = findPhotoByContactId(contact.getId());

        try {
            cloudinary.uploader().destroy(photo.getPublic_id(),
                    ObjectUtils.emptyMap());

            photo.setDeleted(true);
            photoRepository.saveAndFlush(photo);
        } catch (IOException e) {
            throw new NotAllowedDeletingPhotoException("Error when trying to delete photo");
        }

        return photo;


    }

    private CV findCVByContactId(Long contactId) throws NotValidCVException {

        Optional<CV> cvOptional = cvRepository.findCVByContact_Id(contactId);

        if (!cvOptional.isPresent()) {
            throw new NotValidCVException("Error when trying to get the CV from the contact id:  " + contactId);
        }

        CV cv = cvOptional.get();

        return cv;

    }

    @Override
    public void checkCVIsPDF(String path) throws WrongFormatException {
        if (!path.endsWith(".pdf")) {
            throw new WrongFormatException("CV should be in PDF format");
        }

    }

    @Override
    public CV uploadApplicantCV(Long applicantId, MultipartFile cvFile) throws NotValidContactException, NotValidCVException, NotValidCVUploadException {

        Contact contact = findContactByApplicantId(applicantId);
        CV cv = findCVByContactId(contact.getId());

        try {
            Map uploadParams = ObjectUtils.asMap("resource_type", "raw");
            Map uploadResult = cloudinary.uploader().upload(cvFile.getBytes(), uploadParams);

            String CVUrl = (String) uploadResult.get("url");
            String public_id = (String) uploadResult.get("public_id");
            cv.setUrl(CVUrl);
            cv.setPublic_id(public_id);
            cv.setDeleted(false);

        } catch (IOException e) {
//            throw new NotValidCVUploadException(e.getMessage());
            throw new NotValidCVUploadException("Error when trying to upload the CV");
        }

        cvRepository.saveAndFlush(cv);

        return cv;

    }

    @Override
    public boolean createApplicantCVIfNotExist(Long applicantId) throws NotValidContactException {

        boolean created = false;

        Contact contact = findContactByApplicantId(applicantId);

        Optional<CV> cvOptional = cvRepository.findCVByContact_Id(contact.getId());

        if (!cvOptional.isPresent()) {
            CV newCV = new CV();
            contact.setCv(newCV);
            cvRepository.saveAndFlush(newCV);
            created = true;
        }
        return created;

    }

    @Override
    public boolean createApplicantPhotoIfNotExist(Long applicantId) throws NotValidContactException {
        boolean created = false;

        Contact contact = findContactByApplicantId(applicantId);

        Optional<Photo> photoOptional = photoRepository.findPhotoByContact_Id(contact.getId());

        if (!photoOptional.isPresent()) {
            Photo newPhoto = new Photo();
            contact.setPhoto(newPhoto);
            photoRepository.saveAndFlush(newPhoto);
            created = true;
        }
        return created;
    }

    @Override
    public boolean createCompanyPhotoIfNotExist(Long companyId) throws NotValidContactException {
        boolean created = false;

        Contact contact = findContactByCompanyId(companyId);

        Optional<Photo> photoOptional = photoRepository.findPhotoByContact_Id(contact.getId());

        if (!photoOptional.isPresent()) {
            Photo newPhoto = new Photo();
            contact.setPhoto(newPhoto);
            photoRepository.saveAndFlush(newPhoto);
            created = true;
        }
        return created;
    }

    @Override
    public CV deleteApplicantCV(Long applicantId) throws NotValidContactException, NotValidCVException, NotAllowedDeletingCVException {

        Contact contact = findContactByApplicantId(applicantId);
        CV cv = findCVByContactId(contact.getId());


        try {
            cloudinary.uploader().destroy(cv.getPublic_id(),
                    ObjectUtils.emptyMap());

            cv.setDeleted(true);
            cvRepository.saveAndFlush(cv);
        } catch (IOException e) {
            throw new NotAllowedDeletingCVException("Error when trying to delete CV");
        }

        return cv;


    }

    @Override
    public CV getApplicantCV(Long applicantId) throws NotValidContactException, NotValidCVException {
        Contact contact = findContactByApplicantId(applicantId);
        CV cv = findCVByContactId(contact.getId());

        if (cv.isDeleted() == true) {
            throw new NotValidCVException("There is no CV uploaded. Please upload a .pdf format CV");
        }

        return cv;
    }


    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public void incrementViewCounter(Long companyId) throws NotValidCompanyException {

        Company company = findCompanyById(companyId);
        User user = getAuthenticatedUser();

        if (company.getUser().getId() == user.getId())
            return;

        LocalDate currentLocalDate = LocalDate.now();

        Optional<ViewsCounter> optionalView = viewRepository.findViewByUserAndCompany(user, company);

        if (optionalView.isPresent()) {
            ViewsCounter viewsCounter = optionalView.get();
            LocalDate lastViewDate = viewsCounter.getLastViewDay();
            if (!currentLocalDate.equals(lastViewDate)) {
                company.setViews(company.getViews() + 1);
                companyRepository.save(company);
                viewsCounter.setLastViewDay(LocalDate.now());
                viewRepository.save(viewsCounter);
            }
        } else {
            ViewsCounter viewsCounter = new ViewsCounter.Builder()
                    .withUser(user)
                    .withCompany(company)
                    .withLocalDate(currentLocalDate)
                    .build();
            company.setViews(company.getViews() + 1);

            viewRepository.save(viewsCounter);
            companyRepository.save(company);
        }
    }

    @Override
    public Double getDistanceFromUserToCompany(Long companyId) throws NotValidCompanyException, NotValidAddressException {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (!optionalCompany.isPresent())
            throw new NotValidCompanyException("Not existing company!");

        Company company = optionalCompany.get();
        User user = getAuthenticatedUser();


        Address firstAddress = company.getContact().getAddress();
        Address secondAddress = null;

        Set<Role> roles = user.getRoles();

        boolean isApplicant = false, isCompany = false;

        for (Role role : roles) {
            if (role.getRoleString().toString().equals("APPLICANT")) {
                isApplicant = true;
                break;
            }
        }

        if (isApplicant) {
            Optional<Applicant> optionalApplicant = applicantRepository.findApplicantByUser(user);
            if (optionalApplicant.isPresent()) {
                Applicant applicant = optionalApplicant.get();
                secondAddress = applicant.getContact().getAddress();
            }
        } else {

            for (Role role : roles) {
                if (role.getRoleString().toString().equals("COMPANY")) {
                    isCompany = true;
                    break;
                }
            }

            if (isCompany) {
                Optional<Company> optionalCompany1 = companyRepository.findCompanyByUser(user);
                if (optionalCompany.isPresent()) {
                    Company company1 = optionalCompany1.get();
                    secondAddress = company1.getContact().getAddress();
                }
            }
        }

        if (secondAddress == null && firstAddress == null)
            throw new NotValidAddressException("Not valid addresses!");
        if (firstAddress == null)
            throw new NotValidAddressException("The company address is not valid!");
        if (secondAddress == null)
            throw new NotValidAddressException("Your address is not valid!");

        Double distance = Geocoding.getDistance(firstAddress.toString(), secondAddress.toString());
        return distance;
    }

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    private void updateFirstNameApplicant(Applicant currentApplicant, Applicant applicant) {
        if (applicant.getFirstName() != null && !currentApplicant.getFirstName().equals(applicant.getFirstName()))
            currentApplicant.setFirstName(applicant.getFirstName());
    }

    private void updateLastNameApplicant(Applicant currentApplicant, Applicant applicant) {
        if (applicant.getLastName() != null && !currentApplicant.getLastName().equals(applicant.getLastName()))
            currentApplicant.setLastName(applicant.getLastName());
    }

    private void updateDescriptionApplicant(Applicant currentApplicant, Applicant applicant) {
        if (applicant.getDescription() != null && !currentApplicant.getDescription().equals(applicant.getDescription()))
            currentApplicant.setDescription(applicant.getDescription());
    }

    private void updateBirthdayApplicant(Applicant currentApplicant, Applicant applicant) {
        if (applicant.getBirthday() != null && !currentApplicant.getBirthday().equals(applicant.getBirthday()))
            currentApplicant.setBirthday(applicant.getBirthday());
    }

    @Override
    public Applicant updateApplicant(Long id, Applicant applicant) throws NotValidApplicantException, NotAllowedApplicantException {

        Applicant currentApplicant = findApplicantById(id);

        if (!checkTheUser(currentApplicant.getUser()))
            throw new NotAllowedApplicantException("You don't have permissions to update!");

        if (currentApplicant.getDescription() == null) {
            currentApplicant.setDescription("");
        }

        updateFirstNameApplicant(currentApplicant, applicant);
        updateLastNameApplicant(currentApplicant, applicant);
        updateDescriptionApplicant(currentApplicant, applicant);
        updateBirthdayApplicant(currentApplicant, applicant);

        return applicantRepository.save(currentApplicant);
    }

    private void updateLinkedInLink(Contact currentContact, Contact contact) {
        if (contact.getLinkedinLink() != null && !currentContact.getLinkedinLink().equals(contact.getLinkedinLink()))
            currentContact.setLinkedinLink(contact.getLinkedinLink());
    }

    private void updateWebsite(Contact currentContact, Contact contact) {
        if (contact.getWebsite() != null && !currentContact.getWebsite().equals(contact.getWebsite()))
            currentContact.setWebsite(contact.getWebsite());
    }

    private void updatePhoneNumber(Contact currentContact, Contact contact) {
        if (contact.getPhoneNumber() != null && !currentContact.getPhoneNumber().equals(contact.getPhoneNumber()))
            currentContact.setPhoneNumber(contact.getPhoneNumber());
    }

    private void updateFacebookLink(Contact currentContact, Contact contact) {
        if (contact.getFacebookLink() != null && !currentContact.getFacebookLink().equals(contact.getFacebookLink()))
            currentContact.setFacebookLink(contact.getFacebookLink());
    }

    private void initializeNullValuesForContact(Contact contact) {
        if (contact.getFacebookLink() == null)
            contact.setFacebookLink("");
        if (contact.getPhoneNumber() == null)
            contact.setPhoneNumber("");
        if (contact.getLinkedinLink() == null)
            contact.setLinkedinLink("");
        if (contact.getWebsite() == null)
            contact.setWebsite("");
    }

    @Override
    public Contact updateApplicantContact(Long id, Contact contact) throws NotValidApplicantException, NotAllowedApplicantException {

        Applicant currentApplicant = findApplicantById(id);

        if (!checkTheUser(currentApplicant.getUser()))
            throw new NotAllowedApplicantException("You don't have permissions to update!");

        Contact currentContact = currentApplicant.getContact();
        initializeNullValuesForContact(currentContact);

        updateLinkedInLink(currentContact, contact);
        updateWebsite(currentContact, contact);
        updatePhoneNumber(currentContact, contact);
        updateFacebookLink(currentContact, contact);
        updateAddress(currentContact.getAddress(), contact.getAddress());

        return contactRepository.save(currentContact);
    }

    private LatLng getCoordinatesForAddress(Address address) {
        return Geocoding.get(address.toString());
    }

    @Override
    public LatLng getCoordinatesForCompany(Long companyId) throws NotValidCompanyException {

        Company company = findCompanyById(companyId);
        Contact contact = company.getContact();
        if (contact != null) {
            Address address = contact.getAddress();
            if (address != null && !(address.toString().equals("")))
                return getCoordinatesForAddress(address);
        }
        return null;
    }

    @Override
    public Company updateCompanyProfile(Long companyId, Company newCompany) throws NotValidCompanyException, NotAllowedApplicantException {

        Company company = findCompanyById(companyId);

        if (!checkTheUser(company.getUser())) {
            throw new NotAllowedApplicantException("You don't have permissions to update!");
        }

        intializeNullValuesForCompanyProfile(company);

        company.setDescription(newCompany.getDescription());
        company.setDimension(newCompany.getDimension());
        company.setName(newCompany.getName());

        companyRepository.saveAndFlush(company);
        return company;

    }

    private void intializeNullValuesForCompanyProfile(Company company) {

        if (company.getDescription() == null) {
            company.setDescription("");
        }

        if (company.getDimension() == null) {
            company.setDimension(new Long(0));
        }

        if (company.getName() == null) {
            company.setName("");
        }

    }

    @Override
    public User updateApplicantEmail(Long id, User user) throws NotValidApplicantException, NotAllowedApplicantException, UpdateException {

        Applicant currentApplicant = findApplicantById(id);

        if (!checkTheUser(currentApplicant.getUser()))
            throw new NotAllowedApplicantException("You don't have permissions to update!");

        User currentUser = currentApplicant.getUser();

        User userWithNewEmail = updateEmail(currentUser, user);
        return userWithNewEmail;
    }

    @Override
    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    private void updateCompanyContact(Contact contact, Contact newContact) {

        initializeNullValuesForContact(contact);

        updateLinkedInLink(contact, newContact);
        updateWebsite(contact, newContact);
        updatePhoneNumber(contact, newContact);
        updateFacebookLink(contact, newContact);

        contactRepository.saveAndFlush(contact);
    }

    private void updateCompanyAddress(Address address, Address newAddress) {
        updateAddress(address, newAddress);
        // update Address does the flush by itself
    }


    @Override
    public Company updateCompanyContactAndAddress(Long companyId, Company newCompany) throws NotValidCompanyException, NotAllowedApplicantException, NotValidContactException {

        Company company = findCompanyById(companyId);

        if (!checkTheUser(company.getUser())) {
            throw new NotAllowedApplicantException("You don't have permissions to update!");
        }

        Contact contact = findContactByCompanyId(companyId);

        updateCompanyContact(contact, newCompany.getContact());
        updateCompanyAddress(contact.getAddress(), newCompany.getContact().getAddress());

        company = findCompanyById(companyId);
        return company;
    }

    private User updateEmail(User currentUser, User newUser) throws UpdateException {
        if (currentUser.getEmail().equals(newUser.getEmail()))
            return currentUser;
        if (checkEmailExists(newUser))
            throw new UpdateException("This mail already exists");
        if (newUser.getEmail() != null) {
            currentUser.setEmail(newUser.getEmail());
            return userRepository.save(currentUser);
        }
        return currentUser;
    }

    @Override
    public User updateCompanyEmail(Long id, User user) throws NotValidCompanyException, NotAllowedCompanyException, UpdateException {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (!optionalCompany.isPresent())
            throw new NotValidCompanyException("Company with ID:" + id + " doesn't exist!");

        Company currentCompany = optionalCompany.get();

        if (!checkTheUser(currentCompany.getUser()))
            throw new NotAllowedCompanyException("You don't have permissions to update!");


        User currentUser = currentCompany.getUser();

        User userWithNewEmail = updateEmail(currentUser, user);
        return userWithNewEmail;
    }

    @Override
    public Photo getInternshipLogo(Long id) throws NotValidInternshipException {
        Internship internship = findInternshipById(id);
        Contact contact = internship.getCompany().getContact();
        return contact.getPhoto();
    }

    @Override
    public Set<Skill> getAllSkillsForInternship(Long id) throws NotValidInternshipException {
        Internship internship = findInternshipById(id);
        return internship.getSkills();
    }

    @Override
    public Set<Requirement> getAllRequirementsForInternship(Long id) throws NotValidInternshipException {
        Internship internship = findInternshipById(id);
        return internship.getRequirements();
    }

    @Override
    public Set<Attribute> getAllAttributesForInternship(Long id) throws NotValidInternshipException {
        Internship internship = findInternshipById(id);
        return internship.getAttributes();
    }

    @Override
    public Set<Tag> getAllTagsForInternship(Long id) throws NotValidInternshipException {
        Internship internship = findInternshipById(id);
        return internship.getTags();
    }

    @Override
    public Internship getInternshipDetailsById(Long id) throws NotValidInternshipException {
        Internship internship = findInternshipById(id);
        return internship;
    }

    private void updateActive(Internship currentInternship, Internship newInternship) {
        if (newInternship.getActive() == null)
            return;
        currentInternship.setActive(newInternship.getActive());
    }

    private void updateTitle(Internship currentInternship, Internship newInternship) {
        if (newInternship.getTitle() == null)
            return;
        currentInternship.setTitle(newInternship.getTitle());
    }

    private void updateDescriptionInternship(Internship currentInternship, Internship newInternship) {
        if (newInternship.getDescription() == null)
            return;
        currentInternship.setDescription(newInternship.getDescription());
    }

    private void updateStartDate(Internship currentInternship, Internship newInternship) throws UpdateException {
        if (newInternship.getStartDate() == null)
            return;
        if (currentInternship.getEndDate() == null)
            currentInternship.setStartDate(newInternship.getStartDate());
        else {
            if (newInternship.getStartDate().isAfter(currentInternship.getEndDate()))
                throw new UpdateException("Start date cannot be after end date!");
            else
                currentInternship.setStartDate(newInternship.getStartDate());
        }
    }

    private void updateEndDate(Internship currentInternship, Internship newInternship) throws UpdateException {
        if (newInternship.getEndDate() == null)
            return;
        if (currentInternship.getStartDate() == null)
            throw new UpdateException("There is no starting date!");
        else {
            if (newInternship.getEndDate().isBefore(currentInternship.getStartDate()))
                throw new UpdateException("End date cannot be before start date!");
            else
                currentInternship.setEndDate(newInternship.getEndDate());
        }
    }

    private void updateDeadline(Internship currentInternship, Internship newInternship) throws UpdateException {
        if (newInternship.getDeadline() == null)
            return;
        if (currentInternship.getStartDate() == null)
            currentInternship.setDeadline(newInternship.getDeadline());
        else {
            if (newInternship.getDeadline().isAfter(currentInternship.getStartDate()))
                throw new UpdateException("Deadline date cannot be after start date!");
            else
                currentInternship.setDeadline(newInternship.getDeadline());
        }
    }

    private void updateFreeSpots(Internship currentInternship, Internship newInternship) throws UpdateException {
        if (newInternship.getFreeSpots() == null)
            return;
        if (newInternship.getFreeSpots() < 0)
            throw new UpdateException("Free spots number cannot be negative!");

        currentInternship.setFreeSpots(newInternship.getFreeSpots());
    }

    private void updateStatus(Internship currentInternship, Internship newInternship) {
        if (newInternship.getStatus() == null)
            return;
        currentInternship.setStatus(newInternship.getStatus());
    }

    private void updateEmploymentType(Internship currentInternship, Internship newInternship) {
        if (newInternship.getEmploymentType() == null)
            return;
        currentInternship.setEmploymentType(newInternship.getEmploymentType());
    }

    private Internship updateInternship(Internship currentInternship, Internship internship) throws UpdateException {
        updateActive(currentInternship, internship);
        updateTitle(currentInternship, internship);
        updateDescriptionInternship(currentInternship, internship);
        updateStartDate(currentInternship, internship);
        updateEndDate(currentInternship, internship);
        updateDeadline(currentInternship, internship);
        updateFreeSpots(currentInternship, internship);
        updateStatus(currentInternship, internship);
        updateEmploymentType(currentInternship, internship);
        return internshipRepository.saveAndFlush(currentInternship);
    }

    private void internshipUpdateChecks(Internship currentInternship) throws UpdateException {
        User internshipCompanyUser = currentInternship.getCompany().getUser();
        if (!checkTheUser(internshipCompanyUser))
            throw new UpdateException("Not allowed to do this operation!");
    }

    @Override
    public Internship updateInternshipSimpleDetails(Long internshipId, Internship internship) throws NotValidInternshipException, UpdateException {
        Internship currentInternship = findInternshipById(internshipId);
        internshipUpdateChecks(currentInternship);

        return updateInternship(currentInternship, internship);
    }

    @Override
    public Tag addTagToInternship(Long internshipId, Tag tag) throws NotValidInternshipException, UpdateException, NotValidTagException {
        Internship currentInternship = findInternshipById(internshipId);
        internshipUpdateChecks(currentInternship);
        Tag currentTag = findTagByName(tag.getName());
        currentInternship.addTag(currentTag);

        internshipRepository.saveAndFlush(currentInternship);
        return currentTag;
    }

    @Override
    public Tag removeTagFromInternship(Long internshipId, Long tagId) throws NotValidInternshipException, UpdateException, NotValidTagException {
        Internship currentInternship = findInternshipById(internshipId);
        internshipUpdateChecks(currentInternship);
        Tag currentTag = findTagById(tagId);
        currentInternship.removeTag(currentTag);

        internshipRepository.saveAndFlush(currentInternship);
        return currentTag;
    }

    @Override
    public Skill addSkillToInternship(Long internshipId, Skill skill) throws NotValidInternshipException, UpdateException, NotValidSkillException {
        Internship currentInternship = findInternshipById(internshipId);
        internshipUpdateChecks(currentInternship);
        Skill currentSkill = findSkillByName(skill.getName());
        currentInternship.addSkill(currentSkill);

        internshipRepository.saveAndFlush(currentInternship);
        return currentSkill;
    }

    @Override
    public Skill removeSkillFromInternship(Long internshipId, Long skillId) throws NotValidInternshipException, UpdateException, NotValidSkillException {
        Internship currentInternship = findInternshipById(internshipId);
        internshipUpdateChecks(currentInternship);
        Skill currentSkill = findSkillById(skillId);
        currentInternship.removeSkill(currentSkill);

        internshipRepository.saveAndFlush(currentInternship);
        return currentSkill;
    }

    @Override
    public Requirement addRequirementToInternship(Long internshipId, Requirement requirement) throws NotValidInternshipException, UpdateException {
        Internship currentInternship = findInternshipById(internshipId);
        internshipUpdateChecks(currentInternship);
        Requirement currentRequirement = requirementRepository.saveAndFlush(requirement);
        currentInternship.addRequirement(currentRequirement);
        internshipRepository.saveAndFlush(currentInternship);
        return currentRequirement;
    }

    @Override
    public Requirement removeRequirementFromInternship(Long internshipId, Long requirementId) throws NotValidInternshipException, UpdateException, NotValidRequirementException {
        Internship currentInternship = findInternshipById(internshipId);
        internshipUpdateChecks(currentInternship);
        Requirement currentRequirement = findRequirementById(requirementId);
        currentInternship.removeRequirement(currentRequirement);

        internshipRepository.saveAndFlush(currentInternship);
        return currentRequirement;
    }

    @Override
    public Attribute addAttributeToInternship(Long internshipId, Attribute attribute) throws NotValidInternshipException, UpdateException {
        Internship currentInternship = findInternshipById(internshipId);
        internshipUpdateChecks(currentInternship);
        Attribute currentAttribute = attributeRepository.saveAndFlush(attribute);

        currentInternship.addAttribute(currentAttribute);
        internshipRepository.saveAndFlush(currentInternship);
        return currentAttribute;
    }

    @Override
    public Attribute removeAttributeFromInternship(Long internshipId, Long attributeId) throws NotValidInternshipException, UpdateException, NotValidAttributeException {
        Internship currentInternship = findInternshipById(internshipId);
        internshipUpdateChecks(currentInternship);
        Attribute currentAttribute = findAttributeById(attributeId);
        currentInternship.removeAttribute(currentAttribute);

        internshipRepository.saveAndFlush(currentInternship);
        return currentAttribute;
    }

    @Override
    public Internship removeInternship(Long internshipId) throws NotValidInternshipException, UpdateException {
        Internship internship = findInternshipById(internshipId);
        internshipUpdateChecks(internship);

        Company company = internship.getCompany();
        company.removeInternship(internship);
        companyRepository.saveAndFlush(company);

        return internship;
    }


    @Override
    public Photo uploadPhotoForComapny(Long companyId, MultipartFile photoFile) throws NotValidCompanyException, NotAllowedApplicantException, NotValidContactException, NotValidImageUploadException {

        Company company = findCompanyById(companyId);

        if (!checkTheUser(company.getUser())) {
            throw new NotAllowedApplicantException("You don't have permissions to modify data!");
        }

        Contact contact = findContactByCompanyId(companyId);
        Photo photo = findPhotoByContactId(contact.getId());

        try {
            Map uploadResult = cloudinary.uploader().upload(photoFile.getBytes(), ObjectUtils.emptyMap());

            String photoUrl = (String) uploadResult.get("url");
            String public_id = (String) uploadResult.get("public_id");
            photo.setUrl(photoUrl);
            photo.setPublic_id(public_id);
            photo.setDeleted(false);

        } catch (IOException e) {
            throw new NotValidImageUploadException("Error when trying to upload the image");
        }

        photoRepository.saveAndFlush(photo);

        return photo;
    }

    @Override
    public Photo getCompanyPhoto(Long companyId) throws NotValidContactException, NotValidPhotoException {

        Contact contact = findContactByCompanyId(companyId);
        Photo photo = findPhotoByContactId(contact.getId());

        if (photo.isDeleted() == true) {
            throw new NotValidPhotoException("There is no photo uploaded.");
        }

        return photo;

    }

    @Override
    public Photo deleteCompanyPhoto(Long companyId) throws NotValidContactException, NotAllowedDeletingPhotoException {

        Contact contact = findContactByCompanyId(companyId);
        Photo photo = findPhotoByContactId(contact.getId());

        try {
            cloudinary.uploader().destroy(photo.getPublic_id(),
                    ObjectUtils.emptyMap());

            photo.setDeleted(true);
            photoRepository.saveAndFlush(photo);
        } catch (IOException e) {
            throw new NotAllowedDeletingPhotoException("Error when trying to delete photo");
        }

        return photo;

    }

    @Override
    public Comment addComment(Long internshipId, Comment comment) throws NotValidInternshipException {
        Internship internship = findInternshipById(internshipId);

        User user = getAuthenticatedUser();
        Comment resultComment = commentRepository.save(comment);
        user.addComment(resultComment);
        userRepository.save(user);

        internship.addComment(resultComment);
        internshipRepository.save(internship);

        return resultComment;
    }

    @Override
    public Comment addReplyComment(Comment comment, Long parentCommentId) throws NotValidCommentException {
        Comment parent = findCommentById(parentCommentId);
        Comment replyComment = commentRepository.save(comment);

        parent.addSubComment(replyComment);
        commentRepository.save(replyComment);

        User user = getAuthenticatedUser();
        user.addComment(replyComment);
        userRepository.save(user);

        return replyComment;
    }

    @Override
    public List getAllCommentsForInternship(Long internshipId) throws NotValidInternshipException {
        Internship internship = findInternshipById(internshipId);
        Set<Comment> comments = internship.getComments();
        List<Comment> sortedList = new ArrayList<Comment>(comments);
        Collections.sort(sortedList, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                if (o1.getId().equals(o2.getId()))
                    return 0;
                return o1.getId() < o2.getId() ? -1 : 1;
            }
        });
        return sortedList;
    }


    private void initializeNullValuesForAddress(Address address) {
        if (address.getNumber() == null)
            address.setNumber("");
        if (address.getApartment() == null)
            address.setApartment("");
        if (address.getBlock() == null)
            address.setBlock("");
        if (address.getCountry() == null)
            address.setCountry("");
        if (address.getCounty() == null)
            address.setCounty("");
        if (address.getEntrance() == null)
            address.setEntrance("");
        if (address.getFloor() == null)
            address.setFloor("");
        if (address.getTown() == null)
            address.setTown("");
        if (address.getSector() == null)
            address.setSector("");
        if (address.getStreet() == null)
            address.setStreet("");
    }

    private void updateNumber(Address currentAddress, Address address) {
        if (address.getNumber() != null && !currentAddress.getNumber().equals(address.getNumber()))
            currentAddress.setNumber(address.getNumber());
    }

    private void updateApartment(Address currentAddress, Address address) {
        if (address.getApartment() != null && !currentAddress.getApartment().equals(address.getApartment()))
            currentAddress.setApartment(address.getApartment());
    }

    private void updateBlock(Address currentAddress, Address address) {
        if (address.getBlock() != null && !currentAddress.getBlock().equals(address.getBlock()))
            currentAddress.setBlock(address.getBlock());
    }

    private void updateCountry(Address currentAddress, Address address) {
        if (address.getCountry() != null && !currentAddress.getCountry().equals(address.getCountry()))
            currentAddress.setCountry(address.getCountry());
    }

    private void updateCounty(Address currentAddress, Address address) {
        if (address.getCounty() != null && !currentAddress.getCounty().equals(address.getCounty()))
            currentAddress.setCounty(address.getCounty());
    }

    private void updateEntrance(Address currentAddress, Address address) {
        if (address.getEntrance() != null && !currentAddress.getEntrance().equals(address.getEntrance()))
            currentAddress.setEntrance(address.getEntrance());
    }

    private void updateFloor(Address currentAddress, Address address) {
        if (address.getFloor() != null && !currentAddress.getFloor().equals(address.getFloor()))
            currentAddress.setFloor(address.getFloor());
    }

    private void updateTown(Address currentAddress, Address address) {
        if (address.getTown() != null && !currentAddress.getTown().equals(address.getTown()))
            currentAddress.setTown(address.getTown());
    }

    private void updateSector(Address currentAddress, Address address) {
        if (address.getSector() != null && !currentAddress.getSector().equals(address.getSector()))
            currentAddress.setSector(address.getSector());
    }

    private void updateStreet(Address currentAddress, Address address) {
        if (address.getStreet() != null && !currentAddress.getStreet().equals(address.getStreet()))
            currentAddress.setStreet(address.getStreet());
    }


    private void updateAddress(Address currentAddress, Address address) {
        initializeNullValuesForAddress(currentAddress);

        if (address != null) {
            updateNumber(currentAddress, address);
            updateFloor(currentAddress, address);
            updateCounty(currentAddress, address);
            updateCountry(currentAddress, address);
            updateStreet(currentAddress, address);
            updateSector(currentAddress, address);
            updateBlock(currentAddress, address);
            updateEntrance(currentAddress, address);
            updateApartment(currentAddress, address);
            updateTown(currentAddress, address);
            if(!currentAddress.toString().equals("")) {
                LatLng latLng = getCoordinatesForAddress(currentAddress);
                currentAddress.setLongitude(latLng.lng);
                currentAddress.setLatitude(latLng.lat);
            }
        }
        addressRepository.save(currentAddress);
    }

    @Override
    public Set<Skill> getApplicantSkills(Long id) throws NotValidApplicantException, NotAllowedApplicantException {
        Applicant applicant = findApplicantById(id);
        return applicant.getSkills();
    }

    @Override
    public List<Education> getApplicantEducations(Long id) throws NotValidApplicantException, NotAllowedApplicantException {
        Applicant applicant = findApplicantById(id);

        List<Education> sortedListOfEducationsForAnApplicant = applicant.getEducations().stream().sorted((a, b) -> {

            if (a.getStartDate().isBefore(b.getStartDate())) return 1;
            else if (a.getStartDate().isEqual(b.getStartDate())) return 0;
            return -1;
        }).collect(Collectors.toList());

        return sortedListOfEducationsForAnApplicant;
    }

    private void initRoles() {
        Role roleAdmin = new Role.Builder()
                .withRoleString(RoleString.ADMIN)
                .build();
        roleRepository.save(roleAdmin);
        Role roleApplicant = new Role.Builder()
                .withRoleString(RoleString.APPLICANT)
                .build();
        roleRepository.save(roleApplicant);
        Role roleCompany = new Role.Builder()
                .withRoleString(RoleString.COMPANY)
                .build();
        roleRepository.save(roleCompany);
    }

    private void createApplicantsUser() {

        User userApplicant = new User.Builder()
                .isActive(true)
                .withUsername("applicant")
                .withEmail("chise_b@yahoo.com")
                .withPassword(SecurityConfig.passwordEncoder().encode("applicant"))
                .addRole(roleRepository.findByRoleStringEquals(RoleString.APPLICANT))
                .build();
        userRepository.save(userApplicant);

        User userApplicant2 = new User.Builder()
                .isActive(true)
                .withUsername("applicant1")
                .withEmail("applicant1@yuhuu.com")
                .withPassword(SecurityConfig.passwordEncoder().encode("applicant1"))
                .addRole(roleRepository.findByRoleStringEquals(RoleString.APPLICANT))
                .build();
        userRepository.save(userApplicant2);

        User userApplicant3 = new User.Builder()
                .isActive(true)
                .withUsername("natalie")
                .withEmail("natalie@yuhuu.com")
                .withPassword(SecurityConfig.passwordEncoder().encode("natalie"))
                .addRole(roleRepository.findByRoleStringEquals(RoleString.APPLICANT))
                .build();
        userRepository.save(userApplicant3);


    }

    private void initCompaniesUser() {

        User userCompany = new User.Builder()
                .isActive(true)
                .withUsername("company")
                .withEmail("company@yuhuu.com")
                .withPassword(SecurityConfig.passwordEncoder().encode("company"))
                .addRole(roleRepository.findByRoleStringEquals(RoleString.COMPANY))
                .build();
        userRepository.save(userCompany);

        User userCompany1 = new User.Builder()
                .isActive(true)
                .withUsername("company1")
                .withEmail("company1@yuhuu.com")
                .withPassword(SecurityConfig.passwordEncoder().encode("company1"))
                .addRole(roleRepository.findByRoleStringEquals(RoleString.COMPANY))
                .build();
        userRepository.save(userCompany1);

        User userCompany2 = new User.Builder()
                .isActive(true)
                .withUsername("tesla")
                .withEmail("tesla@yuhuu.com")
                .withPassword(SecurityConfig.passwordEncoder().encode("tesla"))
                .addRole(roleRepository.findByRoleStringEquals(RoleString.COMPANY))
                .build();
        userRepository.save(userCompany2);

        User userCompany3 = new User.Builder()
                .isActive(true)
                .withUsername("yonder")
                .withEmail("yonder@yuhuu.com")
                .withPassword(SecurityConfig.passwordEncoder().encode("yonder"))
                .addRole(roleRepository.findByRoleStringEquals(RoleString.COMPANY))
                .build();
        userRepository.save(userCompany3);

        User userCompany4 = new User.Builder()
                .isActive(true)
                .withUsername("optopt")
                .withEmail("optopt@yuhuu.com")
                .withPassword(SecurityConfig.passwordEncoder().encode("optopt"))
                .addRole(roleRepository.findByRoleStringEquals(RoleString.COMPANY))
                .build();
        userRepository.save(userCompany4);

        User userCompany5 = new User.Builder()
                .isActive(true)
                .withUsername("amazon")
                .withEmail("amazon@yuhuu.com")
                .withPassword(SecurityConfig.passwordEncoder().encode("amazon"))
                .addRole(roleRepository.findByRoleStringEquals(RoleString.COMPANY))
                .build();
        userRepository.save(userCompany5);

        User userCompany6 = new User.Builder()
                .isActive(true)
                .withUsername("telenav")
                .withEmail("telenav@yuhuu.com")
                .withPassword(SecurityConfig.passwordEncoder().encode("telenav"))
                .addRole(roleRepository.findByRoleStringEquals(RoleString.COMPANY))
                .build();
        userRepository.save(userCompany6);

    }

    private void initUsers() {
        User userAdmin = new User.Builder()
                .isActive(true)
                .withUsername("admin")
                .withEmail("admin@yuhuu.com")
                .withPassword(SecurityConfig.passwordEncoder().encode("admin"))
                .addRole(roleRepository.findByRoleStringEquals(RoleString.ADMIN))
                .build();
        userRepository.save(userAdmin);

        initCompaniesUser();
        createApplicantsUser();

    }

    private void initCompanies() {
        Company company = new Company.Builder()
                .withDescription("Betfair is an online gambling company which operates the world's largest online betting exchange. It also offers a Sportsbook (fixed odds betting), online casino, online poker and online bingo. The company's headquarters are located in Hammersmith in West London, United Kingdom and Clonskeagh, Dublin.\n" +
                        "\n" +
                        "It was listed on the London Stock Exchange as Betfair Group plc, until it merged with Paddy Power to form Paddy Power Betfair on 2 February 2016. ")
                .withDimension((long) 10031)
                .withName("Betfair")
                .build();
        Optional<User> user = userRepository.findByUsername("company");
        user.ifPresent(userAux -> userAux.setCompany(company));
        companyRepository.saveAndFlush(company);

        Company company1 = new Company.Builder()
                .withDescription("Envisioning a world where businesses have technology at their core – we’ve made it our mission to be part of this momentum and accelerate your digital transformation journey")
                .withDimension((long) 10032)
                .withName("Accesa")
                .build();
        Optional<User> user1 = userRepository.findByUsername("company1");
        user1.ifPresent(userAux -> userAux.setCompany(company1));
        companyRepository.saveAndFlush(company1);

        Company company2 = new Company.Builder()
                .withDescription("Tesla, Inc., formerly Tesla Motors, Inc., incorporated on July 1, 2003, designs, develops, manufactures and sells fully electric vehicles, and energy storage systems, as well as installs, operates and maintains solar and energy storage products.")
                .withDimension((long) 50000)
                .withName("Tesla")
                .build();
        Optional<User> user2 = userRepository.findByUsername("tesla");
        user2.ifPresent(userAux -> userAux.setCompany(company2));
        companyRepository.saveAndFlush(company2);

        Company company3 = new Company.Builder()
                .withDescription("8x8 Inc. is a provider of cloud communications and customer engagement solutions. 8x8 solutions include cloud-based voice, contact center, video, mobile and unified communications for small, medium to enterprise businesses.")
                .withDimension((long) 1000)
                .withName("8X8")
                .build();
        Optional<User> user3 = userRepository.findByUsername("optopt");
        user3.ifPresent(userAux -> userAux.setCompany(company3));
        companyRepository.saveAndFlush(company3);

        Company company4 = new Company.Builder()
                .withDescription("Yonder is the development partner of choice for software vendors. Yonder relies on superior technical skills and in-depth understanding of business principles to help you realize your ambitions.")
                .withDimension((long) 400)
                .withName("Yonder")
                .build();
        Optional<User> user4 = userRepository.findByUsername("yonder");
        user4.ifPresent(userAux -> userAux.setCompany(company4));
        companyRepository.saveAndFlush(company4);

        Company company5 = new Company.Builder()
                .withDescription("We're a company of pioneers. It's our job to make bold bets, and we get our energy from inventing on behalf of customers. Success is measured against the possible, not the probable. For today’s pioneers, that’s exactly why there’s no place on Earth they’d rather build than Amazon.")
                .withDimension((long) 100000)
                .withName("Amazon")
                .build();
        Optional<User> user5 = userRepository.findByUsername("amazon");
        user5.ifPresent(userAux -> userAux.setCompany(company5));
        companyRepository.saveAndFlush(company5);

        Company company6 = new Company.Builder()
                .withDescription("We embrace new ideas, challenge the status quo and bring a Silicon Valley mindset to help global automotive leaders deliver personalized in-car experiences for drivers.")
                .withDimension((long) 500)
                .withName("Telenav")
                .build();
        Optional<User> user6 = userRepository.findByUsername("telenav");
        user6.ifPresent(userAux -> userAux.setCompany(company6));
        companyRepository.saveAndFlush(company6);
    }

    private void initContactForCompanies() {
        Contact contact = new Contact.Builder()
                .withWebsite("https://www.betfair.ro")
                .withPhoneNumber("00 44 20 3059 8888")
                .withLinkedinLink("https://www.linkedin.com/company/betfair")
                .withFacebookLink("https://www.facebook.com/BetfairRO")
                .build();
        Optional<User> user = userRepository.findByUsername("company");
        user.ifPresent(userAux -> userAux.getCompany().setContact(contact));
        contactRepository.save(contact);

        Contact contact1 = new Contact.Builder()
                .withWebsite("https://www.accesa.eu")
                .withLinkedinLink("https://www.linkedin.com/company/accesa-eu")
                .withPhoneNumber("0364 115 115")
                .withFacebookLink("https://www.facebook.com/accesa.eu")
                .build();
        Optional<User> user1 = userRepository.findByUsername("company1");
        user1.ifPresent(userAux -> userAux.getCompany().setContact(contact1));
        contactRepository.save(contact1);

        Contact contact2 = new Contact.Builder()
                .withWebsite("https://www.tesla.com")
                .withPhoneNumber("650 681 5100")
                .withLinkedinLink("https://www.linkedin.com/company/tesla-motors")
                .withFacebookLink("https://www.facebook.com/TeslaMoto")
                .build();
        Optional<User> user2 = userRepository.findByUsername("tesla");
        user2.ifPresent(userAux -> userAux.getCompany().setContact(contact2));
        contactRepository.save(contact2);

        Contact contact3 = new Contact.Builder()
                .withWebsite("https://tss-yonder.com")
                .withLinkedinLink("https://www.linkedin.com/company/yonder")
                .withPhoneNumber(" 0264 599 351")
                .withFacebookLink("https://www.facebook.com/tssyonder")
                .build();
        Optional<User> user3 = userRepository.findByUsername("yonder");
        user3.ifPresent(userAux -> userAux.getCompany().setContact(contact3));
        contactRepository.save(contact3);

        Contact contact4 = new Contact.Builder()
                .withWebsite("https://www.amazon.com")
                .withLinkedinLink("https://www.linkedin.com/company/amazon")
                .withPhoneNumber("00 49 941 788788")
                .withFacebookLink("https://www.facebook.com/Amazon")
                .build();
        Optional<User> user4 = userRepository.findByUsername("amazon");
        user4.ifPresent(userAux -> userAux.getCompany().setContact(contact4));
        contactRepository.save(contact4);

        Contact contact5 = new Contact.Builder()
                .withWebsite("https://www.8x8.com")
                .withPhoneNumber("00 1 408 687 4120")
                .withLinkedinLink("https://www.linkedin.com/company/8x8")
                .withFacebookLink("https://www.facebook.com/8x8Inc")
                .build();
        Optional<User> user5 = userRepository.findByUsername("optopt");
        user5.ifPresent(userAux -> userAux.getCompany().setContact(contact5));
        contactRepository.save(contact5);

        Contact contact6 = new Contact.Builder()
                .withWebsite("https://www.telenav.com")
                .withLinkedinLink("https://www.linkedin.com/company/telenav")
                .withPhoneNumber("+40 0 364 730 191")
                .withFacebookLink("https://www.facebook.com/telenav/")
                .build();
        Optional<User> user6 = userRepository.findByUsername("telenav");
        user6.ifPresent(userAux -> userAux.getCompany().setContact(contact6));
        contactRepository.save(contact6);
    }

    private void initPhotoForCompanies() {
        Photo photo = new Photo();
        photo.setUrl("https://res.cloudinary.com/yuhuubackend/image/upload/v1547739876/betfair.jpg");
        Optional<User> user = userRepository.findByUsername("company");
        user.ifPresent(userAux -> userAux.getCompany().getContact().setPhoto(photo));
        photoRepository.save(photo);

        Photo photo1 = new Photo();
        photo1.setUrl("https://res.cloudinary.com/yuhuubackend/image/upload/v1547234801/accesa_logo_bg.png");
        Optional<User> user1 = userRepository.findByUsername("company1");
        user1.ifPresent(userAux -> userAux.getCompany().getContact().setPhoto(photo1));
        photoRepository.save(photo1);

        Photo photo2 = new Photo();
        photo2.setUrl("https://res.cloudinary.com/yuhuubackend/image/upload/v1547744913/tesla.jpg");
        Optional<User> user2 = userRepository.findByUsername("tesla");
        user2.ifPresent(userAux -> userAux.getCompany().getContact().setPhoto(photo2));
        photoRepository.save(photo2);

        Photo photo3 = new Photo();
        photo3.setUrl("https://res.cloudinary.com/yuhuubackend/image/upload/v1547744997/younder.jpg");
        Optional<User> user3 = userRepository.findByUsername("yonder");
        user3.ifPresent(userAux -> userAux.getCompany().getContact().setPhoto(photo3));
        photoRepository.save(photo3);

        Photo photo4 = new Photo();
        photo4.setUrl("https://res.cloudinary.com/yuhuubackend/image/upload/v1547745563/amazon.png");
        Optional<User> user4 = userRepository.findByUsername("amazon");
        user4.ifPresent(userAux -> userAux.getCompany().getContact().setPhoto(photo4));
        photoRepository.save(photo4);

        Photo photo5 = new Photo();
        photo5.setUrl("https://res.cloudinary.com/yuhuubackend/image/upload/v1547745645/optopt.jpg");
        Optional<User> user5 = userRepository.findByUsername("optopt");
        user5.ifPresent(userAux -> userAux.getCompany().getContact().setPhoto(photo5));
        photoRepository.save(photo5);

        Photo photo6 = new Photo();
        photo6.setUrl("https://res.cloudinary.com/yuhuubackend/image/upload/v1547745721/telenav.png");
        Optional<User> user6 = userRepository.findByUsername("telenav");
        user6.ifPresent(userAux -> userAux.getCompany().getContact().setPhoto(photo6));
        photoRepository.save(photo6);


    }

    private void initAddressesForCompanies() {

        String[] countryName = {"Romania", "Romania", "Romania", "Romania", "Romania", "Romania", "Romania"};
        String[] countyName = {"Cluj","Cluj", "Bucharest","Cluj", "Iasi", "Cluj", "Cluj"};
        String[] townName = {"Cluj-Napoca", "Cluj-Napoca", "","Cluj-Napoca", "Iasi", "Cluj-Napoca", "Cluj-Napoca"};
        String[] streetName = {"Bulevardul 21 Decembrie 1989", "Alexandru Vaida Voevod", "", "Bulevardul 21 Decembrie 1989",
                                "Strada Sfântul Lazăr", "Bulevardul 21 Decembrie 1989", "Bulevardul 21 Decembrie 1989"};
        String[] numberName = {"","16", "", "77", "27", "77", "77"};
        String[] postalCodeName = {"400604","400592", "", "700045", "400124", "400124", "400124"};
        String[] usernameArray = {"company","company1","tesla","yonder", "amazon", "optopt", "telenav"};

        int i;

        for(i=0; i<countryName.length; i++){

            String country = countryName[i];
            String county = countyName[i];
            String town = townName[i];
            String street = streetName[i];
            String number = numberName[i];
            String postalCode = postalCodeName[i];
            String username = usernameArray[i];

            Address address = new Address.Builder()
                    .withCountry(country)
                    .withCounty(county)
                    .withTown(town)
                    .withStreet(street)
                    .withNumber(number)
                    .withPostalCode(postalCode)
                    .build();

            Optional<User> user = userRepository.findByUsername(username);
            user.ifPresent(userAux -> userAux.getCompany().getContact().setAddress(address));
            if(address!=null && !(address.toString().equals(""))){
                LatLng latLng = Geocoding.get(address.toString());
                address.setLatitude(latLng.lat);
                address.setLongitude(latLng.lng);
            }
            addressRepository.save(address);

        }
    }

    private void initSkills() {

        String[] skills = {"Bash", "Java", "C#", "Javascript", "Hadoop", "F#", "Erlang", "Communicative", "Positive Attitude", "Flexibility", "Python", "C++", "Swift", "Android", "TypeScript", "Go", "SQL", "Ruby", "R", "PHP", "Perl", "Kotlin", "Rust", "Scala", "Elixir", "Elm"};

        for (String skillName : skills) {
            Skill skill = new Skill();
            skill.setName(skillName);
            skillRepository.save(skill);
        }

    }

    private void initTags() {

        String[] tags = {"Bash", "Java", "C#", "Javascript", "Hadoop", "F#", "Erlang", "Communicative", "Positive Attitude", "Flexibility", "Python", "C++", "Swift", "Android", "TypeScript", "Go", "SQL", "Ruby", "R", "PHP", "Perl", "Kotlin", "Rust", "Scala", "Elixir", "Elm"};

        for (String tagName : tags) {
            Tag tag = new Tag();
            tag.setName(tagName);
            tagRepository.save(tag);
        }
    }

    private void initAttribute() {
        Attribute attribute = new Attribute();
        attribute.setName("Attribute");
        attributeRepository.save(attribute);
    }

    private void initRequirements() {
        Requirement requirement = new Requirement();
        requirement.setName("Requirement");
        requirementRepository.save(requirement);

        Requirement requirement1 = new Requirement();
        requirement1.setName("Good english communication skills");
        requirementRepository.save(requirement1);

        Requirement requirement2 = new Requirement();
        requirement2.setName("Eager to learn new things");
        requirementRepository.save(requirement2);
    }

    private void initExperience() {
        Experience experience = new Experience();
        experience.setCompanyTitle("Betfair");
        experience.setPosition("Intern");
        experience.setStartDate(LocalDate.of(2018, 9, 1));
        experience.setEndDate(null);//PRESENT,NOT ENDED

        Experience experienceResult = experienceRepository.save(experience);
        Optional<User> user = userRepository.findByUsername("applicant");
        if (user.isPresent()) {
            Applicant applicant = user.get().getApplicant();
            applicant.addExperience(experienceResult);
            applicantRepository.save(applicant);
        }
    }

    private void initEducationForFirstUser(){

        Education education0 = new Education.Builder()
                .withDegree("Mathematics")
                .withSchoolLocation("Cluj-Napoca")
                .withSchoolTitle("Nicolae Balcescu Theoretical High School")
                .withStartDate(LocalDate.of(2012, 9, 12))
                .withEndDate(LocalDate.of(2016, 7, 1))
                .build();

        Education education = new Education.Builder()
                .withDegree("Computer Science, Bachelor degree, Computer Science")
                .withSchoolLocation("Cluj-Napoca")
                .withSchoolTitle("Babes Bolyai University")
                .withEndDate(LocalDate.of(2019, 10, 12))
                .withStartDate(LocalDate.of(2016, 7, 1))
                .build();

        Education education1 = new Education.Builder()
                .withDegree("Computer Science, Master degree, Computer Science")
                .withSchoolLocation("Cluj-Napoca")
                .withSchoolTitle("Babes Bolyai University")
                .withStartDate(LocalDate.of(2019, 10, 15))
                .withEndDate(LocalDate.of(2021, 7, 1))
                .build();

        Education education2 = new Education.Builder()
                .withDegree("Computer Science, Doctoral degree, Computer Science")
                .withSchoolLocation("Cluj-Napoca")
                .withSchoolTitle("Babes Bolyai University")
                .withStartDate(LocalDate.of(2021, 10, 15))
                .withEndDate(LocalDate.of(2023, 7, 1))
                .build();


        Education educationResult0 = educationRepository.save(education0);
        Education educationResult = educationRepository.save(education);
        Education educationResult1 = educationRepository.save(education1);
        Education educationResult2 = educationRepository.save(education2);

        Optional<User> user = userRepository.findByUsername("applicant");
        if (user.isPresent()) {
            Applicant applicant = user.get().getApplicant();

            applicant.addEducation(educationResult0);
            applicant.addEducation(educationResult);
            applicant.addEducation(educationResult1);
            applicant.addEducation(educationResult2);

            applicantRepository.save(applicant);
        }

    }

    private void initEducationForSecondUser(){

        Education education0 = new Education.Builder()
                .withDegree("Languages")
                .withSchoolLocation("Oradea")
                .withSchoolTitle("Onisifor Ghibu High School")
                .withStartDate(LocalDate.of(2012, 9, 12))
                .withEndDate(LocalDate.of(2016, 7, 1))
                .build();

        Education education = new Education.Builder()
                .withDegree("Foreign, Bachelor degree, Computer Science")
                .withSchoolLocation("Cluj-Napoca")
                .withSchoolTitle("Babes Bolyai University")
                .withEndDate(LocalDate.of(2019, 10, 12))
                .withStartDate(LocalDate.of(2016, 7, 1))
                .build();

        Education education1 = new Education.Builder()
                .withDegree("Marketing, Master degree, Computer Science")
                .withSchoolLocation("Texas, USA")
                .withSchoolTitle("Princeton Bolyai University")
                .withStartDate(LocalDate.of(2019, 10, 15))
                .withEndDate(LocalDate.of(2021, 7, 1))
                .build();

        Education education2 = new Education.Builder()
                .withDegree("Computer Science, Bachelor degree, Computer Science")
                .withSchoolLocation("Cluj-Napoca")
                .withSchoolTitle("Babes Bolyai University")
                .withStartDate(LocalDate.of(2021, 10, 15))
                .withEndDate(LocalDate.of(2023, 7, 1))
                .build();


        Education educationResult0 = educationRepository.save(education0);
        Education educationResult = educationRepository.save(education);
        Education educationResult1 = educationRepository.save(education1);
        Education educationResult2 = educationRepository.save(education2);

        Optional<User> user = userRepository.findByUsername("applicant1");
        if (user.isPresent()) {
            Applicant applicant = user.get().getApplicant();

            applicant.addEducation(educationResult0);
            applicant.addEducation(educationResult);
            applicant.addEducation(educationResult1);
            applicant.addEducation(educationResult2);

            applicantRepository.save(applicant);
        }


    }

    private void initEducations() {

        initEducationForFirstUser();
        initEducationForSecondUser();

    }

    private void initFirstInternship() {
        Internship internship = new Internship.Builder()
                .isActive(true)
                .withTitle("Java Internship")
                .withDescription("The 2019 Summer Java Developer Internship, will integrate Java into business applications, software and websites. The internship provides students the opportunity to work through the entire development life cycle of an application; while identifying and analyzing application problems with the help of a Software Engineer mentor. Other duties may include documenting user requirements, completing application change requests and quality assurance testing.")
                .withStartDate(LocalDate.now().minusDays(3))
                .withEndDate(LocalDate.now().plusWeeks(2))
                .withDeadline(LocalDate.now().plusWeeks(1).plusDays(2))
                .withEmploymentType(EmploymentType.INTERNSHIP)
                .withFreeSpots(2)
                .withStatus(InternshipStatus.ACTIVE)
                .build();

        Internship internshipAux = internshipRepository.save(internship);

        Optional<Skill> skillAux = skillRepository.findByNameEquals("Java");
        Optional<Skill> skillAux1 = skillRepository.findByNameEquals("Kotlin");
        Optional<Skill> skillAux2 = skillRepository.findByNameEquals("Android");
        Optional<Skill> skillAux3 = skillRepository.findByNameEquals("Flexibility");
        Optional<Skill> skillAux4 = skillRepository.findByNameEquals("Positive Attitude");

        Optional<Tag> tagAux = tagRepository.findByNameEquals("Java");
        Optional<Tag> tagAux1 = tagRepository.findByNameEquals("Javascript");
        Optional<Tag> tagAux2 = tagRepository.findByNameEquals("Bash");

        Optional<Attribute> attributeAux = attributeRepository.findByNameEquals("Attribute");
        Optional<Requirement> requirementAux = requirementRepository.findByNameEquals("Requirement");
        Optional<Internship> internshipResult = internshipRepository.findById(internshipAux.getId());

        if (internshipResult.isPresent() && skillAux.isPresent() && tagAux.isPresent() &&
                attributeAux.isPresent() && requirementAux.isPresent()) {

            internshipResult.get().addSkill(skillAux.get());
            internshipResult.get().addSkill(skillAux1.get());
            internshipResult.get().addSkill(skillAux2.get());
            internshipResult.get().addSkill(skillAux3.get());
            internshipResult.get().addSkill(skillAux4.get());

            internshipResult.get().addTag(tagAux.get());
            internshipResult.get().addTag(tagAux1.get());
            internshipResult.get().addTag(tagAux2.get());


            internshipResult.get().addAttribute(attributeAux.get());
            internshipResult.get().addRequirement(requirementAux.get());

            Optional<User> user = userRepository.findByUsername("company");
            user.ifPresent(userAux -> userAux.getCompany().addInternship(internshipResult.get()));
            internshipRepository.save(internshipResult.get());
        }
    }

    private void initSecondInternship() {

        Internship internship = new Internship.Builder()
                .isActive(true)
                .withTitle("C# Internship")
                .withDescription("Join the Garage internship where you can shine a light on your talent and be challenged in ways you never imagined. This is a unique internship designed to put your cutting edge development and design skills to the challenge to rapidly prototype and build a product on the latest Microsoft platforms. You will work in a small team with other Garage interns and be responsible for creating a product end-to-end, starting with product definition, working through design and development; and if successful, wrapping up with the public launch of the product. Coaches and technical leaders from across Microsoft provide guidance and expertise along the way.")
                .withStartDate(LocalDate.now().minusDays(1))
                .withEndDate(LocalDate.now().plusWeeks(3))
                .withDeadline(LocalDate.now().plusWeeks(2))
                .withStatus(InternshipStatus.ACTIVE)
                .withFreeSpots(3)
                .withEmploymentType(EmploymentType.INTERNSHIP)
                .build();

        Internship internshipAux = internshipRepository.save(internship);

        Optional<Skill> skillAux = skillRepository.findByNameEquals("C#");
        Optional<Skill> skillAux1 = skillRepository.findByNameEquals("Bash");
        Optional<Skill> skillAux2 = skillRepository.findByNameEquals("Flexibility");
        Optional<Skill> skillAux3 = skillRepository.findByNameEquals("Positive Attitude");
        Optional<Skill> skillAux4 = skillRepository.findByNameEquals("Javascript");
        Optional<Skill> skillAux5 = skillRepository.findByNameEquals("TypeScript");

        Optional<Tag> tag1Aux = tagRepository.findByNameEquals("C#");
        Optional<Tag> tag2Aux = tagRepository.findByNameEquals("Bash");
        Optional<Tag> tag3Aux = tagRepository.findByNameEquals("Javascript");
        Optional<Tag> tag4Aux = tagRepository.findByNameEquals("TypeScript");

        Optional<Attribute> attributeAux = attributeRepository.findByNameEquals("Attribute");
        Optional<Requirement> requirement1Aux = requirementRepository.findByNameEquals("Good english communication skills");
        Optional<Requirement> requirement2Aux = requirementRepository.findByNameEquals("Eager to learn new things");

        Optional<Internship> internshipResult = internshipRepository.findById(internshipAux.getId());

        if (internshipResult.isPresent() && skillAux.isPresent() && tag1Aux.isPresent() && tag2Aux.isPresent() &&
                attributeAux.isPresent() && requirement1Aux.isPresent() && requirement2Aux.isPresent()) {

            internshipResult.get().addSkill(skillAux.get());
            internshipResult.get().addSkill(skillAux1.get());
            internshipResult.get().addSkill(skillAux2.get());
            internshipResult.get().addSkill(skillAux3.get());
            internshipResult.get().addSkill(skillAux4.get());
            internshipResult.get().addSkill(skillAux5.get());

            internshipResult.get().addTag(tag1Aux.get());
            internshipResult.get().addTag(tag2Aux.get());
            internshipResult.get().addTag(tag3Aux.get());
            internshipResult.get().addTag(tag4Aux.get());

            internshipResult.get().addAttribute(attributeAux.get());

            internshipResult.get().addRequirement(requirement1Aux.get());
            internshipResult.get().addRequirement(requirement2Aux.get());

            Optional<User> user = userRepository.findByUsername("company1");
            user.ifPresent(userAux -> userAux.getCompany().addInternship(internshipResult.get()));
            internshipRepository.save(internshipResult.get());
        }

    }

    private void initThirdInternship() {
        Internship internship = new Internship.Builder()
                .isActive(true)
                .withTitle("Angular Internship")
                .withDescription("We are looking for an AngularJS Developer responsible for the client side of our service. Your primary focus will be to implement a complete user interface in the form of a mobile and desktop web app, with a focus on performance. Your main duties will include creating modules and components and coupling them together into a functional app. The artistic design will be delivered to you, together with a few HTML templates, but we will ask for your help in regard to animations, CSS, and final HTML output. You will work in a team with the back-end developer, and communicate with the API using standard methods. A thorough understanding of all of the components of our platform and infrastructure is required.")
                .withStartDate(LocalDate.now().minusDays(3))
                .withEndDate(LocalDate.now().plusWeeks(2))
                .withDeadline(LocalDate.now().plusWeeks(1).plusDays(2))
                .withEmploymentType(EmploymentType.INTERNSHIP)
                .withFreeSpots(4)
                .withStatus(InternshipStatus.ACTIVE)
                .build();

        Internship internshipAux = internshipRepository.save(internship);


        Optional<Skill> skillAux3 = skillRepository.findByNameEquals("Flexibility");
        Optional<Skill> skillAux4 = skillRepository.findByNameEquals("Positive Attitude");

        Optional<Tag> tag2Aux = tagRepository.findByNameEquals("Bash");
        Optional<Tag> tag3Aux = tagRepository.findByNameEquals("Javascript");
        Optional<Tag> tag4Aux = tagRepository.findByNameEquals("TypeScript");


        Optional<Attribute> attributeAux = attributeRepository.findByNameEquals("Attribute");
        Optional<Requirement> requirementAux = requirementRepository.findByNameEquals("Requirement");
        Optional<Internship> internshipResult = internshipRepository.findById(internshipAux.getId());

        if (internshipResult.isPresent() &&
                attributeAux.isPresent() && requirementAux.isPresent()) {

            internshipResult.get().addSkill(skillAux3.get());
            internshipResult.get().addSkill(skillAux4.get());

            internshipResult.get().addTag(tag2Aux.get());
            internshipResult.get().addTag(tag3Aux.get());
            internshipResult.get().addTag(tag4Aux.get());

            internshipResult.get().addAttribute(attributeAux.get());
            internshipResult.get().addRequirement(requirementAux.get());

            Optional<User> user = userRepository.findByUsername("optopt");
            user.ifPresent(userAux -> userAux.getCompany().addInternship(internshipResult.get()));
            internshipRepository.save(internshipResult.get());
        }
    }


    private void initInternship() {
        initFirstInternship();
        initSecondInternship();
        initThirdInternship();
    }

    private void initApplicants() {
        Applicant applicant = new Applicant.Builder()
                .withFirstName("Chise")
                .withLastName("Bogdan")
                .withBirthday(LocalDate.of(1997, 7, 26))
                .withDescription("Simply clever!!!" +
                        "\nExperienced manager with a demonstrated history of working in the higher education and software development industry. Strong education professional skilled in Agile and Predictive Project Management.")
                .build();
        Optional<User> user = userRepository.findByUsername("applicant");
        user.ifPresent(userAux -> userAux.setApplicant(applicant));
        applicantRepository.saveAndFlush(applicant);

        Applicant applicant1 = new Applicant.Builder()
                .withFirstName("Otniel")
                .withLastName("Boros")
                .withBirthday(LocalDate.of(1997, 1, 1))
                .build();
        Optional<User> user1 = userRepository.findByUsername("applicant1");
        user1.ifPresent(userAux -> userAux.setApplicant(applicant1));
        applicantRepository.saveAndFlush(applicant1);

        Applicant applicant2 = new Applicant.Builder()
                .withFirstName("Natalie")
                .withLastName("Portman")
                .withBirthday(LocalDate.of(1981, 6, 9))
                .build();
        Optional<User> user2 = userRepository.findByUsername("natalie");
        user2.ifPresent(userAux -> userAux.setApplicant(applicant2));
        applicantRepository.saveAndFlush(applicant2);

    }

    private void initContactForApplicant() {
        Contact contact = new Contact.Builder()
                .withWebsite("localhost:8080")
                .withPhoneNumber("0770457429")
                .withFacebookLink("https://www.facebook.com/chise.bogdan")
                .withLinkedinLink("https://www.linkedin.com/in/chise-bogdan-1ba95b142/")
                .build();
        Optional<User> user = userRepository.findByUsername("applicant");
        user.ifPresent(userAux -> userAux.getApplicant().setContact(contact));
        contactRepository.save(contact);

        Contact contact1 = new Contact.Builder()
                .withWebsite("localhost:8080")
                .withPhoneNumber("0770457429")
                .withFacebookLink("https://www.facebook.com/otniel.florin")
                .withLinkedinLink("https://www.linkedin.com/in/florin-otniel-boros-585221155/")
                .build();
        Optional<User> user1 = userRepository.findByUsername("applicant1");
        user1.ifPresent(userAux -> userAux.getApplicant().setContact(contact1));
        contactRepository.save(contact1);

        Contact contact2 = new Contact.Builder()
                .withWebsite("https://www.natalieportman.com/")
                .withPhoneNumber("424-288-2000")
                .withFacebookLink("https://www.facebook.com/natalieportmandotcom")
                .withLinkedinLink("https://www.linkedin.com/in/natalie-portman-836496134/")
                .build();
        Optional<User> user2 = userRepository.findByUsername("natalie");
        user2.ifPresent(userAux -> userAux.getApplicant().setContact(contact2));
        contactRepository.save(contact2);

    }

    private void initPhotoForApplicant() {
        Photo photo = new Photo();
        photo.setUrl("https://res.cloudinary.com/yuhuubackend/image/upload/v1547235510/pozaEu.jpg");
        Optional<User> user = userRepository.findByUsername("applicant");
        user.ifPresent(userAux -> userAux.getApplicant().getContact().setPhoto(photo));
        photoRepository.save(photo);

        Photo photo1 = new Photo();
        photo1.setUrl("https://res.cloudinary.com/yuhuubackend/image/upload/v1547827032/oti.jpg");
        Optional<User> user1 = userRepository.findByUsername("applicant1");
        user1.ifPresent(userAux -> userAux.getApplicant().getContact().setPhoto(photo1));
        photoRepository.save(photo1);

        Photo photo2 = new Photo();
        photo2.setUrl("https://res.cloudinary.com/yuhuubackend/image/upload/v1547826764/natalie.jpg");
        Optional<User> user2 = userRepository.findByUsername("natalie");
        user2.ifPresent(userAux -> userAux.getApplicant().getContact().setPhoto(photo2));
        photoRepository.save(photo2);
    }

    private void initFakeCVForCompanies() {

        String[] usernameArray = {"company","company1","tesla","yonder", "amazon", "optopt", "telenav"};

        for(String username: usernameArray){
            CV cv = new CV();
            Optional<User> user = userRepository.findByUsername(username);
            user.ifPresent(
                    presentUser -> presentUser.getCompany().getContact().setCv(cv));
            cvRepository.save(cv);
        }
    }

    private void initCVForApplicant() {
        CV cv = new CV();
        cv.setUrl("https://res.cloudinary.com/yuhuubackend/image/upload/v1547235283/CV_ChiseBogdan.pdf");
        Optional<User> user = userRepository.findByUsername("applicant");
        user.ifPresent(userAux -> userAux.getApplicant().getContact().setCv(cv));
        cvRepository.save(cv);

    }

    private void initAddressForApplicant() {
        Address address = new Address.Builder()
                .withCountry("Romania")
                .withCounty("Cluj")
                .withTown("Cluj-Napoca")
                .withStreet("Aleea Detunata")
                .withNumber("19")
                .build();
        Optional<User> user = userRepository.findByUsername("applicant");
        user.ifPresent(userAux -> userAux.getApplicant().getContact().setAddress(address));
        LatLng latLng = Geocoding.get(address.toString());
        address.setLongitude(latLng.lng);
        address.setLatitude(latLng.lat);
        addressRepository.save(address);

        Address address1 = new Address.Builder()
                .withCountry("Romania")
                .withCounty("Cluj")
                .withTown("Cluj-Napoca")
                .withStreet("Teodor Mihali")
                .withNumber("19")
                .build();

        Optional<User> user1 = userRepository.findByUsername("applicant1");
        user1.ifPresent(userAux -> userAux.getApplicant().getContact().setAddress(address1));
        latLng = Geocoding.get(address1.toString());
        address1.setLongitude(latLng.lng);
        address1.setLatitude(latLng.lat);
        addressRepository.save(address1);
    }

    private void addSkillsToApplicant() {

        String[] applicantUsername = {"applicant", "applicant1"};

        for(String username: applicantUsername){

            Optional<Skill> skillAux = skillRepository.findByNameEquals("Java");
            Optional<Skill> skillAux1 = skillRepository.findByNameEquals("Javascript");
            Optional<Skill> skillAux2 = skillRepository.findByNameEquals("Erlang");
            Optional<Skill> skillAux3 = skillRepository.findByNameEquals("Hadoop");
            Optional<Skill> skillAux4 = skillRepository.findByNameEquals("C#");

            Optional<User> user = userRepository.findByUsername(username);

            if (skillAux.isPresent() && user.isPresent()) {
                Applicant applicant = user.get().getApplicant();

                applicant.addSkill(skillAux.get());
                applicant.addSkill(skillAux1.get());
                applicant.addSkill(skillAux2.get());
                applicant.addSkill(skillAux3.get());
                applicant.addSkill(skillAux4.get());

                applicantRepository.save(applicant);
            }

        }
    }

    private InternshipRequest createInternshipRequest(Internship internship, Applicant applicant, InternshipRequestStatus internshipRequestStatus) {
        InternshipRequest internshipRequest = new InternshipRequest.Builder()
                .withInternship(internship)
                .withApplicant(applicant)
                .withStatus(internshipRequestStatus)
                .withLogoUrl(internship.getCompany().getContact().getPhoto().getUrl())
                .build();
        InternshipRequest createdInternshipRequest = internshipRequestRepository.save(internshipRequest);
        applicant.getInternshipRequests().add(createdInternshipRequest);

        internship.getInternshipRequests().add(createdInternshipRequest);
        internshipRepository.save(internship);
        return createdInternshipRequest;
    }

    private void addApplicantToInternship() {
        Optional<User> user = userRepository.findByUsername("applicant");
        Optional<User> user1 = userRepository.findByUsername("applicant1");

        List<Internship> internships = internshipRepository.findByTitleLike("Java Internship");
        List<Internship> internships1 = internshipRepository.findByTitleLike("C# Internship");
        List<Internship> internships2 = internshipRepository.findByTitleLike("Angular Internship");

        if (user.isPresent()) {
            Applicant applicant = user.get().getApplicant();
            Applicant applicant1 = user1.get().getApplicant();

            Internship internship = internships.get(0);
            Internship internship1 = internships1.get(0);
            Internship internship2 = internships2.get(0);

            applicant1.getInternship().add(internship);
            internship.getApplicants().add(applicant1);
            createInternshipRequest(internship, applicant1, InternshipRequestStatus.REJECTED);

            applicant.getInternship().add(internship);
            internship.getApplicants().add(applicant);
            createInternshipRequest(internship, applicant, InternshipRequestStatus.PENDING);

            applicant.getInternship().add(internship1);
            internship1.getApplicants().add(applicant);
            createInternshipRequest(internship1, applicant, InternshipRequestStatus.ACCEPTED);

            applicant.getInternship().add(internship2);
            internship2.getApplicants().add(applicant);
            createInternshipRequest(internship2, applicant, InternshipRequestStatus.REJECTED);

            applicantRepository.save(applicant);
        }
    }

    private void initDatabase() {
        initUsers();
        initCompanies();
        initContactForCompanies();
        initPhotoForCompanies();
        initFakeCVForCompanies();
        initAddressesForCompanies();
        initTags();
        initSkills();
        initAttribute();
        initRequirements();
        initInternship();
        initApplicants();
        addSkillsToApplicant();
        addApplicantToInternship();
        initExperience();
        initEducations();
        initContactForApplicant();
        initAddressForApplicant();
        initPhotoForApplicant();
        initCVForApplicant();
        initComments();
    }


    private void initComments() {
        Optional<User> optional = userRepository.findByUsername("applicant");
        if (optional.isPresent()) {
            User user = optional.get();
            Comment comment = new Comment();
            comment.setText("Suna foarte bine!");
            Comment resultComment = commentRepository.save(comment);
            user.addComment(resultComment);
            userRepository.save(user);

            Optional<Internship> optionalInternship = internshipRepository.findById(1L);
            if (optionalInternship.isPresent()) {
                Internship internship = optionalInternship.get();
                internship.addComment(resultComment);
                internshipRepository.save(internship);
                commentRepository.save(resultComment);

            }

            Comment subComment = new Comment();
            subComment.setText("Acceptati studenti din anul 1?");
            Comment subResultComment = commentRepository.save(subComment);
            user.addComment(subResultComment);
            userRepository.save(user);
            Long id = subResultComment.getId();

            Comment secondSubComment = new Comment();
            secondSubComment.setText("Dar din anul 2?");
            Comment anotherResult = commentRepository.save(secondSubComment);
            user.addComment(anotherResult);
            userRepository.save(user);
            Long anotherId = anotherResult.getId();


            Optional<Comment> optionalParent = commentRepository.findById(1L);
            if (optionalParent.isPresent()) ;
            {

                Comment parent = optionalParent.get();
                Comment son = commentRepository.findById(id).get();
                parent.getComments().add(son);
                son.setParent(parent);
                commentRepository.save(son);

                Comment daughter = commentRepository.findById(anotherId).get();
                parent.addSubComment(daughter);
                commentRepository.save(daughter);
            }

        }
        Comment likedComment = commentRepository.findById(1L).get();
        User applicant = userRepository.findByUsername("applicant").get();
        User company = userRepository.findByUsername("company").get();

        userLikeComment(likedComment, applicant);
        userLikeComment(likedComment, company);
        userLikeComment(likedComment, applicant);
        userLikeComment(likedComment, company);
        userLikeComment(likedComment, applicant);
        userDislikeComment(likedComment, applicant);
        userDislikeComment(likedComment, company);
        userDislikeComment(likedComment, applicant);
    }

    public Comment userLikeComment(Comment comment, User likeningUser) {
        comment.addOrRemoveLike(likeningUser.getId());
        commentRepository.save(comment);
        return comment;
    }

    public Comment userDislikeComment(Comment comment, User dislikingUser) {
        comment.addOrRemoveDislike(dislikingUser.getId());
        commentRepository.save(comment);
        return comment;
    }


}
