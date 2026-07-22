package com.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticFileHandler implements HttpHandler {

    private final String rootDir;

    public StaticFileHandler(String rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String reqPath = exchange.getRequestURI().getPath();
        if (reqPath.endsWith("/")) reqPath += "index.html";
        Path path = Path.of(rootDir, reqPath);
        if (!Files.exists(path)) {
            byte[] content = "404 Not Found".getBytes(StandardCharsets.UTF_8);
            Utilities.of(exchange).sendResponse(404, content, "Content-Type", "text/plain");
            return;
        }
        byte[] content = Files.readAllBytes(path);
        String contentType = Files.probeContentType(path);
        Utilities.of(exchange).sendResponse(404, content, "Content-Type", contentType);
    }
}
