package com.adwork.microservices.admessage.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "ad_message")
@EntityListeners(AdmessageEntityListener.class)
public class AdmessageEntity extends AuditableEntity<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "title", nullable = false, length = 100)
	@Length(max = 100)
	String title;

	@Column(name = "body", nullable = false, length = 5000)
	@Length(max = 5000)
	String body;

	@Column(name = "likes_count", columnDefinition = "int default 0")
	Integer likesCount;

	@Column(name = "dislikes_count", columnDefinition = "int default 0")
	Integer dislikesCount;

	@Column(name = "comments_count", columnDefinition = "int default 0")
	Integer commentsCount;

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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(Integer likesCount) {
		this.likesCount = likesCount;
	}

	public Integer getDislikesCount() {
		return dislikesCount;
	}

	public void setDislikesCount(Integer dislikesCount) {
		this.dislikesCount = dislikesCount;
	}

	public Integer getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Integer commentsCount) {
		this.commentsCount = commentsCount;
	}

}
