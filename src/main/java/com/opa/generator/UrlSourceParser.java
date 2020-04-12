package com.opa.generator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class UrlSourceParser {

    public List<UrlSource> parseFile(String path) throws IOException {
        List<String> lines = ResourceHelper.getFileContentAsStrings(path);
        List<LineRecord> records = linesToRecords(lines);
        return records.stream()
                .map(record -> parseInternal(cleanInput(record))
                ).collect(toList());
    }

    public List<LineRecord> linesToRecords(List<String> lines) {
        List<LineRecord> records = new ArrayList<>();
        for (int i = 0; i < lines.size(); i = i + 2) {
            if (i < lines.size()-1 &&
                    lines.get(i).charAt(0) == '{' &&
                            lines.get(i + 1).charAt(0) != '{'
            ) {
                records.add(new LineRecord(
                        lines.get(i)
                        , lines.get(i + 1))
                );
            } else {
                System.out.println(format("Ignoring line %s: %s",
                        i, lines.get(i)));
            }
        }
        System.out.println(format("Total lines %s, total records %s",
                lines.size(), records.size()));
        return records;
    }

    /*
    {PUT /v1/some/{subscriptionKey}}
     */
    private UrlSource parseInternal(LineRecord record) {
        String[] parts = record.line0.split(" ");
        String[] uriparts = parts[1].split("/");
        String[] roles = record.line1.split(",");

        UrlSource build = UrlSource.builder()
                .method(parts[0])
                .build();
        Arrays.asList(uriparts).forEach(s -> {
            if (s.length() > 0) {
                boolean var = isVar(s);
                if (var) {
                    build.addPart(s.substring(1, s.length() - 1), var);
                } else {
                    build.addPart(s, var);
                }
            }
        });
        Arrays.asList(roles).forEach(s -> {
            if (s.trim().length() > 0) {
                build.addRole(s.trim());
            }
        });
        return build;
    }

    private boolean isVar(String s) {
        return s.indexOf('{') == 0;
    }

    public LineRecord cleanInput(LineRecord record) {
        if (Objects.nonNull(record) &&
                Objects.nonNull(record.line0) &&
                Objects.nonNull(record.line1) &&
                record.line0.trim().length() > 2 &&
                record.line0.trim().charAt(0) == '{' &&
                record.line0.trim().charAt(record.line0.trim().length() - 1) == '}' &&
                record.line0.trim().indexOf(' ') > 0 &&
                record.line0.trim().indexOf('/') > 0
        ) {
            record.line0 = record.line0.trim()
                    .replace(":", "")
                    .replace(".", "")
                    .replace("+", "");
            record.line0 = record.line0.substring(1, record.line0.length() - 1);
            record.line1 = record.line1.trim();
            return record;
        }
        throw new OpaGeneratorException("Unknown Error cleaning String: '" + record.line0 + "'");
    }

    @Data
    @AllArgsConstructor
    public static class LineRecord {
        String line0;
        String line1;
    }
}
