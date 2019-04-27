package com.sun.health.newwork.record.controller;

import com.sun.health.newwork.base.dto.ResponseDTO;
import com.sun.health.newwork.record.dto.QueryRecordDTO;
import com.sun.health.newwork.record.dto.RecordDTO;
import com.sun.health.newwork.record.entity.Record;
import com.sun.health.newwork.record.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@Controller
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @RequestMapping("/save")
    @ResponseBody
    public ResponseDTO<Record> save(@RequestBody Record record) {
        ResponseDTO<Record> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "保存成功", null, recordService.saveRecord(record));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "保存失败", e.getMessage(), null);
        }
        return responseDTO;
    }

    @RequestMapping("/query/list")
    @ResponseBody
    public ResponseDTO<List<Record>> queryList(QueryRecordDTO queryRecordDTO) {
        ResponseDTO<List<Record>> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "查询成功", null, recordService.queryRecordList(queryRecordDTO));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "查询失败", e.getMessage(), null);
        }
        return responseDTO;
    }

    @RequestMapping("/query/page")
    @ResponseBody
    public ResponseDTO<Page<Record>> queryPage(QueryRecordDTO queryRecordDTO, @PageableDefault() Pageable pageable) {
        queryRecordDTO.setPageable(pageable);
        ResponseDTO<Page<Record>> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "查询成功", null, recordService.queryRecordPage(queryRecordDTO));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "查询失败", e.getMessage(), null);
        }
        return responseDTO;
    }

    @RequestMapping("/query/detail/{recordId}")
    @ResponseBody
    public ResponseDTO<Record> queryRecordDetail(@PathVariable("recordId") Long recordId) {
        ResponseDTO<Record> responseDTO;
        try {
            responseDTO = new ResponseDTO<>(0, "查询成功", null, recordService.queryRecordDetail(recordId));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "查询失败", e.getMessage(), null);
        }
        return responseDTO;
    }

}
