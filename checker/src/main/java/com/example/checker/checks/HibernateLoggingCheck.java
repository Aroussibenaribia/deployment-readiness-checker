package com.example.checker.checks;

import com.example.checker.engine.Check;
import com.example.checker.model.FactSet;
import com.example.checker.model.Finding;
import com.example.checker.model.Severity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CHECK: SQL & Hibernate Logging
 *
 * Ensures that SQL/Hibernate verbose logging is disabled in production.
 * DEBUG-level Hibernate SQL logging floods logs and degrades performance.
 *
 * Severity: WARNING if show_sql=true, BLOCKER if hibernate.SQL logger is set to DEBUG.
 */
@Component
public class HibernateLoggingCheck implements Check {

    @Override
    public List<Finding> execute(FactSet facts) {
        List<Finding> findings = new ArrayList<>();

        // Check show_sql property
        if (facts.hibernate() != null) {
            String showSql = facts.hibernate().get("show_sql");
            if ("true".equalsIgnoreCase(showSql)) {
                findings.add(new Finding(
                        "HibernateLoggingCheck",
                        "hibernate.show_sql is set to 'true'. This floods logs in production and degrades performance. Set it to 'false'.",
                        Severity.WARNING
                ));
            }
        }

        // Check log level for Hibernate SQL logger
        if (facts.logLevels() != null) {
            Map<String, String> logLevels = facts.logLevels();
            String hibernateSqlLevel = logLevels.get("org.hibernate.SQL");
            if ("DEBUG".equalsIgnoreCase(hibernateSqlLevel) || "TRACE".equalsIgnoreCase(hibernateSqlLevel)) {
                findings.add(new Finding(
                        "HibernateLoggingCheck",
                        "Logger 'org.hibernate.SQL' is set to " + hibernateSqlLevel + ". This exposes all SQL statements in logs. Set to WARN or ERROR for production.",
                        Severity.BLOCKER
                ));
            }
        }

        if (findings.isEmpty()) {
            findings.add(new Finding(
                    "HibernateLoggingCheck",
                    "Hibernate SQL logging is correctly disabled. ✓",
                    Severity.INFO
            ));
        }

        return findings;
    }
}
