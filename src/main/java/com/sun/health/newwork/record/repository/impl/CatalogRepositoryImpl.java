package com.sun.health.newwork.record.repository.impl;

import com.sun.health.newwork.record.dto.QueryCatalogDTO;
import com.sun.health.newwork.record.entity.Catalog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CatalogRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;


}
