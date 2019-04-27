package com.sun.health.newwork.record.service;

import com.sun.health.newwork.record.dto.QueryTagDTO;
import com.sun.health.newwork.record.entity.Tag;
import org.springframework.data.domain.Page;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

public interface TagService {

    Tag save(Tag tag) throws UserPrincipalNotFoundException;

    void delete(Tag tag);

    List<Tag> queryTagByRecordId(Long recordId);

    Tag findByName(String name);

    List<Tag> queryTagList(QueryTagDTO queryTagDTO);

    Page<Tag> queryTagPage(QueryTagDTO queryTagDTO);

}
