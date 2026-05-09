package com.springboot.ylw.service.impl;

import com.springboot.ylw.entity.CommunityPost;
import com.springboot.ylw.entity.CommunityPostReply;
import com.springboot.ylw.mapper.CommunityPostMapper;
import com.springboot.ylw.mapper.CommunityPostReplyMapper;
import com.springboot.ylw.service.CommunityPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommunityPostServiceImpl implements CommunityPostService {

    @Autowired
    private CommunityPostMapper postMapper;

    @Autowired
    private CommunityPostReplyMapper replyMapper;

    @Override
    public List<CommunityPost> search(Integer type, String keyword, Integer status) {
        return postMapper.search(type, keyword, status);
    }

    @Override
    public CommunityPost getById(Integer id) {
        CommunityPost post = postMapper.selectById(id);
        if (post == null) throw new RuntimeException("帖子不存在");
        postMapper.incrViewCount(id);
        return post;
    }

    @Override
    public List<CommunityPost> getMyPosts(Integer userId) {
        return postMapper.selectByUserId(userId);
    }

    @Override
    public CommunityPost publish(CommunityPost post) {
        if (post.getType() == null || post.getType() < 1 || post.getType() > 3) {
            throw new RuntimeException("帖子类型不合法");
        }
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            throw new RuntimeException("标题不能为空");
        }
        post.setStatus(1);
        postMapper.insert(post);
        return post;
    }

    @Override
    public String update(CommunityPost post) {
        postMapper.update(post);
        return "更新成功";
    }

    @Override
    public String updateStatus(Integer id, Integer status) {
        postMapper.updateStatus(id, status);
        return "状态已更新";
    }

    @Override
    @Transactional
    public String delete(Integer id) {
        replyMapper.deleteByPostId(id);
        postMapper.deleteById(id);
        return "删除成功";
    }

    @Override
    public List<CommunityPostReply> listReplies(Integer postId) {
        return replyMapper.selectByPostId(postId);
    }

    @Override
    @Transactional
    public CommunityPostReply addReply(CommunityPostReply reply) {
        if (reply.getContent() == null || reply.getContent().trim().isEmpty()) {
            throw new RuntimeException("回复内容不能为空");
        }
        replyMapper.insert(reply);
        postMapper.incrReplyCount(reply.getPostId(), 1);
        return replyMapper.selectById(reply.getId());
    }

    @Override
    @Transactional
    public String deleteReply(Integer id) {
        CommunityPostReply reply = replyMapper.selectById(id);
        if (reply == null) return "回复不存在";
        replyMapper.deleteById(id);
        postMapper.incrReplyCount(reply.getPostId(), -1);
        return "删除成功";
    }
}
