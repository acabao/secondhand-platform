package com.springboot.ylw.service.impl;

import com.springboot.ylw.entity.Banner;
import com.springboot.ylw.mapper.BannerMapper;
import com.springboot.ylw.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public List<Banner> getAll() {
        return bannerMapper.selectAll();
    }

    @Override
    public String add(Banner banner) {
        bannerMapper.insert(banner);
        return "添加成功";
    }

    @Override
    public String update(Banner banner) {
        bannerMapper.update(banner);
        return "更新成功";
    }

    @Override
    public String delete(Integer id) {
        bannerMapper.deleteById(id);
        return "删除成功";
    }
}
