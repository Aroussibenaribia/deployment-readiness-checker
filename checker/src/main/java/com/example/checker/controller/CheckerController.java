package com.example.checker.controller;

import com.example.checker.engine.CheckerEngine;
import com.example.checker.model.FactSet;
import com.example.checker.model.Report;
import com.example.checker.service.AgentClient;
import org.springframework.web.bind.annotation.*;

/**
 * REST API exposing the checker's results to the Angular frontend.
 * GET /api/report — fetches facts from the Agent and runs all checks.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CheckerController {

    private final AgentClient agentClient;
    private final CheckerEngine engine;

    public CheckerController(AgentClient agentClient, CheckerEngine engine) {
        this.agentClient = agentClient;
        this.engine = engine;
    }

    @GetMapping("/report")
    public Report getReport() {
        FactSet facts = agentClient.fetchFacts();
        return engine.run(facts);
    }

    @GetMapping("/health")
    public String health() {
        return "Checker is running";
    }
}
