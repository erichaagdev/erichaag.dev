package dev.erichaag.sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test
    void testAdd() {
        Calculator calculator = new Calculator();
        int sum = calculator.add(7, 3);
        assertEquals(10, sum);
    }
}
