package com.springboot.ylw.service;

import com.springboot.ylw.entity.PickupPoint;
import java.util.List;

public interface PickupPointService {
    List<PickupPoint> listActive();
    List<PickupPoint> listAll();
    PickupPoint getById(Integer id);
    String add(PickupPoint point);
    String update(PickupPoint point);
    String delete(Integer id);
}
