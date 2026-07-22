package com.example;

public class JSON {

    static class Array {
        JSON.Object[] objects = new JSON.Object[0];

        public static JSON.Array init(JSON.Object... objects) {
            JSON.Array array = new JSON.Array();
            for (JSON.Object object : objects) {
                array.addObject(object);
            }
            return array;
        }

        public JSON.Array addObject(JSON.Object object) {
            JSON.Object[] temp = new JSON.Object[objects.length + 1];
            System.arraycopy(objects, 0, temp, 0, objects.length);
            temp[objects.length] = object;
            return this;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("[");
            for (int i = 0; i < this.objects.length; i++) {
                builder.append(this.objects[i].toString());
                if (i < this.objects.length - 1) {
                    builder.append(",");
                }
            }
            builder.append("]");
            return builder.toString();
        }
    }

    static class Object {
        JSON.Field[] fields = new JSON.Field[0];

        public static JSON.Object init(String key, java.lang.Object value) {
            JSON.Object object = new JSON.Object();
            return object.add(key, value);
        }

        public JSON.Object add(String key, java.lang.Object value) {
            fields = add(new Field(key, value));
            return this;
        }

        private JSON.Field[] add(JSON.Field field) {
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
