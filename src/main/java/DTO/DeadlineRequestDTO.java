package DTO;

import java.time.LocalDateTime;

public class DeadlineRequestDTO {

	//Holds the deadline as a LocalDateTime
	
	private LocalDateTime deadline;

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
	}
	
	
	
}
