package org.example;

import org.example.pojo.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.producer.ProducerRecord;
@Component
public class KafkaSenderExample {

	private final Logger LOG = LoggerFactory.getLogger(KafkaSenderExample.class);

	/*private KafkaTemplate<String, String> kafkaTemplate;
	private RoutingKafkaTemplate routingKafkaTemplate;*/
	private KafkaTemplate<String, User> userKafkaTemplate;

	private KafkaTemplate<String, Booking> visitorKafkaTemplate;

	@Autowired
	KafkaSenderExample(
			KafkaTemplate<String, User> userKafkaTemplate, KafkaTemplate<String, Booking> visitorKafkaTemplate) {
		/*this.kafkaTemplate = kafkaTemplate;
		this.routingKafkaTemplate = routingKafkaTemplate;*/
		this.userKafkaTemplate = userKafkaTemplate;
		this.visitorKafkaTemplate = visitorKafkaTemplate;
	}

	public void sendBookingMessage(Booking visitor, String topicName) {
		LOG.info("--------------------------------");
		LOG.info("Sending Avro Serializer : {}", visitor);
		LOG.info("--------------------------------");
		ProducerRecord<String, Booking> producerRecord = new ProducerRecord<String, Booking>(topicName, visitor);
		visitorKafkaTemplate.send(producerRecord);
		//visitorKafkaTemplate.send(topicName, visitor);
	}

	void sendCustomMessage(User user, String topicName) {
		LOG.info("Sending Json Serializer : {}", user);
		LOG.info("--------------------------------");

		userKafkaTemplate.send(topicName, user);
	}

}
