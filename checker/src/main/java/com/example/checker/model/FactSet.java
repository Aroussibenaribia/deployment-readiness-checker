package com.example.checker.model;

import java.util.Map;
import java.util.List;

public record FactSet(
    Map<String, String> systemProperties,
    Map<String, List<String>> springProfiles,
    Map<String, String> logLevels,
    Map<String, Map<String, Object>> datasources,
    Map<String, String> hibernate
) {}
