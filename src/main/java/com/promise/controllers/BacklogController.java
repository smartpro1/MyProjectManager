package com.promise.controllers;

import java.security.Principal;
import java.util.List;

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
import com.promise.models.ProjectTask;
import com.promise.services.ProjectService;
import com.promise.services.ProjectTaskService;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {
	
	@Autowired
	private ProjectTaskService projectTaskService;
	
	@Autowired 
	private ProjectService projectService;
	
	@PostMapping("/{projectIdentifier}")
	public ResponseEntity<?> createProjectTask(@PathVariable String projectIdentifier, @Valid @RequestBody ProjectTask projectTask,
			BindingResult result, Principal principal){
		if(result.hasErrors()) return projectService.validateError(result);
		ProjectTask newProjectTask = projectTaskService.createProjectTask(projectIdentifier, projectTask, principal.getName());
		return new ResponseEntity<ProjectTask>(newProjectTask, HttpStatus.CREATED);
	}
	
	@GetMapping("/{projectIdentifier}")
	public ResponseEntity<?> getProjectTasksByProjectIdentifier(@PathVariable String projectIdentifier, Principal principal){
		List<ProjectTask> projectTasks = projectTaskService.getProjectTasksByProjectIdentifier(projectIdentifier, principal.getName());
		return new ResponseEntity<List<ProjectTask>>(projectTasks, HttpStatus.OK);
	}
	
	@GetMapping("/{projectIdentifier}/{projectSequence}")
	public ResponseEntity<?> getProjectByProjectSequence(@PathVariable String projectIdentifier,  @PathVariable String projectSequence, Principal principal){
		ProjectTask projectTask = projectTaskService.findProjectByProjectSequence(projectIdentifier, projectSequence, principal.getName());
		
		return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
		
	}
	
	@PutMapping(value="/{projectIdentifier}/{projectSequence}", consumes="application/json")
	public ResponseEntity<?> updateProject(@PathVariable String projectIdentifier, @PathVariable String projectSequence,
			                               @Valid @RequestBody ProjectTask projectTask, BindingResult result, Principal principal) {
		if(result.hasErrors()) return projectService.validateError(result);
		
		ProjectTask updateProjectByPTSequence = projectTaskService.updateProjectTaskByProjectSequence(projectIdentifier, 
				                                                  projectSequence, projectTask, principal.getName());
		return new ResponseEntity<ProjectTask>(updateProjectByPTSequence, HttpStatus.OK);
	}
	
	@DeleteMapping("/{projectIdentifier}/{projectSequence}")
	public ResponseEntity<?> deleteProjectTaskByPTSequence(@PathVariable String projectIdentifier, @PathVariable String projectSequence,
			                                                Principal principal) {
	    projectTaskService.deleteProjectTaskByProjectSequence(projectIdentifier, projectSequence, principal.getName());
	    return new ResponseEntity<String>("ProjectTask with projectSequence '" + projectSequence +"' deleted", HttpStatus.OK);
	}

}
