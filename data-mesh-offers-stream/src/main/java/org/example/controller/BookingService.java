package org.example.controller;

import org.example.controller.Booking;

public interface BookingService {


    public Booking createBooking(Booking booking);
    public Booking getBookingDetails(String bookingId);
}
