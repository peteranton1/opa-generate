package com.opa.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class UrlSource {
    String method;
    List<SubPath> pathParts = new ArrayList<>();
    List<String> roles = new ArrayList<>();

    public void addPart(String part, boolean var) {
        pathParts.add(new SubPath(part, var));
    }

    public UrlSource withPart(String part, boolean var) {
        pathParts.add(new SubPath(part, var));
        return this;
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public UrlSource withRole(String role) {
        roles.add(role);
        return this;
    }

    @Value
    @AllArgsConstructor
    public class SubPath {
        String part;
        boolean var;
    }
}
