package com.springboot.ylw.service.impl;

import com.springboot.ylw.entity.PickupPoint;
import com.springboot.ylw.mapper.PickupPointMapper;
import com.springboot.ylw.service.PickupPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PickupPointServiceImpl implements PickupPointService {

    @Autowired
    private PickupPointMapper mapper;

    @Override
    public List<PickupPoint> listActive() { return mapper.selectActive(); }

    @Override
    public List<PickupPoint> listAll() { return mapper.selectAll(); }

    @Override
    public PickupPoint getById(Integer id) { return mapper.selectById(id); }

    @Override
    public String add(PickupPoint point) {
        if (point.getName() == null || point.getName().trim().isEmpty()) {
            throw new RuntimeException("名称不能为空");
        }
        if (point.getAddress() == null || point.getAddress().trim().isEmpty()) {
            throw new RuntimeException("详细地址不能为空");
        }
        mapper.insert(point);
        return "添加成功";
    }

    @Override
    public String update(PickupPoint point) {
        mapper.update(point);
        return "更新成功";
    }

    @Override
    public String delete(Integer id) {
        mapper.deleteById(id);
        return "删除成功";
    }
}
