package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public abstract class DAO<T> {

    private final String fileName;
    private final Class<?> type;

    private List<T> data;
    
    public DAO(String fileName, Class<?> type, String... fields) {
        this.fileName = fileName;
        this.type = type;
    }

    public abstract boolean create(T t) throws IOException;
    public abstract List<T> retrieve() throws IOException;
    public abstract boolean update(T t) throws IOException;
    public abstract boolean delete(T t) throws IOException;

    public void read() throws Exception {
        File f = new File(fileName);
        if (!f.exists()) return;
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
    }
}
