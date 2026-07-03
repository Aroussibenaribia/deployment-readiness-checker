package com.example.checker.model;

import java.util.List;

public record Report(boolean passed, List<Finding> findings) {}
