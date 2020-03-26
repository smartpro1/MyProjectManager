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
import com.promise.models.User;
import com.promise.repositories.ProjectRepository;
import com.promise.repositories.UserRepository;

@Service
public class ProjectService {

	@Autowired
	ProjectRepository projectRepo;
	
	@Autowired
	UserRepository userRepo;
	
	public Project createProject(Project project, String username) {
		User user = userRepo.findByUsername(username);
		if(user == null) {
			throw new ProjectNotFoundException("invalid user");
		}
		
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
	    
	    project.setUser(user);
	    project.setProjectLeader(username);
		
		return projectRepo.save(project);
	}
	
	public List<Project> findAllProjects(String username) {
		List<Project>projects = projectRepo.findAllByProjectLeader(username);
		if(projects.size() == 0) {
			throw new ProjectNotFoundException("No project found");
		}
		return projects;
	}
	
//	public void findProject(String projectIdentifier) {
//		Project project = projectRepo.findByProjectIdentifier(projectIdentifier.toLowerCase());
//		if(project == null) {
//			throw new ProjectIdException("projectIdentifier '" + projectIdentifier + "' does not exist");
//		}
//	}
	
	public Project findProjectById(String projectIdentifier, String username) {
		Project project = projectRepo.findByProjectIdentifier(projectIdentifier.toLowerCase());
		User user = userRepo.findByUsername(username);
		if(project == null) {
			throw new ProjectNotFoundException("projectIdentifier '" + projectIdentifier + "' cannot be found");
		}
		
		if(!project.getProjectLeader().equals(user.getUsername())) {
			throw new ProjectIdException("the project you are looking for does not exist in your account");
		}
		
		return project;
	}
	
	public Project updateProject(String projectIdentifier, Project project, String username) {
		Project project1 = projectRepo.findByProjectIdentifier(projectIdentifier.toLowerCase());
		User user = userRepo.findByUsername(username);
		
		if(project1 == null) {
			throw new ProjectIdException("projectIdentifier '" + projectIdentifier + "' does not exist so cannot be updated");
		} else if(!project1.getProjectIdentifier().equalsIgnoreCase(project.getProjectIdentifier())) {
			throw new ProjectIdException("Project Identifier cannot be changed so this project has not been updated");
		}
		
		if(!project.getProjectLeader().equals(user.getUsername())) {
			throw new ProjectIdException("update declined: this project does not exist in your account");
		}
		
		if(project1.getId() != project.getId()) {
			throw new ProjectIdException("update declined: invalid id");
		}
		
		project.setBacklog(project1.getBacklog());
		
		return projectRepo.save(project);
	}
	
	public void deleteById(String projectIdentifier, String username) {
		Project project = projectRepo.findByProjectIdentifier(projectIdentifier.toLowerCase());
		User user = userRepo.findByUsername(username);
		
		if(project == null) {
			throw new ProjectIdException("projectIdentifier '" + projectIdentifier + "' does not exist so cannot be deleted");
		}
		
		if(!project.getProjectLeader().equals(user.getUsername())) {
			throw new ProjectIdException("delete action declined: this project does not exist in your account");
		}
		
		projectRepo.deleteById(project.getId());
	}
	
	public ResponseEntity<?> validateError(BindingResult result) {
		    System.out.println("got into validate error");
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error: result.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
		
	}
}
