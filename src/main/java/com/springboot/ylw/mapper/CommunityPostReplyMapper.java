package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.CommunityPostReply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CommunityPostReplyMapper {
    List<CommunityPostReply> selectByPostId(@Param("postId") Integer postId);

    CommunityPostReply selectById(@Param("id") Integer id);

    int insert(CommunityPostReply reply);

    int deleteById(@Param("id") Integer id);

    int deleteByPostId(@Param("postId") Integer postId);
}
