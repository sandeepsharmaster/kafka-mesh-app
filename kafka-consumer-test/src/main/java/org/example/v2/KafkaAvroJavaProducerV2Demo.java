package org.example.v2;

import org.example.pojo.Visitor;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaAvroJavaProducerV2Demo {

    public static void main(String[] args) {
        Properties properties = new Properties();
        // normal producer
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("acks", "all");
        properties.setProperty("retries", "10");
        // avro part
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

        Producer<String, Visitor> producer = new KafkaProducer<String, Visitor>(properties);

        String topic = "customer-avro";

        // copied from avro examples
        Visitor customer = Visitor.newBuilder()
                .setAge(34)
                .setFirstName("John")
                .setLastName("Doe")
                //.setEmail("john.doe@gmail.com")
                //.setPhoneNumber("(123)-456-7890")
                .build();

        ProducerRecord<String, Visitor> producerRecord = new ProducerRecord<String, Visitor>(
                topic, customer
        );

        System.out.println(customer);
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.println(metadata);
                } else {
                    exception.printStackTrace();
                }
            }
        });

        producer.flush();
        producer.close();

    }
}
