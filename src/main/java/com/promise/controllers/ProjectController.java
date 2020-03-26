package com.promise.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.promise.models.Project;
import com.promise.services.ProjectService;



@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {
	
	@Autowired
	ProjectService projectService;
  
	@PostMapping
	public ResponseEntity<?> createProject(@Valid @RequestBody Project project, BindingResult result, Principal principal) {
		if(result.hasErrors()) return projectService.validateError(result);
		
		Project newProject = projectService.createProject(project, principal.getName());
		return new ResponseEntity<Project>(newProject, HttpStatus.CREATED);
	}
	
	@GetMapping()
	public ResponseEntity<List<Project>> findAllProjects(Principal principal){
		List<Project> projects = projectService.findAllProjects(principal.getName());
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}
	
	@GetMapping("/{projectIdentifier}") 
	public ResponseEntity<?> findProjectById(@PathVariable() String projectIdentifier, Principal principal) {
		Project project = projectService.findProjectById(projectIdentifier, principal.getName());
		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}
	

	@PutMapping(value="/{projectIdentifier}", consumes="application/json")
	public ResponseEntity<?> updateProject(@PathVariable() String projectIdentifier,@Valid @RequestBody Project project, 
			                               BindingResult result, Principal principal) {
		if(result.hasErrors()) return projectService.validateError(result);

		

		Project updateProject = projectService.updateProject(projectIdentifier, project, principal.getName());

		return new ResponseEntity<Project>(updateProject, HttpStatus.OK);
	}
	
	@DeleteMapping("/{projectIdentifier}")
	public ResponseEntity<?> deleteProjectById(@PathVariable() String projectIdentifier,  Principal principal) {
		 projectService.deleteById(projectIdentifier, principal.getName());;
		return new ResponseEntity<String>("Project with id '" + projectIdentifier + "' deleted", HttpStatus.OK);
	}
}
