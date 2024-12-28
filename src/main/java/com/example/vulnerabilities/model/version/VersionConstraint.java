package com.example.vulnerabilities.model.version;

public class VersionConstraint {
    private final String operator;
    private final String version;

    private VersionConstraint(String operator, String version) {
        this.operator = operator;
        this.version = version;
    }

    public static VersionConstraint parse(String constraint) {
        String[] tokens = constraint.split(" ", 2);
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Invalid version constraint: " + constraint);
        }
        // todo when start handling exceptions handle this too
        return new VersionConstraint(tokens[0], tokens[1]);
    }

    public boolean isSatisfiedBy(String otherVersion) {
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

