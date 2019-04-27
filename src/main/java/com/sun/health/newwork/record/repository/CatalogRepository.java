package com.sun.health.newwork.record.repository;

import com.sun.health.newwork.record.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CatalogRepository extends JpaRepository<Catalog, Long>, JpaSpecificationExecutor<Catalog> {

}
