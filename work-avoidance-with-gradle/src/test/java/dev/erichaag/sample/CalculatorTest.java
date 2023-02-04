package dev.erichaag.sample;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test
    void testAdd() throws IOException {
        final var snippetsDirectory = System.getProperty("dev.erichaag.hugo.shortcodesDirectory");
        final var snippetsFile = new File(snippetsDirectory, "foo.md");
        Files.writeString(snippetsFile.toPath(), "Hello world!");

        Calculator calculator = new Calculator();
        int sum = calculator.add(7, 3);
        assertEquals(10, sum);
    }
}
