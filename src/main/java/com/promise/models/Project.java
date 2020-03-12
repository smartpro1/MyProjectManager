package com.promise.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message="project name is required")
	@Size(min=3, max = 20, message="project name must be between three to twenty characters long")
	private String projectName;
	@NotBlank(message="project identifier is required")
	@Size(min=3,max=10, message="project identifier must be between three to ten characters long")
	@Column(updatable=false)
	private String projectIdentifier;
	@Size(min=3,max=50, message="project identifier must be between three to fifty characters long")
	@NotBlank(message="project description is required")
	private String description;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String start_date;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String end_date;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date created_at;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updated_at;
	
	@PrePersist
	protected void onCreate(){
		this.created_at = new Date();
	}
	
	@PreUpdate
	protected void onUpdate(){
		this.updated_at = new Date();
	}

	public Project() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectIdentifier() {
		return projectIdentifier;
	}

	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	

}