package com.springboot.ylw.mapper;

import com.springboot.ylw.entity.PickupPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PickupPointMapper {
    List<PickupPoint> selectActive();
    List<PickupPoint> selectAll();
    PickupPoint selectById(@Param("id") Integer id);
    int insert(PickupPoint point);
    int update(PickupPoint point);
    int deleteById(@Param("id") Integer id);
}
