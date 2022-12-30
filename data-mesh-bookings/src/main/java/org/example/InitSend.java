package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import org.example.pojo.Visitor;
@Component
class InitSend {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private KafkaSenderExample kafkaSenderExample;
	
	/*@Autowired
	private KafkaSenderWithMessageConverter messageConverterSender;*/
	
	@Value("${io.reflectoring.kafka.topic-1}")
	private String topic1;

	@Value("${io.reflectoring.kafka.topic-2}")
	private String topic2;
	
	@Value("${io.reflectoring.kafka.topic-3}")
	private String topic3;

	@Value("${org.example.kafka.topic.visitor}")
	private String visitorTopic;
	
	@EventListener
	void initiateSendingMessage(ApplicationReadyEvent event) throws InterruptedException {

		Thread.sleep(5000);
		LOG.info("---------------------------------");
		Visitor visitor = Visitor.newBuilder()
				.setAge(35)
				//.setAutomatedEmail(false)
				.setBookingId("XY123")
				.setFirstName("John")
				.setLastName("Doe")
				.setBookingSource("MakeMyTrip")
				.build();
		kafkaSenderExample.sendVisitorMessage(visitor, "visitors");

		Thread.sleep(5000);
		LOG.info("---------------------------------");
		kafkaSenderExample.sendCustomMessage(new User("Lucario"), "reflectoring-user");

	}
}
