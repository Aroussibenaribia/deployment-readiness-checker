package com.example.checker.engine;

import com.example.checker.model.FactSet;
import com.example.checker.model.Finding;
import com.example.checker.model.Report;
import com.example.checker.model.Severity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The core engine that orchestrates all checks.
 * Auto-discovers all beans implementing the Check interface via Spring's dependency injection.
 * To register a new check, simply implement the Check interface and annotate with @Component.
 */
@Service
public class CheckerEngine {

    private final List<Check> checks;

    public CheckerEngine(List<Check> checks) {
        this.checks = checks;
    }

    public Report run(FactSet facts) {
        List<Finding> findings = checks.stream()
                .flatMap(check -> check.execute(facts).stream())
                .collect(Collectors.toList());

        boolean passed = findings.stream()
                .noneMatch(f -> f.severity() == Severity.BLOCKER);

        return new Report(passed, findings);
    }
}
