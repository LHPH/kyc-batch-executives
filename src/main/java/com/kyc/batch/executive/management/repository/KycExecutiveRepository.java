package com.kyc.batch.executive.management.repository;

import com.kyc.batch.executive.management.entity.KycExecutive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface KycExecutiveRepository extends JpaRepository<KycExecutive, Long> {

    @Query(name = "KycExecutive.getExecutivesToProcess")
    Page<KycExecutive> getExecutivesToProcess(Pageable pageable);
}
