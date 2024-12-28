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
     * Version should satisfy all constraints in order to satisfy
     * @param version version to check
     * @param vulnerableVersionRange range for version "< 3.0.0" || "<= 4.2.3"
     * @return is version satisfying all constraints in range
     */
    public static boolean isVersionInRange(String version, String vulnerableVersionRange) {
        for (VersionConstraint constraint : parse(vulnerableVersionRange)) {
            if (!constraint.isSatisfiedBy(version)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Parse range to list of version constraints
     * @param vulnerableVersionRange string representing range "< 3" f.e
     * @return list of constraints
     */
    private static List<VersionConstraint> parse(String vulnerableVersionRange) {
        List<VersionConstraint> versionConstraints = new ArrayList<>();

        // Create a matcher for the version range string
        Pattern pattern = Pattern.compile(VERSION_PATTERN);
        Matcher matcher = pattern.matcher(vulnerableVersionRange);

        // Find all matches and add them to the list
        while (matcher.find()) {
            String operator = matcher.group(1).trim();
            String version = matcher.group(2).trim();

            // Create and add the VersionConstraint object
            versionConstraints.add(new VersionConstraint(operator, version));
        }

        return versionConstraints;
    }

}
