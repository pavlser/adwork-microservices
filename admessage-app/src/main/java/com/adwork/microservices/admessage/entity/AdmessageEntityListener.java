package com.adwork.microservices.admessage.entity;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.adwork.microservices.admessage.utils.BeanUtil;

public class AdmessageEntityListener {
	@PrePersist
	public void prePersist(AdmessageEntity target) {
		perform(target, AdmessageAction.Create);
	}

	@PreUpdate
	public void preUpdate(AdmessageEntity target) {
		perform(target, AdmessageAction.Modify);
	}

	@PreRemove
	public void preRemove(AdmessageEntity target) {
		perform(target, AdmessageAction.Delete);
	}

	@Transactional(TxType.REQUIRED)
	private void perform(AdmessageEntity entity, AdmessageAction action) {
		EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
		entityManager.persist(new AuditChangesEntity(entity.lastModifiedBy, action));
	}
}
