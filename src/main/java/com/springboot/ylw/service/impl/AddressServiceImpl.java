package com.springboot.ylw.service.impl;

import com.springboot.ylw.entity.Address;
import com.springboot.ylw.mapper.AddressMapper;
import com.springboot.ylw.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Address> getByUserId(Integer userId) {
        return addressMapper.selectByUserId(userId);
    }

    @Override
    public String add(Address address) {
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            addressMapper.clearDefault(address.getUserId());
        }
        addressMapper.insert(address);
        return "添加成功";
    }

    @Override
    public String update(Address address) {
        addressMapper.update(address);
        return "更新成功";
    }

    @Override
    public String delete(Integer id) {
        addressMapper.deleteById(id);
        return "删除成功";
    }

    @Override
    @Transactional
    public String setDefault(Integer id, Integer userId) {
        addressMapper.clearDefault(userId);
        Address a = new Address();
        a.setId(id);
        a.setIsDefault(1);
        addressMapper.update(a);
        return "已设为默认地址";
    }
}
