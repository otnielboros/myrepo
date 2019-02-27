package ro.yuhuu.backend.yubackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ro.yuhuu.backend.yubackend.service.CommentIdComparator;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @Lob
    private String text;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User creator;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parent_id" )
    private Comment parent;

    private Integer likes = 0;
    private Integer dislikes = 0;


    private ArrayList<Long> usersIdLike = new ArrayList<>();
    public void addOrRemoveLike(Long userId){
        for (Long id:usersIdLike) {
            if(id.equals(userId)){
                removeLike(id);
                return;
            }
        }
        usersIdLike.add(userId);
        likes = usersIdLike.size();
    }
    public void removeLike(Long userId){
        usersIdLike.remove(userId);
        likes = usersIdLike.size();
    }

    private ArrayList<Long> usersIdDislikes = new ArrayList<>();
    public void addOrRemoveDislike(Long userId){
        for (Long id:usersIdDislikes) {
            if(id.equals(userId)){
                removeDislike(id);
                return;
            }
        }
        usersIdDislikes.add(userId);
        dislikes = usersIdDislikes.size();
    }
    public void removeDislike(Long userId){
        usersIdDislikes.remove(userId);
        dislikes = usersIdDislikes.size();
    }


    @OneToMany(mappedBy = "parent", cascade = CascadeType.DETACH, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Comment> comments = new TreeSet<>(new CommentIdComparator());

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "internship_id")
    @JsonIgnore
    private Internship internship;

    public Internship getInternship() {
        return internship;
    }

    public void setInternship(Internship internship) {
        this.internship = internship;
    }

    public void addSubComment(Comment comment){
        this.comments.add(comment);
        comment.setParent(this);
    }

    public void removeSubComment(Comment comment){
        this.comments.remove(comment);
        comment.setParent(null);
        comment.creator.removeComment(comment);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        if(creator != null) {
            if (creator.getApplicant() != null)
                this.name =
                        creator.getApplicant().getFirstName()
                                + " " +
                                creator.getApplicant().getLastName();
            else if (creator.getCompany() != null)
                this.name = creator.getCompany().getName();
        }else
            this.name = null;

        this.creator = creator;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Integer getLikes(){
        return usersIdLike.size();
    }

    public Integer getDislikes(){
        return usersIdDislikes.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
