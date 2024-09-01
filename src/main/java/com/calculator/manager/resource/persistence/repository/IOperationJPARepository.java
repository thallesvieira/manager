package com.calculator.manager.resource.persistence.repository;

import com.calculator.manager.domain.model.operation.OperationType;
import com.calculator.manager.resource.persistence.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOperationJPARepository extends JpaRepository<OperationEntity, Long> {
    Optional<OperationEntity> findByType(OperationType type);
    List<OperationEntity> findAll();
}
