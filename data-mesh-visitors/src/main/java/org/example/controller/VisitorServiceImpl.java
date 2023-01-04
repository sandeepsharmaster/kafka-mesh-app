package org.example.controller;

import org.example.KafkaSenderExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VisitorServiceImpl implements VisitorService{

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaSenderExample kafkaSenderExample;
    @Override
    public Visitor createVisitor(Visitor visitor) {
         org.example.pojo.Visitor newBooking = org.example.pojo.Visitor.newBuilder()
                 .setBookingId(visitor.getBooking_id())
                 .setFirstName(visitor.getFirst_name())
                 .setLastName(visitor.getLast_name())
                 .setBookingSource(visitor.getBooking_source())
                 .setAge(visitor.getAge())
                 .setAutomatedEmail(visitor.getAutomated_email())
                 //.setBookingStatus(visitor.getBooking_status())
                 //.setPaymentType(visitor.getPayment_type())
                 .build();

         kafkaSenderExample.sendVisitorMessage(newBooking, "visitors");
         return visitor;
    }

    @Override
    public Visitor getVisitorDetails(String bookingId) {
        return null;
    }
}
