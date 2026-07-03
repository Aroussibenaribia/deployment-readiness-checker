package com.example.checker.service;

import com.example.checker.model.FactSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Fetches the fact set from the remote Agent over HTTP.
 */
@Service
public class AgentClient {

    private final RestTemplate restTemplate;
    private final String agentUrl;

    public AgentClient(@Value("${agent.url:http://localhost:8081}") String agentUrl) {
        this.restTemplate = new RestTemplate();
        this.agentUrl = agentUrl;
    }

    public FactSet fetchFacts() {
        return restTemplate.getForObject(agentUrl + "/facts", FactSet.class);
    }
}
