package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface NoticeMapper {
    List<Notice> selectAll();
    List<Notice> selectAllAdmin();
    Notice selectById(Integer id);
    int insert(Notice notice);
    int update(Notice notice);
    int deleteById(Integer id);
}
