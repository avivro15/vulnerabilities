package com.example.vulnerabilities.model.version;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Version constraint helper,
 * Helps to validate version constraint and current version
 */
public class VersionConstraintHelper {

    // Regex to capture the operator and version, including pre-release tags
    private static final String VERSION_PATTERN = "(<=|>=|<|>)(\\s?\\d+(?:\\.\\d+)*(?:-\\w+)?)";

    /**
     * Checks if version is satisfying a range
     * Version should satisfy all constraints
     * if version range is not processable TRUE will be returned
     * @param version version to check
     * @param vulnerableVersionRange range for version "< 3.0.0" || "<= 4.2.3"
     * @return is version satisfying all constraints in range
     */
    public static boolean isVersionInRange(String version, String vulnerableVersionRange) {
        try {
            for (VersionConstraint constraint : parse(vulnerableVersionRange)) {
                if (!constraint.isSatisfiedBy(version)) {
                    return false;
                }
            }

            return true;
        }
        catch (IllegalArgumentException exception) {
            // In order to be on the safe side we will assume that every unprocessable package
            // is within our current package constraints
            System.out.println(exception.getMessage());

            return true;
        }
    }

    /**
     * Parse range to list of version constraints
     * @param vulnerableVersionRange string representing range "<= 3, > 4" f.e
     * @return list of constraints
     */
    private static List<VersionConstraint> parse(String vulnerableVersionRange) throws IllegalArgumentException {
        List<VersionConstraint> versionConstraints = new ArrayList<>();

        // Create a matcher for the version range string
        Pattern pattern = Pattern.compile(VERSION_PATTERN);
        Matcher matcher = pattern.matcher(vulnerableVersionRange);

        // Find all matches and add them to the list
        while (matcher.find()) {
            String operator = matcher.group(1).trim();
            String version = matcher.group(2).trim();

            if (!operator.isEmpty() && !version.isEmpty()) {
                // Create and add the VersionConstraint object
                versionConstraints.add(new VersionConstraint(operator, version));
            }
        }

        // if no constraints found - throw an error
        if (versionConstraints.isEmpty()) {
            throw new IllegalArgumentException("Invalid version constraint format: " + vulnerableVersionRange);
        }
        else {
            return versionConstraints;
        }
    }
}
