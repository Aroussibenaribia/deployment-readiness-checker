# Production Readiness Checker

Production Readiness Checker is an internship project that helps verify whether a Java application is ready for production deployment.

The project is composed of two main components:

* **Agent**: Runs on the target machine and exposes application and environment information through a REST API.
* **Checker**: Retrieves the collected information, applies production readiness rules, and generates a report highlighting potential issues.

## Project Structure

```text
production-readiness/
│
├── agent/        # Collects facts from the target environment
├── checker/      # Applies validation rules
├── contract/     # Shared JSON schema and sample data
└── frontend/     # Web interface for displaying reports
```

## Technologies

* Java
* Spring Boot
* Maven
* Angular
* REST API
* JSON

## Current Features

* Agent REST API
* Production readiness checks
* JSON fact exchange
* Modular check engine
* Web dashboard

## Future Improvements

* WildFly integration
* Spring Boot Actuator integration
* HTML report generation
* CI/CD pipeline support
* Additional production validation rules

## Author

Aroussi Benaribia
