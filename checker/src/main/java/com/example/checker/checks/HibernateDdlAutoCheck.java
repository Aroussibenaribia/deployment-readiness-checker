package com.example.checker.checks;

import com.example.checker.engine.Check;
import com.example.checker.model.FactSet;
import com.example.checker.model.Finding;
import com.example.checker.model.Severity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * CHECK: Hibernate DDL Auto
 *
 * Verifies that hbm2ddl.auto is set to 'validate' or 'none'.
 * Values like 'update', 'create', or 'create-drop' are dangerous in production
 * and can cause data loss or schema corruption.
 *
 * Severity: BLOCKER if set to create/create-drop/update.
 */
@Component
public class HibernateDdlAutoCheck implements Check {

    private static final List<String> DANGEROUS_VALUES = List.of("update", "create", "create-drop");
    private static final List<String> SAFE_VALUES = List.of("validate", "none");

    @Override
    public List<Finding> execute(FactSet facts) {
        List<Finding> findings = new ArrayList<>();

        if (facts.hibernate() == null) {
            findings.add(new Finding(
                    "HibernateDdlAutoCheck",
                    "No Hibernate configuration found in facts.",
                    Severity.INFO
            ));
            return findings;
        }

        String ddlAuto = facts.hibernate().get("hbm2ddl.auto");

        if (ddlAuto == null) {
            findings.add(new Finding(
                    "HibernateDdlAutoCheck",
                    "hbm2ddl.auto is not set. Defaulting to 'none' is acceptable, but consider explicitly setting it to 'validate'.",
                    Severity.INFO
            ));
        } else if (DANGEROUS_VALUES.contains(ddlAuto.toLowerCase())) {
            findings.add(new Finding(
                    "HibernateDdlAutoCheck",
                    "hbm2ddl.auto is set to '" + ddlAuto + "'. This is DANGEROUS in production and can cause data loss. Set it to 'validate' or 'none'.",
                    Severity.BLOCKER
            ));
        } else if (SAFE_VALUES.contains(ddlAuto.toLowerCase())) {
            findings.add(new Finding(
                    "HibernateDdlAutoCheck",
                    "hbm2ddl.auto is set to '" + ddlAuto + "'. ✓",
                    Severity.INFO
            ));
        } else {
            findings.add(new Finding(
                    "HibernateDdlAutoCheck",
                    "hbm2ddl.auto has an unrecognized value: '" + ddlAuto + "'. Expected 'validate' or 'none'.",
                    Severity.WARNING
            ));
        }

        return findings;
    }
}
