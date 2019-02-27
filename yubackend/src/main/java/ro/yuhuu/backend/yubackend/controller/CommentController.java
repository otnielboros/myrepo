package ro.yuhuu.backend.yubackend.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.yuhuu.backend.yubackend.exceptions.NotAllowedApplicantException;
import ro.yuhuu.backend.yubackend.exceptions.NotValidCommentException;
import ro.yuhuu.backend.yubackend.exceptions.NotValidInternshipException;
import ro.yuhuu.backend.yubackend.exceptions.UpdateException;
import ro.yuhuu.backend.yubackend.model.Comment;
import ro.yuhuu.backend.yubackend.service.GenericService;

@SuppressWarnings("unchecked")
@Api("CommentController")
@RestController
@RequestMapping("/api/comment")
@CrossOrigin(origins = "*")
public class CommentController {
    @Autowired
    private GenericService userService;

    @PostMapping("/{internshipId}/add")
    public @ResponseBody
    ResponseEntity addComment(@PathVariable Long internshipId, @RequestBody Comment comment) throws NotValidInternshipException {
        return new ResponseEntity(userService.addComment(internshipId,comment), HttpStatus.OK);
    }

    @PutMapping("/{commentId}/like")
    public @ResponseBody ResponseEntity likeComment(@PathVariable Long commentId) throws NotValidCommentException {
       return new ResponseEntity(userService.likeComment(commentId), HttpStatus.OK);
    }

    @PutMapping("/{commentId}/dislike")
    public @ResponseBody ResponseEntity dislikeComment(@PathVariable Long commentId) throws NotValidCommentException {
        return new ResponseEntity(userService.dislikeComment(commentId), HttpStatus.OK);
    }

    @PostMapping("/reply/{parentCommentId}/add")
    public @ResponseBody
    ResponseEntity addReplyComment(@RequestBody Comment comment, @PathVariable Long parentCommentId) throws NotValidCommentException {
        return new ResponseEntity(userService.addReplyComment(comment,parentCommentId), HttpStatus.OK);
    }

    @GetMapping("/{internshipId}/all")
    public @ResponseBody
    ResponseEntity getAllCommentsForInternship(@PathVariable Long internshipId) throws NotValidInternshipException {
        return new ResponseEntity(userService.getAllCommentsForInternship(internshipId),HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public @ResponseBody
    ResponseEntity removeComment(@PathVariable Long commentId) throws NotValidCommentException, NotAllowedApplicantException {
        return new ResponseEntity(userService.removeComment(commentId),HttpStatus.OK);
    }



    @ExceptionHandler(NotValidCommentException.class)
    public @ResponseBody
    ResponseEntity handleNotValidCommentException(NotValidCommentException exception) {
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
