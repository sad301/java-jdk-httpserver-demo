package com.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class JSONTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void testObject() {
        JSON.Object object = JSON.Object.init("text", "Hello World")
                .add("number", 12345);
        assert object.toString().equals("{\"text\":\"Hello World\",\"number\":12345}");
    }

    @Test
    public void testArray() {
        JSON.Array array = JSON.Array.init(
                JSON.Object.init("text", "Hello World"),
                JSON.Object.init("number", 12345),
                JSON.Object.init("bool", true),
                JSON.Object.init("bool", false)
        );
        System.out.println(array);
    }
}
