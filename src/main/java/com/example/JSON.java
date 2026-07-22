package com.example;

public class JSON {

  public static JSON.Array array(JSON.Object... objects) {
    JSON.Array array = new JSON.Array();
    for (JSON.Object object : objects) {
      array = array.add(object);
    }
    return array;
  }

  public static JSON.Object object(java.lang.Object... objects) {
    JSON.Object object = new JSON.Object();
    for (int i = 0; i < objects.length; i += 2) {
      String key = (String) objects[i];
      java.lang.Object value = objects[i + 1];
      object = object.add(key, value);
    }
    return object;
  }

  static class Array {

    JSON.Object[] objects = new JSON.Object[0];

    public static JSON.Array init(JSON.Object... objects) {
      JSON.Array array = new JSON.Array();
      for (JSON.Object object : objects) {
        array = array.add(object);
      }
      return array;
    }

    public JSON.Array add(JSON.Object object) {
      objects = addObject(object);
      return this;
    }

    private JSON.Object[] addObject(JSON.Object object) {
      JSON.Object[] temp = new JSON.Object[objects.length + 1];
      System.arraycopy(objects, 0, temp, 0, objects.length);
      temp[objects.length] = object;
      return temp;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("[");
      for (int i = 0; i < objects.length; i++) {
        JSON.Object object = objects[i];
        sb.append(object);
        if (i < objects.length - 1) {
          sb.append(",");
        }
      }
      sb.append("]");
      return sb.toString();
    }
  }

  static class Object {

    JSON.Field[] fields = new JSON.Field[0];

    public static JSON.Object init(String key, java.lang.Object value) {
      JSON.Object object = new JSON.Object();
      return object.add(key, value);
    }

    public JSON.Object add(String key, java.lang.Object value) {
      fields = addField(new Field(key, value));
      return this;
    }

    private JSON.Field[] addField(JSON.Field field) {
      JSON.Field[] temp = new JSON.Field[fields.length + 1];
      System.arraycopy(fields, 0, temp, 0, fields.length);
      temp[fields.length] = field;
      return temp;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("{");
      for (int i = 0; i < fields.length; i++) {
        builder.append(fields[i]);
        if (i != fields.length - 1) {
          builder.append(",");
        }
      }
      builder.append("}");
      return builder.toString();
    }
  }

  static class Field {

    String key;
    java.lang.Object value;

    public Field(String key, java.lang.Object value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String toString() {
      String fmt;
      if (value.getClass().getName().equals("java.lang.String")) {
        fmt = "\"%s\":\"%s\"";
      } else {
        fmt = "\"%s\":%s";
      }
      return String.format(fmt, key, value);
    }
  }
}
