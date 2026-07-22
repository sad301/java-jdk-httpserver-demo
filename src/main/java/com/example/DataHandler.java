package com.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DataHandler implements HttpHandler {

  public interface Handler {
    void handle(HttpExchange exchange) throws Exception;
  }

  public Map<String, Map<String, Handler>> handlers = new HashMap<>();

  public DataHandler() {
    GET("/api", exchange -> {
      byte[] content = JSON.object("message", "API Index, nothing to see here").toString()
          .getBytes(StandardCharsets.UTF_8);
      Utilities.of(exchange).sendResponse(200, content, "Content-Type", "application/json");
    });
    GET("/api/accounts", exchange -> {
      JSON.Array array = buildArrayFromJSON("accounts.csv", "id", "name");
      byte[] content = array.toString().getBytes();
      Utilities.of(exchange).sendResponse(200, content, "Content-Type", "application/json");
    });
  }

  private void GET(String path, Handler handler) {
    addHandler(path, "GET", handler);
  }

  private void POST(String path, Handler handler) {
    addHandler(path, "POST", handler);
  }

  private void addHandler(String path, String method, Handler handler) {
    if (handlers.containsKey(path)) {
      handlers.get(path).put(method, handler);
    } else {
      Map<String, Handler> map = new HashMap<>();
      map.put(method, handler);
      handlers.put(path, map);
    }
  }

  private JSON.Array buildArrayFromJSON(String csv, String... names) throws IOException {
    FileReader fReader = new FileReader(csv);
    BufferedReader bReader = new BufferedReader(fReader);
    JSON.Array array = new JSON.Array();
    for (String line : bReader.readAllLines()) {
      String[] cols = line.split(";");
      JSON.Object object = new JSON.Object();
      for (int i = 0; i < names.length; i++) {
        object.add(names[i], cols[i]);
      }
      array.add(object);
    }
    bReader.close();
    return array;
  }

  private byte[] createMessage(String message, boolean success) {
    JSON.Object object = JSON.object("message", message, "success", success);
    return object.toString().getBytes();
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String path = exchange.getRequestURI().getPath();
    if (!handlers.containsKey(path)) {
      byte[] content = createMessage("404 Not Found", false);
      Utilities.of(exchange).sendResponse(404, content, "Content-Type", "application/json");
      return;
    }
    String method = exchange.getRequestMethod();
    if (!handlers.get(path).containsKey(method)) {
      byte[] content = createMessage("405 Method Not Allowed", false);
      Utilities.of(exchange).sendResponse(405, content, "Content-Type", "application/json");
      return;
    }
    try {
      handlers.get(path).get(method).handle(exchange);
    } catch (Exception exception) {
      StringWriter sw = new StringWriter();
      exception.printStackTrace(new PrintWriter(sw));
      byte[] content = JSON.object(
          "message", "500 Internal Server Error",
          "error", sw.toString(),
          "success", false).toString().getBytes();
      Utilities.of(exchange).sendResponse(500, content, "Content-Type", "application/json");
    }
  }
}
