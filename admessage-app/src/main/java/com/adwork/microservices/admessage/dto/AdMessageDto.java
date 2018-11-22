package com.adwork.microservices.admessage.dto;

import java.util.Date;

public class AdMessageDto {
	public Long id;
	public String title;
	public String body;
	public Integer likesCount;
	public Integer dislikesCount;
	public Integer commentsCount;
	public String createdBy;
	public Date createdDate;
	public Date lastModifiedDate;
}
