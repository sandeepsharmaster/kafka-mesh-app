package org.example.controller;

import org.example.KafkaSenderExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingServiceImpl implements BookingService{

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaSenderExample kafkaSenderExample;
    @Override
    public Booking createBooking(Booking booking) {

        org.example.pojo.Booking newBooking = org.example.pojo.Booking.newBuilder()
                .setBookingId(booking.getBooking_id())
                .setFirstName(booking.getFirst_name())
                .setLastName(booking.getLast_name())
                .setBookingSource(booking.getBooking_source())
                .setBookingStatus(booking.getBooking_status())
                .setPaymentType(booking.getPayment_type())
                .build();

        kafkaSenderExample.sendBookingMessage(newBooking, "bookings");
        return booking;
    }

    @Override
    public Booking getBookingDetails(String bookingId) {
        return null;
    }
}
