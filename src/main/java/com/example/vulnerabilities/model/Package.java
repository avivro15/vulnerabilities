package com.example.vulnerabilities.model;

public class Package {

    private final String name;
    private final String version;

    public Package(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
