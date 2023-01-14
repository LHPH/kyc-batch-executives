package com.kyc.batchs.executive.management.repository;

import com.kyc.batchs.executive.management.entity.KycExecutive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface KycExecutiveRepository extends JpaRepository<KycExecutive, Long> {

    @Query("from KycExecutive e where e.batchOperationControl in (0,2,3) and (e.creationDate = CURRENT_DATE or e.updatedDate = CURRENT_DATE)")
    Page<KycExecutive> getExecutivesToProcess(Pageable pageable);
}
