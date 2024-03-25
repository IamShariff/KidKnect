package com.kidknect.responsedto;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.kidknect.enums.AttendenceStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayAttendanceResponseDto {

	private LocalDate date;

	@Enumerated(EnumType.STRING)
	private AttendenceStatus status;

	@Transient
	private DayOfWeek currentDay;

	public DayOfWeek getCurrentDay() {
		return date.getDayOfWeek();
	}

	public void setCurrentDay(DayOfWeek currentDay) {
		this.currentDay = currentDay;
	}

}
