package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.Banner;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BannerMapper {
    List<Banner> selectAll();
    int insert(Banner banner);
    int update(Banner banner);
    int deleteById(Integer id);
}
