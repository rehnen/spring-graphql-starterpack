package com.marcusrehn.gqlInJava.controller;

import com.example.graphql.model.PersonTO;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PersonController {
    @QueryMapping
    public PersonTO getPerson() {
        return PersonTO.builder().setFirstName("Marcus").setLastName("Rehn").build();
    }
}
