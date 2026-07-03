package com.example.checker.engine;

import com.example.checker.model.FactSet;
import com.example.checker.model.Finding;
import java.util.List;

public interface Check {
    List<Finding> execute(FactSet facts);
}
