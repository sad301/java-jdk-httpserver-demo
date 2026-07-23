package com.example;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Utilities {

    private final HttpExchange exchange;

    public static Utilities of(HttpExchange exchange) {
        return new Utilities(exchange);
    }

    public Utilities(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public void sendResponse(int rCode, byte[] content, String... headers) throws IOException {
        if (headers.length % 2 != 0) throw new IllegalArgumentException("headers must be even");
        for (int i = 0; i < headers.length; i += 2) {
            exchange.getResponseHeaders().add(headers[i], headers[i + 1]);
        }
        exchange.sendResponseHeaders(rCode, content.length);
        OutputStream os = exchange.getResponseBody();
        os.write(content);
        os.close();
    }

    public Map<String, String> getRequestBody() throws IOException {
        Map<String, String> temp = new HashMap<>();
        String body = new String(exchange.getRequestBody().readAllBytes());
        for (String field : body.split("&")) {
            String[] pair = field.split("=");
            String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
            temp.put(key, value);
        }
        return temp;
    }

    public Map<String, String> getRequestQuery() {
        Map<String, String> temp = new HashMap<>();
        String query = exchange.getRequestURI().getQuery();
        for (String field : query.split("&")) {
            String[] pair = field.split("=");
            String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
            temp.put(key, value);
        }
        return temp;
    }
}
