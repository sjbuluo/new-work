package com.sun.health.newwork.record.controller;

import com.sun.health.newwork.base.dto.ResponseDTO;
import com.sun.health.newwork.record.dto.QueryTagDTO;
import com.sun.health.newwork.record.entity.Tag;
import com.sun.health.newwork.record.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @RequestMapping("/save")
    @ResponseBody
    public ResponseDTO<Tag> save(@RequestBody Tag tag) {
        ResponseDTO<Tag> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "保存成功", null, tagService.save(tag));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "保存失败", e.getMessage(), null);
        }
        return responseDTO;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResponseDTO<Void> delete(@RequestBody Tag tag) {
        ResponseDTO<Void> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "删除成功", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "删除失败", e.getMessage(), null);
        }
        return responseDTO;
    }

    @RequestMapping("/query/record/tag/{recordId}")
    @ResponseBody
    public ResponseDTO<List<Tag>> queryRecordTags(@PathVariable("recordId") Long recordId) {
        ResponseDTO<List<Tag>> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "查询成功", null, tagService.queryTagByRecordId(recordId));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "查询失败", e.getMessage(), null);
        }
        return responseDTO;
    }

    @RequestMapping("/exist/name")
    @ResponseBody
    public ResponseDTO<Tag> existName(@RequestBody String name) {
        ResponseDTO<Tag> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "查询成功", null, tagService.findByName(name));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "查询失败", e.getMessage(), null);
        }
        return responseDTO;
    }

    @RequestMapping("/query/tag/list")
    @ResponseBody
    public ResponseDTO<List<Tag>> queryTagList(QueryTagDTO queryTagDTO) {
        ResponseDTO<List<Tag>> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "查询成功", null, tagService.queryTagList(queryTagDTO));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "查询失败", e.getMessage(), null);
        }
        return responseDTO;
    }

    @RequestMapping("/query/tag/page")
    @ResponseBody
    public ResponseDTO<Page<Tag>> queryTagPage(QueryTagDTO queryTagDTO, @PageableDefault() Pageable pageable) {
        queryTagDTO.setPageable(pageable);
        ResponseDTO<Page<Tag>> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "查询成功", null, tagService.queryTagPage(queryTagDTO));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "查询失败", e.getMessage(), null);
        }
        return responseDTO;
    }

}
