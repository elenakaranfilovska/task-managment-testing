package com.ios.backend.repositories;

import com.ios.backend.entities.TaskRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRecordRepository extends JpaRepository<TaskRecord, Long> {

    List<TaskRecord> findTaskRecordByCreatedBy(long user);

    List<TaskRecord> findByProgramAndCreatedBy(long program, long user);

    TaskRecord findByProgramAndCreatedByAndTask(long program, long user, long task);
}
