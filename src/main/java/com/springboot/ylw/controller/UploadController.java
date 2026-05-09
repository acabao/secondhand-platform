package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private OssService ossService;

    private static final List<String> ALLOWED_EXTS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp");
    private static final long MAX_SIZE = 10 * 1024 * 1024; // 10MB

    @PostMapping("/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file,
                                      @RequestParam(value = "type", required = false) String type) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            if (file.getSize() > MAX_SIZE) {
                return Result.error("文件大小不能超过 10MB");
            }

            String originalFilename = file.getOriginalFilename();
            String ext = ".jpg";
            if (originalFilename != null && originalFilename.contains(".")) {
                ext = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            }
            if (!ALLOWED_EXTS.contains(ext)) {
                return Result.error("只支持 JPG、PNG、GIF、WebP 格式");
            }

            String dir = "avatar".equalsIgnoreCase(type) ? "avatars/" : "goods/";
            String objectName = dir + UUID.randomUUID() + ext;
            String contentType = file.getContentType() != null ? file.getContentType() : "image/jpeg";
            String url = ossService.upload(file.getInputStream(), objectName, contentType);

            return Result.success(url);
        } catch (Exception e) {
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}
