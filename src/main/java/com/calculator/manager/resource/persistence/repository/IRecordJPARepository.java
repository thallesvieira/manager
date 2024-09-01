package com.calculator.manager.resource.persistence.repository;

import com.calculator.manager.resource.persistence.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRecordJPARepository extends JpaRepository<RecordEntity, Long>, JpaSpecificationExecutor<RecordEntity> {
    Optional<RecordEntity> findByIdAndUserId(Long id, Long userId);
}
