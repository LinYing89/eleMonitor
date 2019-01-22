package com.bairock.eleMonitor.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
public class SysRole {

	@Id
    private Long id;
    @NotNull
    private String name;
    private String description;
    @ManyToMany
    private List<SysPermission> permissions;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<SysPermission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<SysPermission> permissions) {
		this.permissions = permissions;
	}
}
