package com.example.demomeshvisitor;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;


import static org.apache.kafka.clients.producer.ProducerConfig.*;

import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
public class DemoMeshVisitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoMeshVisitorApplication.class, args);
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		/*return new DefaultKafkaProducerFactory<>(
				Map.of(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092", RETRIES_CONFIG, 0, BUFFER_MEMORY_CONFIG, 33554432,
						KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class, VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
				));*/

		Map<String, Object> configProps = new HashMap<>();
		configProps.put(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				"localhost:9092");
		configProps.put(
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		configProps.put(
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

}
