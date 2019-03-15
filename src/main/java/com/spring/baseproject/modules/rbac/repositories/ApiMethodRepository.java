package com.spring.baseproject.modules.rbac.repositories;

import com.spring.baseproject.modules.rbac.models.entities.ApiMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiMethodRepository extends JpaRepository<ApiMethod, String> {
}
