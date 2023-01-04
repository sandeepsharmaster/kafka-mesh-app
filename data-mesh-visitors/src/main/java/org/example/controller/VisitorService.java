package org.example.controller;

import org.example.controller.Visitor;

public interface VisitorService {


    public Visitor createVisitor(Visitor visitor);
    public Visitor getVisitorDetails(String bookingId);
}
