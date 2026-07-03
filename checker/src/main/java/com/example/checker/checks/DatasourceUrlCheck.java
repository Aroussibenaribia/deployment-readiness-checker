package com.example.checker.checks;

import com.example.checker.engine.Check;
import com.example.checker.model.FactSet;
import com.example.checker.model.Finding;
import com.example.checker.model.Severity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * CHECK: Datasource URL
 *
 * Detects common dev/test hostname patterns in datasource URLs,
 * such as 'localhost', '127.0.0.1', or URLs explicitly labeled as dev/test.
 *
 * Severity: BLOCKER if a dev URL pattern is detected.
 */
@Component
public class DatasourceUrlCheck implements Check {

    private static final List<Pattern> DEV_URL_PATTERNS = List.of(
            Pattern.compile(".*localhost.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*127\\.0\\.0\\.1.*"),
            Pattern.compile(".*(dev|test|local)db.*", Pattern.CASE_INSENSITIVE),
            Pattern.compile(".*h2:mem.*", Pattern.CASE_INSENSITIVE)
    );

    @Override
    public List<Finding> execute(FactSet facts) {
        List<Finding> findings = new ArrayList<>();

        if (facts.datasources() == null || facts.datasources().isEmpty()) {
            findings.add(new Finding(
                    "DatasourceUrlCheck",
                    "No datasource configuration found in facts.",
                    Severity.WARNING
            ));
            return findings;
        }

        for (Map.Entry<String, Map<String, Object>> entry : facts.datasources().entrySet()) {
            String dsName = entry.getKey();
            Object urlObj = entry.getValue().get("url");
            if (urlObj == null) continue;

            String url = urlObj.toString();
            boolean isDevUrl = DEV_URL_PATTERNS.stream().anyMatch(p -> p.matcher(url).matches());

            if (isDevUrl) {
                findings.add(new Finding(
                        "DatasourceUrlCheck",
                        "Datasource '" + dsName + "' points to a dev/local URL: '" + url + "'. This must be changed to a production database URL.",
                        Severity.BLOCKER
                ));
            } else {
                findings.add(new Finding(
                        "DatasourceUrlCheck",
                        "Datasource '" + dsName + "' URL looks production-appropriate. ✓",
                        Severity.INFO
                ));
            }
        }

        return findings;
    }
}
