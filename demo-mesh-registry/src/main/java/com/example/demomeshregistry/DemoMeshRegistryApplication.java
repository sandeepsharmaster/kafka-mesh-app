package com.example.demomeshregistry;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import static org.apache.kafka.clients.producer.ProducerConfig.*;


@SpringBootApplication
public class DemoMeshRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoMeshRegistryApplication.class, args);
	}

	@Bean
	NewTopic hobbit2() {
		return TopicBuilder.name("hobbit2").partitions(5).replicas(3).build();
	}

	@Bean
	NewTopic counts() {
		return TopicBuilder.name("streams-wordcount-output").partitions(5).replicas(3).build();
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		/*return new DefaultKafkaProducerFactory<>(
				Map.of(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
						RETRIES_CONFIG, 0,
						BUFFER_MEMORY_CONFIG, 33554432,
						KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
						VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
				));*/

		Map<String, Object> configProps = new HashMap<>();
		configProps.put(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				"pkc-ldvj1.ap-southeast-2.aws.confluent.cloud:9092");
		configProps.put(
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		configProps.put(
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}

	@RequiredArgsConstructor
	@Component
	class Producer {

		@Autowired
		private final KafkaTemplate<Integer, String> template;

		//Faker faker;

		@EventListener(ApplicationStartedEvent.class)
		public void generate() {

			/*faker = Faker.instance();
			final Flux<Long> interval = Flux.interval(Duration.ofMillis(1_000));

			final Flux<String> quotes = Flux.fromStream(Stream.generate(() -> faker.hobbit().quote()));

			Flux.zip(interval, quotes)
					.map(it -> template.send("hobbit", faker.random().nextInt(42), it.getT2())).blockLast();*/

			for (int i=0; i<15;i++) {
				template.send("hobbit", 11+i, "Hey Sandy");
		}
	}
}

@Component
class Consumer {
	@KafkaListener(topics = {"streams-wordcount-output"}, groupId = "spring-boot-kafka")
	public void consume(ConsumerRecord<String, Long> record) {
		System.out.println("received = " + record.value() + " with key " + record.key());
	}
}
