package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {
    User selectById(Integer id);
    User selectByUsername(String username);
    User selectByPhone(String phone);
    List<User> selectAll();
    int insert(User user);
    int update(User user);
    int updateLastActive(Integer id);
    int deleteById(Integer id);
}
