package com.opa.generator;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlTableFormatterTest {

    private UrlTableFormatter underTest = new UrlTableFormatter();

    @Test
    public void shouldFormatSingleValidUrlSource() {
        UrlSource input = UrlSource.builder()
                .method("PUT")
                .build()
                .withPart("v1", false)
                .withPart("some", false)
                .withPart("subscriptionKey", true);
        String expected = "|PUT|/v1/some/(subscriptionKey)|PUT some|\n";
        String actual = underTest.formatLineForTable(input);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldFormatListValidUrlSources() {
        List<UrlSource> input = Collections.singletonList(UrlSource.builder()
                .method("PUT")
                .build()
                .withPart("v1", false)
                .withPart("some", false)
                .withPart("subscriptionKey", true));
        String expected = "||HTTP Request Method||Endpoint||Description||\n" +
                "|PUT|/v1/some/(subscriptionKey)|PUT some|\n";
        String actual = underTest.formatTable(input);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

}