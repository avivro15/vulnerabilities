package com.example.vulnerabilities.model.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Comparable version used to compare each part of version independently
 */
public class SemanticVersion implements Comparable<SemanticVersion> {

    private static final Pattern VERSION_PATTERN =
            Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(?:-(.+))?");

    private final int major;
    private final int minor;
    private final int patch;
    private final String preRelease;


    public SemanticVersion(int major, int minor, int patch, String preRelease) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.preRelease = preRelease;
    }

    public static SemanticVersion parse(String version) {
        Matcher matcher = VERSION_PATTERN.matcher(version);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid semantic version: " + version);
        }
        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int patch = Integer.parseInt(matcher.group(3));
        String preRelease = matcher.group(4);

        return new SemanticVersion(major, minor, patch, preRelease);
    }

    public static int compare(String v1, String v2) {
        SemanticVersion sv1 = parse(v1);
        SemanticVersion sv2 = parse(v2);
        return sv1.compareTo(sv2);
    }

    @Override
    public int compareTo(SemanticVersion other) {
        // Compare major, minor and patch
        if (major != other.major) return Integer.compare(major, other.major);
        if (minor != other.minor) return Integer.compare(minor, other.minor);
        if (patch != other.patch) return Integer.compare(patch, other.patch);

        // Both have the same m.m.p and no pre release tag - return equal
        if (preRelease == null && other.preRelease == null) return 0;
        // Non-pre-release is considered newer
        if (preRelease == null) return 1;
        if (other.preRelease == null) return -1;

        // if both versions have preRelease tag compare both strings lexicographically
        return preRelease.compareTo(other.preRelease);
    }

}
