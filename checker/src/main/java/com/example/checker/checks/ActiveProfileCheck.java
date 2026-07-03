package com.example.checker.checks;

import com.example.checker.engine.Check;
import com.example.checker.model.FactSet;
import com.example.checker.model.Finding;
import com.example.checker.model.Severity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * CHECK: Active Spring Profile
 *
 * Ensures that a "prod" profile is active and that dev-only profiles
 * (e.g. "dev", "local", "db-local") are NOT active in production.
 *
 * Severity: BLOCKER if no prod profile, WARNING if dev profiles are still active.
 */
@Component
public class ActiveProfileCheck implements Check {

    private static final List<String> DEV_PROFILES = List.of("dev", "local", "db-local", "test");
    private static final String PROD_PROFILE = "prod";

    @Override
    public List<Finding> execute(FactSet facts) {
        List<Finding> findings = new ArrayList<>();

        if (facts.springProfiles() == null || facts.springProfiles().get("active") == null) {
            findings.add(new Finding(
                    "ActiveProfileCheck",
                    "Could not determine active Spring profiles from facts.",
                    Severity.WARNING
            ));
            return findings;
        }

        List<String> active = facts.springProfiles().get("active");

        if (!active.contains(PROD_PROFILE)) {
            findings.add(new Finding(
                    "ActiveProfileCheck",
                    "The 'prod' profile is NOT active. Active profiles: " + active + ". A production deployment must include the 'prod' profile.",
                    Severity.BLOCKER
            ));
        } else {
            findings.add(new Finding(
                    "ActiveProfileCheck",
                    "The 'prod' profile is active. ✓",
                    Severity.INFO
            ));
        }

        for (String devProfile : DEV_PROFILES) {
            if (active.contains(devProfile)) {
                findings.add(new Finding(
                        "ActiveProfileCheck",
                        "Dev-only profile '" + devProfile + "' is still active. Remove it before deploying to production.",
                        Severity.WARNING
                ));
            }
        }

        return findings;
    }
}
