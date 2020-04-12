package com.opa.generator;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ResourceHelperTest {

    private static final String TEST_FILE_TXT = "test_file.txt";
    private static final String TEST_FILE_OUT_TXT = "test_file_out.txt";
    public static final String EXPECTED = "{PUT /v1/some/{subscriptionKey}/some/{someKey:.+}}" +
            "\n    PrimaryOwner,Administrator";

    @Test
    public void shouldLoadFileAsText() throws IOException {
        String actual = ResourceHelper.getFileContentAsString(TEST_FILE_TXT);
        assertThat(actual).isEqualTo(EXPECTED+"\n");
    }

    @Test
    public void shouldLoadFileAsTextList() throws IOException {
        List<String> expected = Arrays.asList(
                "{PUT /v1/some/{subscriptionKey}/some/{someKey:.+}}",
                "    PrimaryOwner,Administrator");
        List<String> actual = ResourceHelper.getFileContentAsStrings(TEST_FILE_TXT);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldWriteFileAsText() throws IOException {
        ResourceHelper.writeList(TEST_FILE_TXT, TEST_FILE_OUT_TXT, EXPECTED);
    }
}