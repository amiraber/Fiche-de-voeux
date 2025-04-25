package DTO;

import java.time.LocalDate;

public class DeadlineRequestDTO {

	//Holds the deadline as a LocalDateTime
	
	private LocalDate deadline;

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	
	
	
}
