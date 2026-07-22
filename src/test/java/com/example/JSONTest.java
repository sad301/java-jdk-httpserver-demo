package com.example;

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
    System.out.println(object);
    assert object.toString().equals("{\"text\":\"Hello World\",\"number\":12345}");
  }

  @Test
  public void testArray() {
    JSON.Array array = JSON.Array.init(
        JSON.Object.init("message", "Hello World"),
        JSON.Object.init("number", 123),
        JSON.Object.init("valid", true));
    System.out.println(array);
    assert true;
  }

  @Test
  public void testObject2() {
    JSON.Object object = JSON.object("message", "hello world", "number", 123.456, "success", true);
    System.out.println(object);
    assert true;
  }

  @Test
  public void testArray2() {
    JSON.Array array = JSON.array(
        JSON.object("message", "hello world", "number", 12345, "success", true),
        JSON.object("message", "Nothing to see here", "number", 889.352, "success", false));
    System.out.println(array);
    assert true;
  }
}
