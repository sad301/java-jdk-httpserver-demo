package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountDAO implements DAO<Account> {

    public static final String ID = "ACCOUNT_DAO";
    private final String dir;
    private final String CSV_FILE = "accounts.csv";
    private final Map<String, Account> accounts = new HashMap<>();

    public AccountDAO(String dir) {
        this.dir = dir;
    }

    @Override
    public boolean create(Account account) throws IOException {
        if (accounts.containsKey(account.getId())) {
            return false;
        }
        accounts.put(account.getId(), account);
        return true;
    }

    @Override
    public List<Account> retrieve() throws IOException {
        return accounts.values().stream().toList();
    }

    @Override
    public boolean update(Account account) throws IOException {
        if (accounts.containsKey(account.getId())) {
            accounts.put(account.getId(), account);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Account account) throws IOException {
        if (accounts.containsKey(account.getId())) {
            accounts.remove(account.getId());
            return true;
        }
        return false;
    }

    @Override
    public void read() throws IOException {
        Path path = Path.of(dir, CSV_FILE);
        if (!Files.exists(path.getParent())) {
            Files.createDirectory(path.getParent());
            return;
        }
        if (Files.exists(path)) {
            for (String line : Files.readAllLines(path)) {
                String[] values = line.split(";");
                Account account = new Account(values[0], values[1]);
                accounts.put(account.getId(), account);
            }
        }
    }

    @Override
    public void write() throws IOException {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss"));
        Path path = Path.of(dir, CSV_FILE);
//        String newName = now + "." + CSV_FILE;
//        System.out.println(newName);

        String[] ss = CSV_FILE.split("\\.");
        String newName = "%s-%s.%s".formatted(ss[0], now, ss[1]);

        Path newPath = Path.of(dir, newName);
        if (Files.exists(path)) {
            Files.move(path, newPath, StandardCopyOption.REPLACE_EXISTING);
        }
        Files.createFile(path);
        for (Account account : accounts.values()) {
            String line = "%s;%s".formatted(account.getId(), account.getName());
            Files.writeString(path, line, StandardOpenOption.APPEND);
        }
    }
}
