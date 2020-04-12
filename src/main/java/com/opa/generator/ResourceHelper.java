package com.opa.generator;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.ClassLoader.getSystemResourceAsStream;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ResourceHelper {

    public static String getFileContentAsString(String fileName) throws IOException {
        try (InputStream in = getSystemResourceAsStream(fileName)) {
            Objects.requireNonNull(in);
            return IOUtils.toString(in, UTF_8);
        }
    }

    public static List<String> getFileContentAsStrings(String fileName) throws IOException {
        try (InputStream in = getSystemResourceAsStream(fileName)) {
            Objects.requireNonNull(in);
            return IOUtils.readLines(in, UTF_8);
        }
    }

    public static void writeList(String inPath, String outPath, String content) throws IOException {
        try (OutputStream out = getSystemResourceAsOutStream(inPath, outPath)) {
            Objects.requireNonNull(out);
            out.write(content.getBytes());
        }
    }

    public static OutputStream getSystemResourceAsOutStream(String inPath, String outPath) throws FileNotFoundException {
        URL urlIn = getSystemResource(inPath);
        String path = urlIn.getPath();
        path = path.substring(0,path.indexOf(inPath));
        String urlOut = new File(path, outPath).getAbsolutePath();
        System.out.println("Writing: "+urlOut);
        return new BufferedOutputStream(new FileOutputStream(urlOut));
    }

}
