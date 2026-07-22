package com.example;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class Server {

  private final InetSocketAddress addr;
  private final String dir;
  private final Map<String, DAO<?>> daoMap;

  private HttpServer server;
  private Map<String, HttpContext> contextMap;

  public Server(InetSocketAddress addr, String dir, Map<String, DAO<?>> daoMap) {
    this.addr = addr;
    this.dir = dir;
    this.daoMap = daoMap;
    contextMap = new HashMap<>();
  }

  public void configure() throws IOException {
    server = HttpServer.create(addr, 0);
    contextMap.put("ui", server.createContext("/", new StaticFileHandler(dir)));
    contextMap.put("api", server.createContext("/api", new DataHandler(daoMap)));
  }

  public void start() {
    if (server == null) {
      throw new NullPointerException("Server has not been initialized");
    }
    server.start();
  }

  public void stop() {
    if (server == null) {
      throw new NullPointerException("Server has not been initialized");
    }
    server.stop(3);
  }

  public Map<String, HttpContext> getContextMap() {
    return contextMap;
  }
}
