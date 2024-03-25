package com.kidknect.responsedto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificatonPubishResponseDto {

	private int notificationId;
	private String message;
	private LocalDate publishDate;
	private String publishedBy;

}
