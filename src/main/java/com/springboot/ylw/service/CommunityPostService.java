package com.springboot.ylw.service;

import com.springboot.ylw.entity.CommunityPost;
import com.springboot.ylw.entity.CommunityPostReply;
import java.util.List;

public interface CommunityPostService {
    List<CommunityPost> search(Integer type, String keyword, Integer status);
    CommunityPost getById(Integer id);
    List<CommunityPost> getMyPosts(Integer userId);
    CommunityPost publish(CommunityPost post);
    String update(CommunityPost post);
    String updateStatus(Integer id, Integer status);
    String delete(Integer id);

    List<CommunityPostReply> listReplies(Integer postId);
    CommunityPostReply addReply(CommunityPostReply reply);
    String deleteReply(Integer id);
}
