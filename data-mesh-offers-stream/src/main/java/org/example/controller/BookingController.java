package org.example.controller;

import org.example.controller.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @GetMapping(path="/bookings-message", produces = "application/json")
    public String getBookings()
    {
        return "Hey There!!";
    }

    @PostMapping(
            value = "/booking", consumes = "application/json", produces = "application/json")
    public Booking createBooking(@RequestBody Booking booking) {

        System.out.println("Booking getting created"+booking);
        return bookingService.createBooking(booking);
    }


    @GetMapping(path="/booking", produces = "application/json")
    public Booking getBookingDetails(String bookingId) {

        System.out.println("** Get Call for booking details");
        Booking booking = new Booking("Sandy", "Sharma", "XYZ", "CC", "Trivago--1", "Confirmed");
                  return booking;
    }
}
