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


	private KafkaTemplate<String, Booking> visitorKafkaTemplate;

	@Autowired
	KafkaSenderExample( KafkaTemplate<String, Booking> visitorKafkaTemplate) {

		this.visitorKafkaTemplate = visitorKafkaTemplate;
	}

	public void sendBookingMessage(Booking booking, String topicName) {
		LOG.info("--------------------------------");
		LOG.info("Sending Avro Serializer : {}", booking);
		LOG.info("--------------------------------");
		ProducerRecord<String, Booking> producerRecord = new ProducerRecord<String, Booking>(topicName, "Custom-booking-key-2", booking);
		visitorKafkaTemplate.send(producerRecord);

	}

}
