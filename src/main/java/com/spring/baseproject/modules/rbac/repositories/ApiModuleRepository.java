package com.spring.baseproject.modules.rbac.repositories;

import com.spring.baseproject.modules.rbac.models.entities.ApiModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ApiModuleRepository extends JpaRepository<ApiModule, String> {
    ApiModule findFirstByName(String name);

    @Modifying
    @Transactional
    @Query("delete from ApiModule am " +
            "where not exists " +
            "(select af from ApiFunction af where af.apiModule = am.name)")
    void deleteOrphanApiModules();
}
