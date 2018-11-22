package com.adwork.microservices.admessage.conrtoller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adwork.microservices.admessage.dto.AdMessageDto;
import com.adwork.microservices.admessage.entity.AdmessageEntity;
import com.adwork.microservices.admessage.service.AdmessagesRepository;

@RestController
@RequestMapping("/api/admessages")
public class AdmessagesController {
	
	AdmessagesRepository repo;
	
	public AdmessagesController(AdmessagesRepository repo) {
		this.repo = repo;
	}
	
	@RequestMapping(path = "all", method = RequestMethod.GET)
	public List<AdMessageDto> all() {
		return repo.findAll().stream().map(entity -> toDto(entity)).collect(Collectors.toList());
	}
	
	@GetMapping("{id}")
	public AdMessageDto one(@PathVariable("id") Long id) {
		return toDto(repo.findById(id).orElseThrow(() -> new AdMessagesException("Ad not found")));
	}
	
	@PostMapping
	public Long create(@RequestBody AdMessageDto dto) {
		return repo.save(fromDto(dto)).getId();
	}
	
	@PutMapping
	public AdMessageDto update(@RequestBody AdMessageDto dto) {
		AdmessageEntity updatedEntity = repo.findById(dto.id).map(entity -> {
			entity.setTitle(dto.title);
			entity.setBody(dto.body);
			return repo.save(entity);
		}).orElseThrow(() -> new AdMessagesException("Ad not found"));
		return toDto(updatedEntity);
	}
	
	@DeleteMapping("{id}")
	public boolean delete(@PathVariable("id") Long id) {
		AdmessageEntity entity = repo.findById(id).orElseThrow(() -> new AdMessagesException("Ad not found"));
		repo.delete(entity);
		return true;
	}
	
	AdMessageDto toDto(AdmessageEntity entity) {
		AdMessageDto dto = new AdMessageDto();
		dto.id = entity.getId();
		dto.title = entity.getTitle();
		dto.body = entity.getBody();
		dto.createdBy = entity.getCreatedBy();
		dto.createdDate = entity.getCreatedDate();
		dto.lastModifiedDate = entity.getLastModifiedDate();
		dto.commentsCount = entity.getCommentsCount();
		dto.likesCount = entity.getLikesCount();
		dto.dislikesCount = entity.getDislikesCount();
		return dto;
	}
	
	AdmessageEntity fromDto(AdMessageDto dto) {
		AdmessageEntity entity = new AdmessageEntity();
		entity.setTitle(dto.title);
		entity.setBody(dto.body);
		return entity;
	}

}
