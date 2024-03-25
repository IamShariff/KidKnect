package com.kidknect.enums;

import java.util.Arrays;
import java.util.Optional;

public enum AttendenceStatus {

	ABSENT("A"), PRESENT("P"), UNMARKED("U"), HOLIDAY("H");

	private String attendence;

	private AttendenceStatus(String attendence) {
		this.attendence = attendence;
	}

	public String getAttendence() {
		return this.attendence;
	}

	public static boolean hasAttendence(String att) {
		for (AttendenceStatus a : AttendenceStatus.values()) {
			if (a.getAttendence().equalsIgnoreCase(att)) {
				return true;
			}
		}
		return false;
	}

	public static Optional<AttendenceStatus> fromString(String at) {
		return Arrays.stream(AttendenceStatus.values()).filter(role -> role.getAttendence().equalsIgnoreCase(at))
				.findFirst();
	}

}
