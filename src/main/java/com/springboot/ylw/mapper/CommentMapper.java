package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectByGoodsId(Integer goodsId);
    int insert(Comment comment);
    int deleteById(Integer id);
}
