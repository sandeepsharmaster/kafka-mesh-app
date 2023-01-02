package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import org.example.pojo.Booking;
@Component
class InitSend {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private KafkaSenderExample kafkaSenderExample;
	
	/*@Autowired
	private KafkaSenderWithMessageConverter messageConverterSender;*/
	
	@Value("${org.example.kafka.topic.users}")
	private String topic1;

	@Value("${org.example.kafka.topic.bookings}")
	private String topic2;
	
	@EventListener
	void initiateSendingMessage(ApplicationReadyEvent event) throws InterruptedException {

		Thread.sleep(5000);
		LOG.info("---------------------------------");
		Booking visitor = Booking.newBuilder()
				.setBookingId("XYZ-1")
				.setFirstName("Sandy")
				.setLastName("Sharma")
				.setBookingSource("MakeMyTrip")
				.setBookingStatus("Confirmed")
				.setPaymentType("NetBanking")
				.build();
		kafkaSenderExample.sendBookingMessage(visitor, "bookings");

		Thread.sleep(5000);
		LOG.info("---------------------------------");
		kafkaSenderExample.sendCustomMessage(new User("Lucario"), "users");

	}
}
