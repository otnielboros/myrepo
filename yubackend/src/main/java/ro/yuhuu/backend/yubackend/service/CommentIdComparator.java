package ro.yuhuu.backend.yubackend.service;

import ro.yuhuu.backend.yubackend.model.Comment;

import java.util.Comparator;

public class CommentIdComparator implements Comparator<Comment> {
    @Override
    public int compare(Comment comment1, Comment comment2) {
        {
            return comment1.getId().compareTo(comment2.getId());  // return -I1compareTo(I2);
        }
    }
}
