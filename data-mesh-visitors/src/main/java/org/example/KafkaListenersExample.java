package org.example;

import org.example.pojo.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Component
class KafkaListenersExample {

	private final Logger LOG = LoggerFactory.getLogger(KafkaListenersExample.class);

	@KafkaListener(topics = "reflectoring-1")
	void listener(String message) {
		LOG.info("Listener [{}]", message);
	}

	@KafkaListener(id = "1", topics = "reflectoring-user", groupId = "reflectoring-user-mc", containerFactory = "kafkaJsonListenerContainerFactory")
	void listenerWithMessageConverter(User user) {
		LOG.info("MessageConverterUserListener [{}]", user);
	}

	/*@KafkaListener(id = "2", topics = "visitors", groupId = "visitors-group", containerFactory = "kafkaJsonListenerContainerFactory")
	void listenerWithMessageConverter(Visitor visitor) {
		LOG.info("MessageConverterUserListener [{}]", visitor);
	}
*/
	@KafkaListener(topics = {"visitors"}, groupId = "visitors-group")
	public void consume(ConsumerRecord<String, Visitor> record) {
		System.out.println("received = " + record.value() + " with key " + record.key());
	}
}
