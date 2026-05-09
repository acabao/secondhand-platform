package com.springboot.ylw.service;

import com.springboot.ylw.entity.Banner;
import java.util.List;

public interface BannerService {
    List<Banner> getAll();
    String add(Banner banner);
    String update(Banner banner);
    String delete(Integer id);
}
