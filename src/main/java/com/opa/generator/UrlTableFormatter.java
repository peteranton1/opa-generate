package com.opa.generator;

import java.util.List;

public class UrlTableFormatter extends UrlBaseFormatter {

    public String formatTable(List<UrlSource> urlSources) {
        StringBuilder buf = new StringBuilder();
        buf.append("||HTTP Request Method||Endpoint||Description||\n");
        urlSources.forEach(urlSource ->
            buf.append(formatLineForTable(urlSource))
        );
        return buf.toString();
    }

    public String formatLineForTable(UrlSource urlSource) {
        return "|" +
                urlSource.getMethod() +
                "|" +
                constructUrlForTable(urlSource, SLASH) +
                "|" +
                constructDescription(urlSource) +
                "|\n";
    }

    private String constructDescription(UrlSource urlSource) {
        return urlSource.getMethod() + constructDescForTable(urlSource, SPACE);
    }

    protected String constructDescForTable(UrlSource urlSource, String separator) {
        return urlSource.getPathParts().stream()
                .filter(part -> !part.isVar())
                .filter(part -> !part.getPart().equals("my-service"))
                .filter(part -> !part.getPart().equals("v1"))
                .map(UrlSource.SubPath::getPart)
                .reduce("", (str1, str2)
                        -> str1 + separator + str2);
    }
}
