package com.example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class AccountReflectionTest {

    private final Class<?> type;
    private Map<String, Field> fields;
    private Map<String, Method> methods;

    public AccountReflectionTest() {
        this.type = Account.class;
    }

    @Test
    public void testAccount() throws Exception {
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("Name: " + field.getName());
            System.out.println("Type: " + field.getType().getName());
        }
        assert true;
    }
}
