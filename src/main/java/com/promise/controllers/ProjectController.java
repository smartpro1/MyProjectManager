package com.promise.controllers;

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
import org.springframework.web.bind.annotation.RequestMethod;
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
	
<<<<<<< HEAD
	@PutMapping(value="/{projectIdentifier}")
	//@RequestMapping(value = "/{projectIdentifier}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProject(@PathVariable("projectIdentifier") String projectIdentifier, @Valid @RequestBody Project project, BindingResult result) {
		System.out.println("got here 1");
		if(result.hasErrors()) {
			System.out.println("entered hasErrors");
			System.out.println(result);
			return projectService.validateError(result);
		}
		System.out.println("got here 2");
=======
	@PutMapping(value="/{projectIdentifier}", consumes="application/json")
	public ResponseEntity<?> updateProject(@PathVariable() String projectIdentifier,@Valid @RequestBody Project project, BindingResult result) {
		if(result.hasErrors()) return projectService.validateError(result);
>>>>>>> develop
		
		Project updateProject = projectService.updateProject(projectIdentifier, project);
		System.out.println("passed updateProject");
		return new ResponseEntity<Project>(updateProject, HttpStatus.OK);
	}
	
	@DeleteMapping("/{projectIdentifier}")
	public ResponseEntity<?> deleteProjectById(@PathVariable() String projectIdentifier) {
		 projectService.deleteById(projectIdentifier);;
		return new ResponseEntity<String>("Project with id '" + projectIdentifier + "' deleted", HttpStatus.OK);
	}
}
