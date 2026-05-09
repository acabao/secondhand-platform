package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AdminMapper {
    Admin selectById(Integer id);
    Admin selectByUsername(String username);
    List<Admin> selectAll();
    int insert(Admin admin);
    int update(Admin admin);
    int deleteById(Integer id);
}
