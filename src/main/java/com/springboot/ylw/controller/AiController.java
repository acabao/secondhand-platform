package com.springboot.ylw.controller;

import com.springboot.ylw.common.Result;
import com.springboot.ylw.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired private AiService aiService;

    /** 根据标题生成商品详情描述 */
    @PostMapping("/describe")
    public Result<?> describe(@RequestBody Map<String, String> body) {
        String title = body.get("title");
        String description = aiService.generateDescription(title);
        Map<String, String> data = new HashMap<>();
        data.put("description", description);
        return Result.success(data);
    }

    /** 根据图片识别商品标题 */
    @PostMapping("/recognize")
    public Result<?> recognize(@RequestBody Map<String, String> body) {
        String imageUrl = body.get("imageUrl");
        String title = aiService.recognizeTitle(imageUrl);
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        return Result.success(data);
    }

    /** AI 客服对话 */
    @PostMapping("/chat")
    @SuppressWarnings("unchecked")
    public Result<?> chat(@RequestBody Map<String, Object> body) {
        Object uidObj = body.get("userId");
        Integer userId = null;
        if (uidObj instanceof Number) userId = ((Number) uidObj).intValue();
        else if (uidObj instanceof String && !((String) uidObj).isEmpty()) {
            try { userId = Integer.parseInt((String) uidObj); } catch (Exception ignore) {}
        }

        String message = body.get("message") == null ? "" : String.valueOf(body.get("message"));
        Object histObj = body.get("history");
        List<Map<String, String>> history = histObj instanceof List
                ? (List<Map<String, String>>) histObj
                : Collections.emptyList();

        String reply = aiService.chat(userId, history, message);
        Map<String, String> data = new HashMap<>();
        data.put("reply", reply);
        return Result.success(data);
    }
}
