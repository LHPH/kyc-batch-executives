package com.kyc.batchs.executive.management.repository;

import com.kyc.batchs.executive.management.entity.KycUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycUserRepository extends JpaRepository<KycUser,Long> {


}
