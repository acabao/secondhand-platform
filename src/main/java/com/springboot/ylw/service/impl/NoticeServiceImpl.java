package com.springboot.ylw.service.impl;

import com.springboot.ylw.entity.Notice;
import com.springboot.ylw.mapper.NoticeMapper;
import com.springboot.ylw.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public List<Notice> getAll() {
        return noticeMapper.selectAll();
    }

    @Override
    public List<Notice> getAllForAdmin() {
        return noticeMapper.selectAllAdmin();
    }

    @Override
    public Notice getById(Integer id) {
        return noticeMapper.selectById(id);
    }

    @Override
    public String toggleStatus(Integer id, Integer status) {
        Notice n = new Notice();
        n.setId(id);
        n.setStatus(status);
        noticeMapper.update(n);
        return status == 1 ? "已发布" : "已隐藏";
    }

    @Override
    public String add(Notice notice) {
        noticeMapper.insert(notice);
        return "发布成功";
    }

    @Override
    public String update(Notice notice) {
        noticeMapper.update(notice);
        return "更新成功";
    }

    @Override
    public String delete(Integer id) {
        noticeMapper.deleteById(id);
        return "删除成功";
    }
}
