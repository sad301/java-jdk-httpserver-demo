package com.example;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

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
}
