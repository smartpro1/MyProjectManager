package com.promise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.promise.models.ProjectTask;

@Repository
public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {

}
