package com.jodev.messaging.authorizationserver.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Long version = 0l;

	@CreatedDate
	protected LocalDateTime createdOn;

	@LastModifiedDate
	protected LocalDateTime updatedOn;

}
