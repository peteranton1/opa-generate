package com.opa.generator;

import java.io.IOException;
import java.util.List;

public class OpaGenerator {

    public static void main(String[] args) throws IOException {
        final String path_urls = "my-service-urls.txt";
        final String path_opas = "my-service-opa.txt";
        final String path_ends = "my-service-endpoints.txt";
        OpaGenerator generator = new OpaGenerator();
        generator.generate(path_urls, path_ends, path_opas);
    }

    public void generate(String path_urls, String path_ends, String path_opas) throws IOException {
        UrlSourceParser parser = new UrlSourceParser();
        List<UrlSource> urlSources = parser.parseFile(path_urls);

        UrlTableFormatter tableFormatter = new UrlTableFormatter();
        ResourceHelper.writeList(path_urls, path_ends, tableFormatter.formatTable(urlSources));

        UrlOpaFormatter opaFormatter = new UrlOpaFormatter();
        ResourceHelper.writeList(path_opas, path_opas, opaFormatter.formatRules(urlSources));
    }
}
