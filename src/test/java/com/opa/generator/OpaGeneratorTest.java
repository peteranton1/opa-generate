package com.opa.generator;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

public class OpaGeneratorTest {

    private static final String PATH_URLS = "my-service-urls.txt";
    private static final String PATH_OPAS = "my-service-opa.txt";
    private static final String PATH_ENDS = "my-service-endpoints.txt";

    private OpaGenerator underTest = new OpaGenerator();

    @Test
    public void shouldGenerateUrlSourceWithInput() throws Exception {
        final String path_urls = "my-service-urls.txt";
        final String path_opas = "my-service-opa.txt";
        final String path_ends = "my-service-endpoints.txt";
        assertThatCode(() -> {
            underTest.generate(path_urls, path_ends, path_opas);
        }).doesNotThrowAnyException();
    }
}