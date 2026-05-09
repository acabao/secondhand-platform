package com.springboot.ylw.service;

import com.springboot.ylw.entity.Address;
import java.util.List;

public interface AddressService {
    List<Address> getByUserId(Integer userId);
    String add(Address address);
    String update(Address address);
    String delete(Integer id);
    String setDefault(Integer id, Integer userId);
}
