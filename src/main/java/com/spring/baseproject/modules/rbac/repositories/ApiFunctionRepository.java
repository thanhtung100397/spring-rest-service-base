package com.spring.baseproject.modules.rbac.repositories;

import com.spring.baseproject.modules.rbac.models.entities.ApiFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ApiFunctionRepository extends JpaRepository<ApiFunction, String> {
    @Modifying
    @Transactional
    @Query("delete from ApiFunction af " +
            "where not exists " +
            "(select a from Api a where a.apiFunction = af.name)")
    void deleteOrphanApiFunctions();
}
