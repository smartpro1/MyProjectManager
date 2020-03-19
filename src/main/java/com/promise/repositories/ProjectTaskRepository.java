package com.promise.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.promise.models.ProjectTask;

@Repository
public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {

	List<ProjectTask> findByProjectIdentifierOrderByPriority(String projectIdentifier);

	ProjectTask findByProjectSequence(String projectSequence);
	//void deleteByProjectSequence(String projectSequence);

	void deleteByProjectSequence(String projectSequence);
}
