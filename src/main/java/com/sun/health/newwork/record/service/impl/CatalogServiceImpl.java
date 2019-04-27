package com.sun.health.newwork.record.service.impl;

import com.sun.health.newwork.base.entity.User;
import com.sun.health.newwork.record.dto.CatalogDTO;
import com.sun.health.newwork.record.dto.QueryCatalogDTO;
import com.sun.health.newwork.record.entity.Catalog;
import com.sun.health.newwork.record.repository.CatalogRepository;
import com.sun.health.newwork.record.service.CatalogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Catalog saveCatalog(Catalog catalog) {
        catalog.setCreateTime(new Date());
        return catalogRepository.save(catalog);
    }

    @Override
    public Catalog updateCatalog(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    @Override
    public void deleteCatalog(Catalog catalog) {
        catalogRepository.delete(catalog);
    }

    @Override
    public Page<Catalog> querySelfCatalogPage(QueryCatalogDTO queryCatalogDTO) {
        return queryCatalogPage(queryCatalogDTO);
    }

    @Override
    public Page<Catalog> queryCatalogPage(QueryCatalogDTO queryCatalogDTO) {
        return catalogRepository.findAll(createQueryCatalogSpecification(queryCatalogDTO), queryCatalogDTO.getPageable());
    }

    @Override
    public List<Catalog> queryCatalogList(QueryCatalogDTO queryCatalogDTO) {
        return catalogRepository.findAll(createQueryCatalogSpecification(queryCatalogDTO));
    }

    @Override
    public List<Catalog> queryCatalogListEntityManager(QueryCatalogDTO queryCatalogDTO) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
//        hql.append("SELECT new Catalog(c, c.createUser.id, c.createUser.username) FROM Catalog AS c JOIN c.createUser WHERE 1=1");
        hql.append("SELECT new Catalog(c.id, c.name, c.description, c.createTime, c.pv, c.parentCatalog.id, c.parentCatalog.name, c.createUser.id, c.createUser.username) FROM Catalog AS c LEFT JOIN c.createUser LEFT JOIN c.parentCatalog WHERE 1=1");
        if(queryCatalogDTO.getUserId() != null) {
            hql.append(" AND c.createUser.id = :userId");
            params.put("userId", queryCatalogDTO.getUserId());
        }
        if(StringUtils.isNotBlank(queryCatalogDTO.getName())) {
            hql.append(" AND c.name LIKE %:name%");
            params.put("name", queryCatalogDTO.getName());
        }
        if(queryCatalogDTO.getParentId() != null) {
            hql.append(" AND c.parentCatalog.id = :parentId");
            params.put("parentId", queryCatalogDTO.getParentId());
        } else {
            hql.append(" AND c.parentCatalog.id IS NULL");
        }
        if(queryCatalogDTO.isPv() != null) {
            hql.append(" AND c.pv = :pv");
            params.put("pv", queryCatalogDTO.isPv());
        }
        if(queryCatalogDTO.getCreateTimeBefore() != null) {
            hql.append(" AND c.createTime >= :createTimeBefore");
            params.put("createTimeBefore", queryCatalogDTO.getCreateTimeBefore());
        }
        if(queryCatalogDTO.getCreateTimeAfter() != null) {
            hql.append(" AND c.createTime <= :createTimeAfter");
            params.put("createTimeAfter", queryCatalogDTO.getCreateTimeAfter());
        }
        TypedQuery<Catalog> query = entityManager.createQuery(hql.toString(), Catalog.class);
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            query.setParameter(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        return query.getResultList();
    }

    public List<Catalog> queryCatalogListNativeQuery(QueryCatalogDTO queryCatalogDTO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT c.id AS id, c.private AS pv, c.name AS name, c.description AS description, c.parent_id AS parentId, c.create_time AS createTime, u.username AS username, u.id AS userId FROM catalog AS c, user AS u WHERE c.user_id = u.id");
        if(queryCatalogDTO.getUserId() != null) {
            sql.append(" AND c.user_id = :userId");
            params.put("userId", queryCatalogDTO.getUserId());
        }
        if(StringUtils.isNotBlank(queryCatalogDTO.getName())) {
            sql.append(" AND c.name LIKE :name");
            params.put("name", "%" + queryCatalogDTO.getName() + "%");
        }
        if(queryCatalogDTO.getParentId() != null) {
            sql.append(" AND c.parent_id = :parentId");
            params.put("parentId", queryCatalogDTO.getParentId());
        } else {
            sql.append(" AND c.parent_id IS NULL");
        }
        if(queryCatalogDTO.isPv() != null) {
            sql.append(" AND c.private = :pv");
            params.put("pv", queryCatalogDTO.isPv());
        }
        if(queryCatalogDTO.getCreateTimeBefore() != null) {
            sql.append(" AND c.create_time >= :createTimeBefore");
            params.put("createTimeBefore", queryCatalogDTO.getCreateTimeBefore());
        }
        if(queryCatalogDTO.getCreateTimeAfter() != null) {
            sql.append(" AND c.create_time <= :createTimeAfter");
            params.put("createTimeAfter", queryCatalogDTO.getCreateTimeAfter());
        }
        Query nativeQuery = entityManager.createNativeQuery(sql.toString(), CatalogDTO.class);
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            nativeQuery.setParameter(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        return nativeQuery.getResultList();
    }

    private Specification<Catalog> createQueryCatalogSpecification(QueryCatalogDTO queryCatalogDTO) {
        return new Specification<Catalog>() {
            @Override
            public Predicate toPredicate(Root<Catalog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(queryCatalogDTO.getUserId() != null) {
                    Predicate userIdPredicate = criteriaBuilder.equal(root.get("createUser").get("id").as(Long.class), queryCatalogDTO.getUserId());
                    predicates.add(userIdPredicate);
                }
                if(StringUtils.isNotBlank(queryCatalogDTO.getName())) {
                    Predicate namePredicate = criteriaBuilder.like(root.get("name").as(String.class), "%" + queryCatalogDTO.getName() + "%");
                    predicates.add(namePredicate);
                }
                if(queryCatalogDTO.getParentId() == null) {
                    Predicate parentIdPredicate = criteriaBuilder.isNull(root.get("parentCatalog").get("id").as(Long.class));
                    predicates.add(parentIdPredicate);
                } else {
                    Predicate parentIdPredicate = criteriaBuilder.equal(root.get("parentCatalog").get("id").as(Long.class), queryCatalogDTO.getParentId());
                    predicates.add(parentIdPredicate);
                }
                if(queryCatalogDTO.isPv() != null) {
                    Predicate pvPredicate = criteriaBuilder.equal(root.get("pv").as(Boolean.class), queryCatalogDTO.isPv());
                    predicates.add(pvPredicate);
                }
                if(queryCatalogDTO.getCreateTimeBefore() != null) {
                    Predicate createTimeBeforePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(Date.class), queryCatalogDTO.getCreateTimeBefore());
                    predicates.add(createTimeBeforePredicate);
                }
                if(queryCatalogDTO.getCreateTimeAfter() != null) {
                    Predicate createTimeAfterPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(Date.class), queryCatalogDTO.getCreateTimeAfter());
                    predicates.add(createTimeAfterPredicate);
                }
                Join<Catalog, User> createUser = root.join(root.getModel().getSingularAttribute("createUser", User.class), JoinType.INNER);
                Predicate createUserPredicate = criteriaBuilder.equal(createUser.get("id").as(Long.class), root.get("createUser").get("id").as(Long.class));
                predicates.add(createUserPredicate);
                Predicate[] predicateArr = new Predicate[predicates.size()];
                CriteriaQuery<Catalog> query = criteriaBuilder.createQuery(Catalog.class);
                criteriaQuery.where(criteriaBuilder.and(predicates.toArray(predicateArr)));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createTime").as(Date.class)));
                return criteriaQuery.getRestriction();
            }
        };
    }
}
