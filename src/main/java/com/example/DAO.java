package com.example;

import java.io.IOException;
import java.util.List;

public interface DAO<T> {

    boolean create(T t) throws IOException;

    List<T> retrieve() throws IOException;

    boolean update(T t) throws IOException;

    boolean delete(T t) throws IOException;

    void read() throws IOException;

    void write() throws IOException;
}
