package com.example.vulnerabilities.model.version;

/**
 * Version constraint
 */
public class VersionConstraint {

    private final String operator;
    private final String version;

    protected VersionConstraint(String operator, String version) {
        this.operator = operator;
        this.version = version;
    }

    protected boolean isSatisfiedBy(String otherVersion) {
        return switch (operator) {
            case "<" -> SemanticVersion.compare(otherVersion, version) < 0;
            case "<=" -> SemanticVersion.compare(otherVersion, version) <= 0;
            case ">" -> SemanticVersion.compare(otherVersion, version) > 0;
            case ">=" -> SemanticVersion.compare(otherVersion, version) >= 0;
            // TODO and this too exception
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }

}

