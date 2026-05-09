package com.springboot.ylw.service;

import com.springboot.ylw.entity.Comment;
import java.util.List;

public interface CommentService {
    List<Comment> getByGoodsId(Integer goodsId);
    String addComment(Comment comment);
    String deleteComment(Integer id);
}
