package com.sun.health.newwork.record.service;

import com.sun.health.newwork.record.dto.QueryCatalogDTO;
import com.sun.health.newwork.record.entity.Catalog;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CatalogService {

    Catalog saveCatalog(Catalog catalog);

    Catalog updateCatalog(Catalog catalog);

    void deleteCatalog(Catalog catalog);

    Page<Catalog> querySelfCatalogPage(QueryCatalogDTO queryCatalogDTO);

    Page<Catalog> queryCatalogPage(QueryCatalogDTO queryCatalogDTO);

    List<Catalog> queryCatalogList(QueryCatalogDTO queryCatalogDTO);

    List<Catalog> queryCatalogListEntityManager(QueryCatalogDTO queryCatalogDTO);

    List<Catalog> queryCatalogListNativeQuery(QueryCatalogDTO queryCatalogDTO);
}
