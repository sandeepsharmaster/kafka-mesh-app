package org.example.v1;

import org.example.pojo.Booking;
import org.example.pojo.Visitor;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaAvroJavaConsumerV1Demo {

    public static void main(String[] args) {

        Map<String, Visitor> visitorMap = new HashMap();
        Map<String, Booking> bookingMap = new HashMap();
        Properties properties = new Properties();
        // normal consumer
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");
        properties.put("group.id", "visitor-consumer-group-v1");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "earliest");

        // avro part (deserializer)
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");
        properties.setProperty("specific.avro.reader", "true");

       KafkaConsumer<String, Visitor> visitorConsumer = new KafkaConsumer<>(properties);
        String visitor_topic = "visitors";
        visitorConsumer.subscribe(Collections.singleton(visitor_topic));

        System.out.println("Waiting for data...");
        try {
        while (true){
            System.out.println("Polling");
            ConsumerRecords<String, Visitor> visitorRecords = visitorConsumer.poll(1000);
            //ConsumerRecords<String, Booking> bookingRecords = bookingConsumer.poll(1000);
            boolean got_data = false;
            for (ConsumerRecord<String, Visitor> visitorRecord : visitorRecords){
                Visitor customer = visitorRecord.value();
                System.out.println(customer);
                //System.out.println(customer.getBookingId());
                visitorMap.put(customer.getBookingId(), customer);
                got_data = true;
            }

            visitorConsumer.commitSync();
            if (got_data)
                break;
        } } finally {
            visitorConsumer.close();
        }

        Properties booking_props = new Properties();
        // normal consumer
        booking_props.setProperty("bootstrap.servers","127.0.0.1:9092");
        booking_props.put("group.id", "visitor-consumer-group-v1");
        booking_props.put("auto.commit.enable", "false");
        booking_props.put("auto.offset.reset", "earliest");

        // avro part (deserializer)
        booking_props.setProperty("key.deserializer", StringDeserializer.class.getName());
        booking_props.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        booking_props.setProperty("schema.registry.url", "http://127.0.0.1:8081");
        booking_props.setProperty("specific.avro.reader", "true");
        KafkaConsumer<String, Booking> bookingConsumer = new KafkaConsumer<>(booking_props);
        String booking_topic = "bookings";
        bookingConsumer.subscribe(Collections.singleton(booking_topic));
        try {
        while (true){
            System.out.println("Polling");
            //ConsumerRecords<String, Visitor> visitorRecords = visitorConsumer.poll(1000);
            ConsumerRecords<String, Booking> bookingRecords = bookingConsumer.poll(1000);
            boolean got_data = false;

            for (ConsumerRecord<String, Booking> bookingRecord : bookingRecords){
                Booking booking = bookingRecord.value();
                System.out.println(booking);
                bookingMap.put(booking.getBookingId(), booking);
                got_data = true;
            }
            bookingConsumer.commitSync();
            if (got_data) {
                break;
            }
        } } finally {
            bookingConsumer.close();
        }
        aggregate(visitorMap, bookingMap);
    }

    public static void aggregate(Map<String, Visitor> visitorMap, Map<String, Booking> bookingMap) {

        for (String key : visitorMap.keySet()) {

            Visitor visitor = (Visitor) visitorMap.get(key);
            System.out.println("Visitor Object - " + key + ":" + visitorMap.get(key));

            if(null != bookingMap.get(key)) {
                Booking booking = (Booking) bookingMap.get(key);
                System.out.println("Visitor " + visitor.getFirstName() + " has confirmed booking & payment type " + booking.getPaymentType());

            }
        }
    }
}
