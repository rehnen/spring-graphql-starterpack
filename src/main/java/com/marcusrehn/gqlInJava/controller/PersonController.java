package com.marcusrehn.gqlInJava.controller;

import com.example.graphql.model.PersonTO;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class PersonController {
    @QueryMapping(value = "getPerson")
    public PersonTO getPerson() {
        return PersonTO.builder().setFirstName("Marcus").setLastName("Rehn").build();
    }
}
