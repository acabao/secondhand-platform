package com.springboot.ylw.service.impl;

import com.springboot.ylw.entity.Comment;
import com.springboot.ylw.mapper.CommentMapper;
import com.springboot.ylw.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getByGoodsId(Integer goodsId) {
        return commentMapper.selectByGoodsId(goodsId);
    }

    @Override
    public String addComment(Comment comment) {
        commentMapper.insert(comment);
        return "评论成功";
    }

    @Override
    public String deleteComment(Integer id) {
        commentMapper.deleteById(id);
        return "删除成功";
    }
}
