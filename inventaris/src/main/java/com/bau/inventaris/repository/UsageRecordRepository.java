package com.bau.inventaris.repository;

import com.bau.inventaris.entity.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {
}
