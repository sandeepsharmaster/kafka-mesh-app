package org.example;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaTopicConfig {

	@Value("${org.example.kafka.topic.users}")
	private String topic1;

	@Value("${org.example.kafka.topic.bookings}")
	private String topic2;

	@Value("${org.example.kafka.topic.offers}")
	private String topic3;

	@Bean
	NewTopic topic1() {
		return TopicBuilder.name(topic1).build();
	}

	@Bean
	NewTopic topic2() {
		return TopicBuilder.name(topic2).build();
	}

	@Bean
	NewTopic topic3() {
		return TopicBuilder.name(topic3).build();
	}

}
