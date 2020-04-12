package com.opa.generator;

public abstract class UrlBaseFormatter {

    public static final String SLASH = "/";
    public static final String SPACE = " ";
    public static final String INDENT = "\t";

    protected String constructUrlForTable(UrlSource urlSource, String separator) {
        return urlSource.getPathParts().stream()
                .map(part -> {
                    if (part.isVar()) {
                        return "(" + part.getPart() + ")";
                    } else {
                        return part.getPart();
                    }
                }).reduce("", (str1, str2)
                        -> str1 + separator + str2);
    }

}
