package com.sun.health.newwork.record.service.impl;

import com.sun.health.newwork.base.util.UserUtils;
import com.sun.health.newwork.record.dto.QueryTagDTO;
import com.sun.health.newwork.record.entity.Tag;
import com.sun.health.newwork.record.repository.TagRepository;
import com.sun.health.newwork.record.service.TagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag save(Tag tag) throws UserPrincipalNotFoundException {
        if(tag.getCreateTime() == null) {
            tag.setCreateTime(new Date());
        }
        if(tag.getCreateUser() == null) {
            tag.setCreateUser(UserUtils.getSignInUser());
        }
        return tagRepository.save(tag);
    }

    @Override
    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }

    @Override
    public List<Tag> queryTagByRecordId(Long recordId) {
        return tagRepository.findByRecordId(recordId);
    }

    @Override
    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public List<Tag> queryTagList(QueryTagDTO queryTagDTO) {
        return tagRepository.findAll(makeQueryTagSpecification(queryTagDTO));
    }

    @Override
    public Page<Tag> queryTagPage(QueryTagDTO queryTagDTO) {
        return tagRepository.findAll(makeQueryTagSpecification(queryTagDTO), queryTagDTO.getPageable());
    }

    private Specification<Tag> makeQueryTagSpecification(final QueryTagDTO queryTagDTO) {
        return new Specification<Tag>() {
            @Override
            public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicateList = new ArrayList<>();
                if(queryTagDTO.getId() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("id").as(Long.class), queryTagDTO.getId()));
                }
                if(StringUtils.isNotBlank(queryTagDTO.getName())) {
                    predicateList.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + queryTagDTO.getName() + "%"));
                }
                if(StringUtils.isNotBlank(queryTagDTO.getDescription())) {
                    predicateList.add(criteriaBuilder.like(root.get("description").as(String.class), "%" + queryTagDTO.getDescription() + "%"));
                }
                if(queryTagDTO.getCreateTimeBefore() != null) {
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(Date.class), queryTagDTO.getCreateTimeBefore()));
                }
                if(queryTagDTO.getCreateTimeAfter() != null) {
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(Date.class), queryTagDTO.getCreateTimeAfter()));
                }
                if(queryTagDTO.getUserId() != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("createUser").get("id").as(Long.class), queryTagDTO.getUserId()));
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(predicates)));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createTime").as(Date.class)));
                return criteriaQuery.getRestriction();
            }
        };
    }
}
