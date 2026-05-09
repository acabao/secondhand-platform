package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.CommunityPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CommunityPostMapper {
    List<CommunityPost> search(@Param("type") Integer type,
                               @Param("keyword") String keyword,
                               @Param("status") Integer status);

    CommunityPost selectById(@Param("id") Integer id);

    List<CommunityPost> selectByUserId(@Param("userId") Integer userId);

    int insert(CommunityPost post);

    int update(CommunityPost post);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    int incrViewCount(@Param("id") Integer id);

    int incrReplyCount(@Param("id") Integer id, @Param("delta") Integer delta);

    int deleteById(@Param("id") Integer id);
}
