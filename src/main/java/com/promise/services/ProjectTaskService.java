package com.promise.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promise.exceptions.ProjectNotFoundException;
import com.promise.models.Backlog;
import com.promise.models.ProjectTask;
import com.promise.repositories.BacklogRepository;
import com.promise.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	@Autowired
	private BacklogRepository backlogRepo;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepo;
	
	public ProjectTask createProjectTask(String projectIdentifier, ProjectTask projectTask) {
		Backlog backlog = backlogRepo.findByProjectIdentifier(projectIdentifier);
		if(backlog == null) {
			throw new ProjectNotFoundException("The projectIdentifier supplied '" + projectIdentifier + "' is invalid.");
		}
		Integer newSequence = backlog.getProjectSequence() + 1;
		projectTask.setProjectIdentifier(projectIdentifier);
		projectTask.setProjectSequence(projectIdentifier + "-" + newSequence);
		if(projectTask.getStatus() == null || projectTask.getStatus() == "") {
			projectTask.setStatus("TO_DO");
		}
		if(projectTask.getPriority() == null || projectTask.getPriority() == 0) {
			projectTask.setPriority(3);
		}
		
		projectTask.setBacklog(backlog);
		
		return projectTaskRepo.save(projectTask);
			
	}

}
