package com.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataHandler implements HttpHandler {

    public interface Handler {
        void handle(HttpExchange exchange) throws Exception;
    }

    public Map<String, Map<String, Handler>> handlers = new HashMap<>();

    public void GET(String path, Handler handler) {
        addHandler(path, "GET", handler);
    }

    public void POST(String path, Handler handler) {
        addHandler(path, "POST", handler);
    }

    public DataHandler() {
        GET("/api", exchange -> {

        });
    }

    public void addHandler(String path, String method, Handler handler) {
        if (handlers.containsKey(path)) {
            handlers.get(path).put(method, handler);
        } else {
            Map<String, Handler> map = new HashMap<>();
            map.put(path, handler);
            handlers.put(path, map);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
