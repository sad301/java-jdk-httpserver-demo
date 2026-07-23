package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionDAO implements DAO<Transaction> {

    public static final String ID = "TRANSACTION_DAO";
    private final String dir;
    private final String CSV_FILE = "transactions.csv";
    private final Map<String, Transaction> transactions = new HashMap<>();
    private final Map<String, DAO<?>> daoMap;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public TransactionDAO(String dir, Map<String, DAO<?>> daoMap) {
        this.dir = dir;
        this.daoMap = daoMap;
    }

    @Override
    public boolean create(Transaction transaction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Transaction> retrieve() {
        return transactions.values().stream()
                .sorted(Comparator.comparing(Transaction::getDateTime))
                .collect(Collectors.toList());
    }

    @Override
    public boolean update(Transaction transaction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Transaction transaction) {
        throw new UnsupportedOperationException();
    }

    public boolean deposit(Account account, double amount) {
        AccountDAO accountDAO = (AccountDAO) daoMap.get(AccountDAO.ID);
        if (!accountDAO.exists(account)) return false;
        if (amount < 1.0d) return false;
        Account acc = accountDAO.retrieve(account.getId());
        double newBalance = acc.getBalance() + amount;
        acc.setBalance(newBalance);
        String id = "TR%08d".formatted(transactions.size() + 1);
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAccount(acc);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setDeposit(amount);
        transaction.setWithdraw(0);
        transaction.setBalance(newBalance);
        transactions.put(id, transaction);
        return true;
    }

    public boolean withdraw(Account account, double amount) {
        AccountDAO accountDAO = (AccountDAO) daoMap.get(AccountDAO.ID);
        if (!accountDAO.exists(account)) return false;
        Account acc = accountDAO.retrieve(account.getId());
        if (acc.getBalance() < amount) return false;
        double newBalance = acc.getBalance() - amount;
        acc.setBalance(newBalance);
        String id = "TR%08d".formatted(transactions.size() + 1);
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAccount(acc);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setDeposit(0);
        transaction.setWithdraw(amount);
        transaction.setBalance(newBalance);
        transactions.put(id, transaction);
        return true;
    }

    @Override
    public void read() throws IOException {
        Path path = Path.of(dir, CSV_FILE);
        if (!Files.exists(path.getParent())) {
            Files.createDirectory(path.getParent());
            return;
        }
        if (!Files.exists(path)) return;
        AccountDAO accountDAO = (AccountDAO) daoMap.get(AccountDAO.ID);
        for (String line : Files.readAllLines(path)) {
            String[] values = line.split(";");
            Transaction transaction = new Transaction();
            transaction.setId(values[0]);
            transaction.setAccount(accountDAO.retrieve(values[1]));
            transaction.setDateTime(LocalDateTime.parse(values[2], fmt));
            transaction.setDeposit(Double.parseDouble(values[3]));
            transaction.setWithdraw(Double.parseDouble(values[4]));
            transaction.setBalance(Double.parseDouble(values[5]));
            transactions.put(values[0], transaction);
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
        for (Transaction transaction : retrieve()) {
            String line = "%s;%s;%s;%s;%s;%s\n".formatted(
                    transaction.getId(),
                    transaction.getAccount().getId(),
                    transaction.getDateTime().format(fmt),
                    transaction.getDeposit(),
                    transaction.getWithdraw(),
                    transaction.getBalance());
            Files.writeString(path, line, StandardOpenOption.APPEND);
        }
    }
}
