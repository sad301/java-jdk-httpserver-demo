package com.example;

import java.io.IOException;
import java.util.List;

public interface DAO<T> {

    boolean create(T t);

    List<T> retrieve();

    boolean update(T t);

    boolean delete(T t);

    void read() throws IOException;

    void write() throws IOException;
}
