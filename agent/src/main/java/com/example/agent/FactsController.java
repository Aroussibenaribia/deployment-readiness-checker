package com.example.agent;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FactsController {

    @GetMapping(value = "/facts", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource getFacts() {
        return new ClassPathResource("facts.json");
    }
}
