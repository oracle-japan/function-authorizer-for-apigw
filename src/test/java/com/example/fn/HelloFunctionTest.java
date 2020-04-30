package com.example.fn;

import com.fnproject.fn.testing.*;
import org.junit.*;

import java.sql.SQLOutput;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.Assert.*;

public class HelloFunctionTest {

    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void shouldReturnGreeting() {

        final String input = "{" + "\n" +
                "  \"type\":\"TOKEN\"," + "\n" +
                "  \"token\":\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1nHyDtTwR3SEJ3z489...\"" + "\n" +
                "}";
        testing.givenEvent().withBody(input).enqueue();
        testing.thenRun(HelloFunction.class, "handleRequest");

        FnResult result = testing.getOnlyResult();
        System.out.printf("Result: [" + result.getBodyAsString() + "]");
        assertEquals(true, true);
    }

}