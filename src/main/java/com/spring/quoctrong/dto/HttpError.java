package com.spring.quoctrong.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpError {

	private Integer code;
	private String name;
	private String message;
}
