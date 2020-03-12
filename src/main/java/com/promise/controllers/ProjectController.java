package com.promise.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.promise.models.Project;
import com.promise.services.ProjectService;



@RestController
@RequestMapping("/api/project")
public class ProjectController {
	
	@Autowired
	ProjectService projectService;
  
	@PostMapping
	public ResponseEntity<?> createProject(@Valid @RequestBody Project project, BindingResult result) {
		if(result.hasErrors()) return projectService.validateError(result);
		
		Project newProject = projectService.createProject(project);
		return new ResponseEntity<Project>(newProject, HttpStatus.CREATED);
	}
	
	@GetMapping()
	public ResponseEntity<List<Project>> findAllProjects(){
		List<Project> projects = projectService.findAllProjects();
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}
	
	@GetMapping("/{projectIdentifier}") 
	public ResponseEntity<?> findProjectById(@PathVariable() String projectIdentifier) {
		Project project = projectService.findProjectById(projectIdentifier);
		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}
	
	@DeleteMapping("/{projectIdentifier}")
	public ResponseEntity<?> deleteProjectById(@PathVariable() String projectIdentifier) {
		 projectService.deleteById(projectIdentifier);;
		return new ResponseEntity<String>("Project with id '" + projectIdentifier + "' deleted", HttpStatus.OK);
	}
}
