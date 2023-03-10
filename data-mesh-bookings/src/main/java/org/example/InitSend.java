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
	
	@Value("${org.example.kafka.topic.bookings}")
	private String topic2;
	
	@EventListener
	void initiateSendingMessage(ApplicationReadyEvent event) throws InterruptedException {

		Thread.sleep(5000);
		LOG.info("---------------------------------");
		Booking visitor = Booking.newBuilder()
				.setBookingId("XYZ")
				.setFirstName("Sandeep")
				.setLastName("Sharma")
				.setBookingSource("Trivago")
				.setBookingStatus("Confirmed")
				.setPaymentType("CC")
				.build();
		kafkaSenderExample.sendBookingMessage(visitor, "bookings");

	}
}
