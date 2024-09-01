package com.calculator.manager.resource.persistence.repository;

import com.calculator.manager.resource.persistence.entity.UserEntity;
import com.calculator.manager.resource.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IWalletJPARepository extends JpaRepository<WalletEntity, Long> {
    Optional<WalletEntity> findByUser(UserEntity userEntity);
}
