package com.devsuperior.dsmovie.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.devsuperior.dsmovie.entities.Movie;

import io.swagger.annotations.ApiModelProperty;

public class MovieDTO {

	@ApiModelProperty(notes = "Database generated movie ID")
	private Long id;

	@NotEmpty(message = "can't be empty")
	@Size(min = 3, max = 50, message = "Length must be between 3 and 50")
	private String title;

	@PositiveOrZero
	@Min(value = 0, message = "Score should not be less than 0")
	@Max(value = 5, message = "Score should not be greater than 5")
	private Double score;
	private Integer count;
	private String image;

	public MovieDTO() {
	}

	public MovieDTO(Long id, String title, Double score, Integer count, String image) {
		this.id = id;
		this.title = title;
		this.score = score;
		this.count = count;
		this.image = image;
	}

	public MovieDTO(Movie movie) {
		id = movie.getId();
		title = movie.getTitle();
		score = movie.getScore();
		count = movie.getCount();
		image = movie.getImage();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Movie toEntity() {
		return new Movie(id, title, score, count, image);
	}
}
