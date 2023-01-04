package org.example.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/visitors")
public class VisitorController {

    @Autowired
    VisitorService visitorService;

    @PostMapping(
            value = "/visitor", consumes = "application/json", produces = "application/json")
    public Visitor createBooking(@RequestBody Visitor visitor) {

        System.out.println("Visitor  created : "+visitor);
        return visitorService.createVisitor(visitor);
    }

    @GetMapping(path="/visitor", produces = "application/json")
    public Visitor getVisitor()
    {
        System.out.println("** Get Call for booking details");
        Visitor visitor = new Visitor("Sandy", "Sharma", 23, "CC", "Trivago--1", true);
        return visitor;
    }

}
