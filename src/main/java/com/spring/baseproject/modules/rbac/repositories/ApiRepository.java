package com.spring.baseproject.modules.rbac.repositories;

import com.spring.baseproject.modules.rbac.models.entities.Api;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ApiRepository extends JpaRepository<Api, Integer> {
    @Query("select a from Api a " +
            "left join fetch a.roles " +
            "left join fetch a.method " +
            "join fetch a.apiFunction af " +
            "join fetch af.apiModule")
    List<Api> getAllFetchedApis();

    @Query("select a from Api a " +
            "left join fetch a.method " +
            "join fetch a.apiFunction af " +
            "join fetch af.apiModule")
    List<Api> getAllApiWithFetchedFunctionAndModule(Sort sort);

    @Query("select a from Api a " +
            "join a.roles r " +
            "left join fetch a.method " +
            "join fetch a.apiFunction af " +
            "join fetch af.apiModule " +
            "where r.id = ?1")
    List<Api> getAllApiWithFetchedFunctionAndModuleOfRole(int roleID, Sort sort);

    @Query("select a from Api a " +
            "left join fetch a.roles " +
            "where a.id in ?1")
    Set<Api> getAllApiWithFetchedRoles(Set<Integer> ids);
}
