package com.sun.health.newwork.record.controller;

import com.sun.health.newwork.base.dto.ResponseDTO;
import com.sun.health.newwork.base.util.UserUtils;
import com.sun.health.newwork.record.dto.CatalogDTO;
import com.sun.health.newwork.record.dto.QueryCatalogDTO;
import com.sun.health.newwork.record.entity.Catalog;
import com.sun.health.newwork.record.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @RequestMapping("/save")
    @ResponseBody
    public ResponseDTO<Catalog> save(@RequestBody Catalog catalog) {
        ResponseDTO<Catalog> responseDTO;
        try {
            Catalog savedCatalog = catalogService.saveCatalog(catalog);
            responseDTO = new ResponseDTO<>(0, "保存成功", null, savedCatalog);
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "保存失败", e.getMessage(), null);
        }
        return responseDTO;
    }

    @RequestMapping("/query/page")
    @ResponseBody
    public ResponseDTO<Page<Catalog>> queryCatalogPage(@RequestBody QueryCatalogDTO queryCatalogDTO) {
        ResponseDTO<Page<Catalog>> responseDTO;
        try {
//            responseDTO = new ResponseDTO<>(0, "查询成功", null, catalogService.queryCatalogPage(queryCatalogDTO));
            responseDTO = new ResponseDTO<>(0, "查询成功", null, catalogService.queryCatalogPage(queryCatalogDTO));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "查询失败", e.getMessage(), null);
        }
        return responseDTO;
    }

    @RequestMapping("/query/list")
    @ResponseBody
    public ResponseDTO<List<Catalog>> queryCatalogList(@RequestBody QueryCatalogDTO queryCatalogDTO) {
        ResponseDTO<List<Catalog>> responseDTO;
        try {
            queryCatalogDTO.setUserId(UserUtils.getSignInUserId());
//            responseDTO = new ResponseDTO<>(0, "查询成功", null, catalogService.queryCatalogList(queryCatalogDTO));
            responseDTO = new ResponseDTO<>(0, "查询成功", null, catalogService.queryCatalogListEntityManager(queryCatalogDTO));
//            responseDTO = new ResponseDTO<>(0, "查询成功", null, catalogService.queryCatalogListNativeQuery(queryCatalogDTO));
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO = new ResponseDTO<>(1, "查询失败", e.getMessage(), null);
        }
        return responseDTO;
    }

}
