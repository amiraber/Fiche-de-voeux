package DTO;

import java.time.LocalDate;

public class DeadlineRequestDTO {

	//Holds the deadline as a LocalDateTime
	
	private LocalDate deadline;
	private Long profId; 

	public Long getProfId() {
		return profId;
	}

	public void setProfId(Long profId) {
		this.profId = profId;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	
	
	
}
