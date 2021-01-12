package com.artificer.osworks.api.model;

import javax.validation.constraints.NotNull;

public class ClientIdInput {

	@NotNull
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Long id;
}
