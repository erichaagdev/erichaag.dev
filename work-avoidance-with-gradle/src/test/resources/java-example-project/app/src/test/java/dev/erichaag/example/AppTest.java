package dev.erichaag.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void appHasAGreeting() {
        App classUnderTest = new App();
        Assertions.assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }
}
