package com.opa.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class UrlOpaFormatter extends UrlBaseFormatter {

    public static final String SLASH = "/";

    public String formatRules(List<UrlSource> urls) {
        StringBuilder buf = new StringBuilder();
        buf.append("---\n" +
                "{code:java}\n" +
                "package corebankinga.ledgermanager\n" +
                "import data.helpers.token\n" +
                "\n" +
                "default allow=false\n" +
                "\n");
        urls.stream()
                .map(this::formatRule)
                .forEach(buf::append);
        buf.append("{code}\n");
        return buf.toString();
    }

    public String formatRule(UrlSource urlSource) {
        StringBuilder buf = new StringBuilder();
        buf.append(format("\n# %s %s\n",
                urlSource.getMethod(),
                constructUrlForTable(urlSource, SLASH)));
        buf.append("allow {\n");
        someVariables(buf, urlSource, INDENT);
        addLine(INDENT, buf, format("input.method == \"%s\"", urlSource.getMethod()));
        addLine(INDENT, buf, format("input.path == %s", constructUrlForOpa(urlSource)));
        addLine(INDENT, buf, "token.valid");
        buf.append(addChecks(urlSource, INDENT));
        buf.append("}\n");
        return buf.toString();
    }

    private void someVariables(StringBuilder buf, UrlSource urlSource, String indent) {
        variables(urlSource)
                .forEach(url ->
                        addLine(indent, buf, "some " + url));
    }

    private List<String> variables(UrlSource urlSource) {
        return urlSource.getPathParts().stream()
                .filter(UrlSource.SubPath::isVar)
                .map(UrlSource.SubPath::getPart)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private List<String> constructUrlForOpa(UrlSource urlSource) {
        return urlSource.getPathParts().stream()
                .map(part -> {
                    if (!part.isVar()) {
                        return "\"" + part.getPart() + "\"";
                    } else {
                        return part.getPart();
                    }
                }).collect(toList());
    }

    private void addLine(String indent, StringBuilder buf, String s) {
        buf.append(indent);
        buf.append(s);
        buf.append("\n");
    }

    private String addChecks(UrlSource urlSource, String indent) {
        StringBuilder buf = new StringBuilder();
        List<String> vars = variables(urlSource);
        if (vars.contains("subscriptionKey")) {
            List<String> roles = formatRolesForOutput(urlSource);
            String line = format("has_role_on_subscription(%s, subscriptionKey)",roles);
            addLine(indent, buf, line);
        }
        return buf.toString();
    }

    private List<String> formatRolesForOutput(UrlSource urlSource) {
        List<String> rolesFormatted = new ArrayList<>();
        urlSource.getRoles().forEach(role -> {
            if(role.charAt(0) != '"'){
                rolesFormatted.add("\""+role+"\"");
            }else{
                rolesFormatted.add(role);
            }
        });
        return rolesFormatted;

    }
}
