package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
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
    private final Map<String, DAO<?>> daoMap;

    public AccountDAO(String dir, Map<String, DAO<?>> daoMap) {
        this.dir = dir;
        this.daoMap = daoMap;
    }

    @Override
    public boolean create(Account account) {
        if (accounts.containsKey(account.getId())) {
            return false;
        }
        accounts.put(account.getId(), account);
        return true;
    }

    public Account retrieve(String id) {
        return accounts.get(id);
    }

    public boolean exists(Account account) {
        return accounts.containsKey(account.getId());
    }

    @Override
    public List<Account> retrieve() {
        return accounts.values().stream().toList();
    }

    @Override
    public boolean update(Account account) {
        if (accounts.containsKey(account.getId())) {
            accounts.get(account.getId()).setName(account.getName());
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Account account) {
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
        if (!Files.exists(path)) return;
        for (String line : Files.readAllLines(path)) {
            String[] values = line.split(";");
            Account account = new Account(values[0], values[1], Double.parseDouble(values[2]));
            accounts.put(account.getId(), account);
        }
    }

    @Override
    public void write() throws IOException {
        Path path = Path.of(dir, CSV_FILE);
        if (Files.exists(path)) {
            String[] s = CSV_FILE.split("\\.");
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss"));
            Files.move(path, Path.of(dir, "%s-%s.%s".formatted(s[0], now, s[1])), StandardCopyOption.REPLACE_EXISTING);
        }
        Files.createFile(path);
        for (Account account : accounts.values()) {
            String line = "%s;%s;%s\n".formatted(account.getId(), account.getName(), account.getBalance());
            Files.writeString(path, line, StandardOpenOption.APPEND);
        }
    }
}
