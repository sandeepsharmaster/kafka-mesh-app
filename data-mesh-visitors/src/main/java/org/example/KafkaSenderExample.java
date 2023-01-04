package org.example;

import org.example.pojo.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.apache.kafka.clients.producer.ProducerRecord;
@Component
public class KafkaSenderExample {

	private final Logger LOG = LoggerFactory.getLogger(KafkaSenderExample.class);

	private KafkaTemplate<String, Visitor> visitorKafkaTemplate;

	@Autowired
	KafkaSenderExample(KafkaTemplate<String, Visitor> visitorKafkaTemplate) {
		/*this.kafkaTemplate = kafkaTemplate;
		this.routingKafkaTemplate = routingKafkaTemplate;*/

		this.visitorKafkaTemplate = visitorKafkaTemplate;
	}

	public void sendVisitorMessage(Visitor visitor, String topicName) {
		LOG.info("--------------------------------");
		LOG.info("Sending Avro Serializer : {}", visitor);
		LOG.info("--------------------------------");
		ProducerRecord<String, Visitor> producerRecord = new ProducerRecord<String, Visitor>(topicName, "Custom Visitor-Key",visitor);
		visitorKafkaTemplate.send(producerRecord);
		//visitorKafkaTemplate.send(topicName, visitor);
	}
}
