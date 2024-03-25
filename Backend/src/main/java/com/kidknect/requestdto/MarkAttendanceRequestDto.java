package com.kidknect.requestdto;

import lombok.Data;

import java.util.List;

@Data
public class MarkAttendanceRequestDto {

	private int className;
	private List<AttendanceStatusEntry> attendanceStatuses;

	@Data
	public static class AttendanceStatusEntry {
		private int rollNo;
		private String attendanceStatus;
	}
}
