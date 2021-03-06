package com.promise.models;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements UserDetails{
  
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message="please enter your fullname")
	private String fullname;
	@Email(message="your username must be a valid email")
	@NotBlank(message="username field is required")
	@Column(unique=true)
	private String username;
	@NotBlank(message="password is required")
	private String password;
	
	@Transient
	private String confirmPassword;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date created_At;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updated_At;
	
	// OneToMany with Project
	@OneToMany(cascade = CascadeType.REFRESH, mappedBy="user", orphanRemoval=true)
	private List<Project> projects = new ArrayList<>();
	
	// OneToOne with PasswordReset
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="user")
	@JsonIgnore
	private PasswordReset passwordReset;
	
	//private String resetToken;
	
    public User() {
		
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Date getCreated_At() {
		return created_At;
	}

	public void setCreated_At(Date created_At) {
		this.created_At = created_At;
	}

	public Date getUpdated_At() {
		return updated_At;
	}

	public void setUpdated_At(Date updated_At) {
		this.updated_At = updated_At;
	}
	

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}


	@PrePersist
	protected void onCreate() {
		this.created_At = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updated_At = new Date();
	}
	


	public PasswordReset getPasswordReset() {
		return passwordReset;
	}


	public void setPasswordReset(PasswordReset passwordReset) {
		this.passwordReset = passwordReset;
	}


	/*
	 * UserDetails interface methods
	 * 
	 * */
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return null;
	}
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
	
}
