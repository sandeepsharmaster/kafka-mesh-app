package org.example;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.example.pojo.Booking;
import org.slf4j.Logger;
import org.example.pojo.Offers;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.kafka.streams.kstream.Consumed;
@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
public class SpringKafkaExampleApplication {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaExampleApplication.class, args);
	}
}

@Component
class Processor {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Value("${org.example.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Autowired
	private KafkaSenderExample kafkaSenderExample;

	@Autowired
	public void process(StreamsBuilder builder) {

		System.out.println("******* Stream processing started *******");
		// Serializers/deserializers (serde) for String and Long types
		final Serde<Integer> integerSerde = Serdes.Integer();
		final Serde<String> stringSerde = Serdes.String();
		final Serde<Long> longSerde = Serdes.Long();
		final Serde<GenericRecord> keyGenericAvroSerde = new GenericAvroSerde();

		Properties configProps = new Properties();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		//configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		//configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
		configProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
		configProps.put(StreamsConfig.CLIENT_ID_CONFIG, "123-X");

		configProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "spring-boot-streams");
		configProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		configProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,  new GenericAvroSerde().getClass().getName());

		// Construct a `KStream` from the input topic "streams-plaintext-input", where message values
		// represent lines of text (for the sake of this example, we ignore whatever may be stored
		// in the message keys).
		try {

			// When you want to override serdes explicitly/selectively
			final Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", "http://localhost:8081");

			final Serde<GenericRecord> valueGenericAvroSerde = new GenericAvroSerde();
			valueGenericAvroSerde.configure(serdeConfig, false); // `false` for record values

			//StreamsBuilder streamsBuilder = new StreamsBuilder();
			System.out.println("Stream Processing try block");
			KStream<String, GenericRecord> kStream = builder.stream("bookings", Consumed.with(stringSerde, valueGenericAvroSerde));
			kStream.foreach((k, v) -> { System.out.println("Key= " + k + " Value= " +v + "Value Class" + v.getClass());


				System.out.println("Booking Object Payment Type : " + v.get("payment_type").toString());

				String first_name = v.get("payment_type").toString();
				String last_name = v.get("last_name").toString();
				String booking_id = v.get("booking_id").toString();
				String payment_type = v.get("payment_type").toString();
				String booking_source = v.get("booking_source").toString();
				String booking_status = v.get("booking_status").toString();

				String cashback = "0";

				if(payment_type.equals("CC")) {
					cashback = "10%";
				}
				if(booking_source.equalsIgnoreCase("MakeMyTrip")) {
					cashback = "20%";
				}

						Offers offer = Offers.newBuilder()
								.setBookingId(booking_id)
								.setFirstName(first_name)
								.setBookingSource(booking_source)
								.setPaymentType(payment_type)
								.setCashBack(cashback)
								.build();
						kafkaSenderExample.sendOffersMessage(offer, "offers");
			}
			);

			Topology topology = builder.build();
			KafkaStreams streams = new KafkaStreams(topology, configProps);

			LOG.info("Starting stream");
			streams.start();

			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					LOG.info("Shutting down stream");
			streams.close();}));


		} catch(Exception  exception) {
			System.out.println("===========  EXCEPTION IN STREAM PROCESSING  ======================="+ exception);
			System.out.println(exception.getMessage());
		}

	}
}

@RestController
@RequiredArgsConstructor
class RestService {

	private final StreamsBuilderFactoryBean factoryBean;



	@GetMapping("/count/{word}")
	public Long getCount(@PathVariable String word) {
		final KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();

		final ReadOnlyKeyValueStore<String, Long> counts = kafkaStreams.store(StoreQueryParameters.fromNameAndType("counts", QueryableStoreTypes.keyValueStore()));
		return counts.get(word);
	}

}