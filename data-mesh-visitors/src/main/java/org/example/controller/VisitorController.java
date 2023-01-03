package org.example.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/visitors")
public class VisitorController {

    @GetMapping(path="/visitor", produces = "application/json")
    public Visitor getVisitor()
    {
        System.out.println("** Get Call for booking details");
        Visitor visitor = new Visitor("Sandy", "Sharma", 23, "CC", "Trivago--1", "sandy@gmail.com");
        return visitor;
    }

}
