package com.springboot.ylw.service;

import com.springboot.ylw.entity.Notice;
import java.util.List;

public interface NoticeService {
    List<Notice> getAll();
    List<Notice> getAllForAdmin();
    Notice getById(Integer id);
    String add(Notice notice);
    String update(Notice notice);
    String toggleStatus(Integer id, Integer status);
    String delete(Integer id);
}
