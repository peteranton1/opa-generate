package com.opa.generator;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UrlSourceParserTest {

    private static final String TEST_PATH_OK = "test_file.txt";
    public static final String TEST_LINE_0 = "{PUT /v1/some/{subscriptionKey}/some/{someKey:.+}}";
    public static final String TEST_LINE_1 = "    PrimaryOwner,Administrator";

    private UrlSourceParser underTest = new UrlSourceParser();

    @Test
    public void shouldHandleLinesIntoRecords() {
        List<List<String>> inputListLines = Arrays.asList(
                Arrays.asList(TEST_LINE_0, TEST_LINE_1)
                , Arrays.asList(TEST_LINE_0, TEST_LINE_1, TEST_LINE_0, TEST_LINE_1)
                , Arrays.asList(TEST_LINE_0, TEST_LINE_1, TEST_LINE_0, TEST_LINE_1, "")
        );
        List<List<UrlSourceParser.LineRecord>> expectedRecords = Arrays.asList(
                singletonList(new UrlSourceParser.LineRecord(TEST_LINE_0, TEST_LINE_1))
                , Arrays.asList(
                        new UrlSourceParser.LineRecord(TEST_LINE_0, TEST_LINE_1)
                        , new UrlSourceParser.LineRecord(TEST_LINE_0, TEST_LINE_1)
                )
                , Arrays.asList(
                        new UrlSourceParser.LineRecord(TEST_LINE_0, TEST_LINE_1)
                        , new UrlSourceParser.LineRecord(TEST_LINE_0, TEST_LINE_1)
                )
        );

        for (int i = 0; i < inputListLines.size(); i++) {
            List<UrlSourceParser.LineRecord> expected = expectedRecords.get(i);
            List<UrlSourceParser.LineRecord> actual = underTest.linesToRecords(inputListLines.get(i));
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Test
    public void shouldParseFileReturnValidUrlSource() throws IOException {
        List<UrlSource> expected = singletonList(getExpected());
        List<UrlSource> actual = underTest.parseFile(TEST_PATH_OK);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    private UrlSource getExpected() {
        return UrlSource.builder()
                .method("PUT")
                .build()
                .withPart("v1", false)
                .withPart("some", false)
                .withPart("subscriptionKey", true)
                .withPart("some", false)
                .withPart("someKey", true)
                .withRole("PrimaryOwner")
                .withRole("Administrator")
                ;//{PUT /v1/some/{subscriptionKey/some/{someKey:.+}}
    }

    @Test
    public void shouldParseCleanBadInputNullEmptySpaces() {
        List<String> line0Inputs = Arrays.asList(null, "", "   ", "{PUT/v1/some/{subscriptionKey}}");
        line0Inputs.forEach(line0 ->
                assertThatThrownBy(() ->
                        underTest.cleanInput(new UrlSourceParser.LineRecord(line0, "PrimaryOwner,Administrator"))
                ).isInstanceOf(OpaGeneratorException.class)
                        .hasMessageContaining(
                                format("Unknown Error cleaning String: '%s'", line0))
        );
    }

    @Test
    public void shouldParseCleanGoodInputSpaces() {
        String line0 = "{PUT /v1/some/{subscriptionKey}}";
        String line0cleaned = "PUT /v1/some/{subscriptionKey}";
        UrlSourceParser.LineRecord expected = new UrlSourceParser.LineRecord(line0cleaned, "PrimaryOwner,Administrator");
        List<String> inputs = Arrays.asList(line0, "  " + line0 + " ");
        inputs.forEach(input ->
                {
                    UrlSourceParser.LineRecord actual = underTest.cleanInput(new UrlSourceParser.LineRecord(line0, "PrimaryOwner,Administrator"));
                    assertThat(actual).isEqualTo(expected);
                }
        );
    }

}