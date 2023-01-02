package org.example;

import org.example.pojo.Booking;
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


	@KafkaListener(topics = {"bookings"}, groupId = "bookings-group")
	public void consume(ConsumerRecord<String, Booking> record) {
		System.out.println("received = " + record.value() + " with key " + record.key());
	}
}
