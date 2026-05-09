package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AddressMapper {
    List<Address> selectByUserId(Integer userId);
    Address selectById(Integer id);
    int insert(Address address);
    int update(Address address);
    int deleteById(Integer id);
    int clearDefault(Integer userId);
}
