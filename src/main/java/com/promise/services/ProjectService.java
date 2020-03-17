package com.promise.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.promise.exceptions.ProjectIdException;
import com.promise.exceptions.ProjectNotFoundException;
import com.promise.models.Backlog;
import com.promise.models.Project;
import com.promise.repositories.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	ProjectRepository projectRepo;
	
	public Project createProject(Project project) {
		String projIdentifier = project.getProjectIdentifier().toLowerCase();
		Project newProject = projectRepo.findByProjectIdentifier(projIdentifier);
		if(newProject != null) {
			throw new ProjectIdException("projectIdentifier '" + projIdentifier + "' already exists choose another name for it");
		}
		
		Backlog backlog = new Backlog();
		project.setBacklog(backlog);
	    backlog.setProject(project);
		backlog.setProjectIdentifier(projIdentifier);
	project.setProjectIdentifier(projIdentifier);
		
		return projectRepo.save(project);
	}
	
	public List<Project> findAllProjects() {
		List<Project>projects = projectRepo.findAll();
		if(projects.size() == 0) {
			throw new ProjectNotFoundException("No projects found");
		}
		return projects;
	}
	
//	public void findProject(String projectIdentifier) {
//		Project project = projectRepo.findByProjectIdentifier(projectIdentifier.toLowerCase());
//		if(project == null) {
//			throw new ProjectIdException("projectIdentifier '" + projectIdentifier + "' does not exist");
//		}
//	}
	
	public Project findProjectById(String projectIdentifier) {
		Project project = projectRepo.findByProjectIdentifier(projectIdentifier.toLowerCase());
		if(project == null) {
			throw new ProjectNotFoundException("projectIdentifier '" + projectIdentifier + "' cannot be found");
		}
		
		return project;
	}
	
	public Project updateProject(String projectIdentifier, Project project) {
		Project project1 = projectRepo.findByProjectIdentifier(projectIdentifier.toLowerCase());
		if(project1 == null) {
			throw new ProjectIdException("projectIdentifier '" + projectIdentifier + "' does not exist so cannot be updated");
		} else if(!project1.getProjectIdentifier().equalsIgnoreCase(project.getProjectIdentifier())) {
			throw new ProjectIdException("Project Identifier cannot be changed so this project has not been updated");
		}
		
		return projectRepo.save(project);
	}
	
	public void deleteById(String projectIdentifier) {
		Project project = projectRepo.findByProjectIdentifier(projectIdentifier.toLowerCase());
		if(project == null) {
			throw new ProjectIdException("projectIdentifier '" + projectIdentifier + "' does not exist so cannot be deleted");
		}
		projectRepo.deleteById(project.getId());
	}
	
	public ResponseEntity<?> validateError(BindingResult result) {
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error: result.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
		
	}
}
