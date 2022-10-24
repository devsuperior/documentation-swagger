package com.devsuperior.dsmovie.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ScoreDTO {

	private Long movieId;
	
	@NotBlank
	@Email
	private String email;
	private Double score;
	
	public ScoreDTO() {
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
}
