package com.adwork.microservices.admessage.kafka;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AdworkKafkaIntegration {

	@Value("${adwork.kafka.topic.messages}")
	String adMessageTopicName;
	
	@Value("${adwork.kafka.client-id}")
	String adMessageClientAppId;
	
	KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	public AdworkKafkaIntegration(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	@PostConstruct
	void onCreate() {
		sendAdMessage("Hello Kaaaafkaaaa 1");
		sendAdMessage("Hello Kaaaafkaaaa 2");
		sendAdMessage("Hello Kaaaafkaaaa 3");
	}

	public void sendAdMessage(String msg) {
		kafkaTemplate.send(adMessageTopicName, adMessageClientAppId, msg);
	}

	@KafkaListener(
			id="${adwork.kafka.client-id}",
			topics="${adwork.kafka.topic.messages}", 
			groupId="${adwork.kafka.group-id}")
	public void processMessage(String content) {
		System.out.println("AdworkKafkaIntegration.processMessage() =>>>> " + content);
	}

}
