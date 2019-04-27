package com.sun.health.newwork.record.repository;

import com.sun.health.newwork.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
