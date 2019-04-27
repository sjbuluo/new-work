package com.sun.health.newwork.record.controller;

import com.sun.health.newwork.base.dto.ResponseDTO;
import com.sun.health.newwork.record.entity.Reply;
import com.sun.health.newwork.record.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @RequestMapping("/save")
    @ResponseBody
    public ResponseDTO<Reply> save(@RequestBody Reply reply) {
        ResponseDTO<Reply> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "保存成功", null, replyService.save(reply));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "保存失败", e.getMessage(), null);
        }
        return responseDTO;
    }

    @RequestMapping("/query/page")
    @ResponseBody
    public ResponseDTO<Page<Reply>> queryPage(Long recordId, @PageableDefault() Pageable pageable) {
        ResponseDTO<Page<Reply>> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "保存成功", null, replyService.queryRecordReplyPage(recordId, pageable));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "保存失败", e.getMessage(), null);
        }
        return responseDTO;
    }

}
