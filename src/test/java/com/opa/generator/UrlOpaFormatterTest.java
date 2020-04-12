package com.opa.generator;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlOpaFormatterTest {

    private UrlOpaFormatter underTest = new UrlOpaFormatter();

    @Test
    public void shouldFormatSingleValidUrlSource() {
        UrlSource input = UrlSource.builder()
                .method("PUT")
                .build()
                .withPart("v1", false)
                .withPart("some", false)
                .withPart("subscriptionKey", true)
                .withRole("PrimaryOwner")
                .withRole("Administrator")
                ;

        String expected = "\n" +
                "# PUT /v1/some/(subscriptionKey)\n" +
                "allow {\n" +
                "\tsome subscriptionKey\n" +
                "\tinput.method == \"PUT\"\n" +
                "\tinput.path == [\"v1\", \"some\", subscriptionKey]\n" +
                "\ttoken.valid\n" +
                "\thas_role_on_subscription([\"PrimaryOwner\", \"Administrator\"], subscriptionKey)\n" +
                "}\n";
        String actual = underTest.formatRule(input);

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
                .withPart("subscriptionKey", true)
                .withRole("PrimaryOwner")
                .withRole("Administrator"))
                ;
        String expected = "---\n" +
                "{code:java}\n" +
                "package corebankinga.ledgermanager\n" +
                "import data.helpers.token\n" +
                "\n" +
                "default allow=false\n" +
                "\n" +
                "\n" +
                "# PUT /v1/some/(subscriptionKey)\n" +
                "allow {\n" +
                "\tsome subscriptionKey\n" +
                "\tinput.method == \"PUT\"\n" +
                "\tinput.path == [\"v1\", \"some\", subscriptionKey]\n" +
                "\ttoken.valid\n" +
                "\thas_role_on_subscription([\"PrimaryOwner\", \"Administrator\"], subscriptionKey)\n" +
                "}\n" +
                "{code}\n";
        String actual = underTest.formatRules(input);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }


}