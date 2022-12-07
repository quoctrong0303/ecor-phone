package com.spring.quoctrong.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {

	private String oldPassword;
	private String newPassword;
	private String confirmNewPassword;
}
