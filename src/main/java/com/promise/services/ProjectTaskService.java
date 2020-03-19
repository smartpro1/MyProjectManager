package com.promise.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		Integer backlogSequence = backlog.getProjectSequence();
		backlogSequence += 1;
		backlog.setProjectSequence(backlogSequence);
		
		projectTask.setProjectIdentifier(projectIdentifier);
		projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
		if(projectTask.getStatus() == null || projectTask.getStatus() == "") {
			projectTask.setStatus("TO_DO");
		}
		if(projectTask.getPriority() == null || projectTask.getPriority() == 0) {
			projectTask.setPriority(3);
		}
		
		projectTask.setBacklog(backlog);
		
		return projectTaskRepo.save(projectTask);
			
	}
	
	public List<ProjectTask> getProjectTasksByProjectIdentifier(String projectIdentifier){
		
		if(!isBacklog(projectIdentifier)) {
			throw new ProjectNotFoundException("The projectIdentifier supplied '" + projectIdentifier + "' is invalid.");
		}
		return projectTaskRepo.findByProjectIdentifierOrderByPriority(projectIdentifier);	
	}
	
	public ProjectTask findProjectByProjectSequence(String projectIdentifier, String projectSequence) {
		
		ProjectTask projectTask = projectTaskRepo.findByProjectSequence(projectSequence);
		if(!isBacklog(projectIdentifier) || projectTask == null) {
			throw new ProjectNotFoundException("Invalid project Identifier or projectSequence");
		}
		
		if(!projectTask.getProjectIdentifier().equals(projectIdentifier)) {
			throw new ProjectNotFoundException("The projectIdentifier '" + projectIdentifier +"' cannot be found in this project task");
		}
		return projectTask;
	}
	
	public ProjectTask updateProjectTaskByProjectSequence(String projectIdentifier, String projectSequence, ProjectTask projectTask) {
		ProjectTask projectTask1 = projectTaskRepo.findByProjectSequence(projectSequence);
		if(!isBacklog(projectIdentifier) || projectTask1 == null) {
			throw new ProjectNotFoundException("Invalid project Identifier or projectSequence");
		}
		
		if(!projectTask1.getProjectIdentifier().equals(projectIdentifier)) {
			throw new ProjectNotFoundException("The projectIdentifier '" + projectIdentifier +"' cannot be found in this project task");
		}
		
		if(!projectTask.getProjectIdentifier().equals(projectIdentifier)) {
			throw new ProjectNotFoundException("update declined: projectIdentifier cannot be changed");
		}
		
		return projectTaskRepo.save(projectTask);
	}
	
	@Transactional
	public void deleteProjectTaskByProjectSequence(String projectIdentifier, String projectSequence) {
		ProjectTask projectTask1 = projectTaskRepo.findByProjectSequence(projectSequence);
		if(!isBacklog(projectIdentifier) || projectTask1 == null) {
			throw new ProjectNotFoundException("Delete declined: invalid project Identifier or projectSequence");
		}
		
		if(!projectTask1.getProjectIdentifier().equals(projectIdentifier)) {
			throw new ProjectNotFoundException("Delete declined: the projectIdentifier '" + projectIdentifier +"' cannot be found in this project task");
		}
		
		projectTaskRepo.deleteByProjectSequence(projectSequence);
		                
	}
	
	public Boolean isBacklog(String projectIdentifier) {
		Backlog backlog = backlogRepo.findByProjectIdentifier(projectIdentifier);
		if(backlog == null) return false;
		return true;
	}
	
	

}
