package com.adwork.microservices.admessage.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class AuditChangesEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	Long id;

	@Column(name = "user")
	String user;

	@Column(name = "action")
	AdmessageAction action;

	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	Date date;
	
	public AuditChangesEntity() {}
	
	public AuditChangesEntity(String user, AdmessageAction action) {
		this.user = user;
		this.action = action;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public AdmessageAction getAction() {
		return action;
	}

	public void setAction(AdmessageAction action) {
		this.action = action;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
