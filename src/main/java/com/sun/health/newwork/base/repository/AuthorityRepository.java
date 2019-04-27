package com.sun.health.newwork.base.repository;

import com.sun.health.newwork.base.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    @Query("SELECT a FROM Authority AS a, UserAuthority AS ua WHERE ua.userId = ?1 AND a.id = ua.authorityId")
    List<Authority> queryAuthoritiesOfUser(Long userId);

    @Query(value = "SELECT a.name FROM Authority AS a, UserAuthority AS ua WHERE ua.userId = ?1 AND a.id=ua.authorityId")
    List<String> queryAuthorityNameListOfUser(@Param("userId") Long userId);

    Authority findByName(String name);


}
