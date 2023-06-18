package com.fileoperations.entity;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class Role extends BaseEntity{
	@Column(name = "role_name",unique = true)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
