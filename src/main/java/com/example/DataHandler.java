package com.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DataHandler implements HttpHandler {

    public interface Handler {
        void handle(HttpExchange exchange) throws Exception;
    }

    public Map<String, Map<String, Handler>> handlers = new HashMap<>();
    private final Map<String, DAO<?>> daoMap;

    public DataHandler(Map<String, DAO<?>> daoMap) {
        this.daoMap = daoMap;
        setRoutes();
    }

    private void setRoutes() {
        AccountDAO accountDAO = (AccountDAO) daoMap.get(AccountDAO.ID);
        TransactionDAO transactionDAO = (TransactionDAO) daoMap.get(TransactionDAO.ID);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        GET("/api", exchange -> {
            byte[] content = JSON.object("message", "API Index, nothing to see here").toString().getBytes();
            Utilities.of(exchange).sendResponse(200, content, "Content-Type", "application/json");
        });

        GET("/api/accounts", exchange -> {
            JSON.Array array = new JSON.Array();

            List<Transaction> transactions = transactionDAO.retrieve();

            for (Account account : accountDAO.retrieve()) {

                String id = account.getId();

                double depositTotal = transactions.stream().filter(t -> t.getAccount().getId().equals(id)).mapToDouble(Transaction::getDeposit).sum();

                double withdrawTotal = transactions.stream().filter(t -> t.getAccount().getId().equals(id)).mapToDouble(Transaction::getWithdraw).sum();

                array.add(JSON.object(
                        "id", account.getId(),
                        "name", account.getName(),
                        "transaction", JSON.object(
                                "deposit", depositTotal,
                                "withdraw", withdrawTotal
                        ),
                        "balance", account.getBalance()
                ));
            }
            byte[] content = array.toString().getBytes();
            Utilities.of(exchange).sendResponse(200, content, "Content-Type", "application/json");
        });

        GET("/api/transactions", exchange -> {
            JSON.Array array = new JSON.Array();
            List<Transaction> transactions = transactionDAO.retrieve();
            if (transactions.size() > 0) {
                for (Transaction transaction : transactions) {
                    array.add(JSON.object(
                            "id", transaction.getId(),
                            "account", JSON.object(
                                    "id", transaction.getAccount().getId(),
                                    "name", transaction.getAccount().getName(),
                                    "balance", transaction.getAccount().getBalance()
                            ),
                            "dateTime", transaction.getDateTime().format(fmt),
                            "deposit", transaction.getDeposit(),
                            "withdraw", transaction.getWithdraw(),
                            "balance", transaction.getBalance()
                    ));
                }
            }
            byte[] content = array.toString().getBytes();
            Utilities.of(exchange).sendResponse(200, content, "Content-Type", "application/json");
        });

        POST("/api/accounts", exchange -> {
            Map<String, String> body = Utilities.of(exchange).getRequestBody();
            if (!body.containsKey("id") || !body.containsKey("name")) {
                byte[] content = JSON.object("message", "Invalid body").toString().getBytes();
                Utilities.of(exchange).sendResponse(400, content, "Content-Type", "application/json");
                return;
            }
            if (accountDAO.create(new Account(body.get("id"), body.get("name"), 0))) {
                byte[] content = JSON.object("message", "Account created successfully", "success", true).toString().getBytes();
                Utilities.of(exchange).sendResponse(201, content, "Content-Type", "application/json");
            } else {
                byte[] content = JSON.object("message", "Account creation failed", "success", false).toString().getBytes();
                Utilities.of(exchange).sendResponse(400, content, "Content-Type", "application/json");
            }
        });

        POST("/api/transactions/deposit", exchange -> {
            Map<String, String> body = Utilities.of(exchange).getRequestBody();
            if (!body.containsKey("id") || !body.containsKey("amount")) {
                byte[] content = JSON.object("message", "Required value doesn't exists").toString().getBytes();
                Utilities.of(exchange).sendResponse(400, content, "Content-Type", "application/json");
                return;
            }
            Account account = new Account(body.get("id"));
            double amount = Double.parseDouble(body.get("amount"));
            if (transactionDAO.deposit(account, amount)) {
                byte[] content = JSON.object("message", "Amount deposited successfully", "success", true).toString().getBytes();
                Utilities.of(exchange).sendResponse(200, content, "Content-Type", "application/json");
            } else {
                byte[] content = JSON.object("message", "Deposit failed", "success", true).toString().getBytes();
                Utilities.of(exchange).sendResponse(400, content, "Content-Type", "application/json");
            }
        });

        POST("/api/transactions/withdraw", exchange -> {
            Map<String, String> body = Utilities.of(exchange).getRequestBody();
            if (!body.containsKey("id") || !body.containsKey("amount")) {
                byte[] content = JSON.object("message", "Required value doesn't exists").toString().getBytes();
                Utilities.of(exchange).sendResponse(400, content, "Content-Type", "application/json");
                return;
            }
            Account account = new Account(body.get("id"));
            double amount = Double.parseDouble(body.get("amount"));
            if (transactionDAO.withdraw(account, amount)) {
                byte[] content = JSON.object("message", "Amount withdrawn successfully", "success", true).toString().getBytes();
                Utilities.of(exchange).sendResponse(200, content, "Content-Type", "application/json");
            } else {
                byte[] content = JSON.object("message", "Withdraw failed", "success", true).toString().getBytes();
                Utilities.of(exchange).sendResponse(400, content, "Content-Type", "application/json");
            }
        });

        PUT("/api/accounts", exchange -> {
            Utilities util = new Utilities(exchange);
            Map<String, String> query = util.getRequestQuery();
            if (!query.containsKey("id")) {
                byte[] content = JSON.object("message", "Required query parameter doesn't exists", "success", false).toString().getBytes();
                Utilities.of(exchange).sendResponse(400, content, "Content-Type", "application/json");
                return;
            }
            Map<String, String> body = util.getRequestBody();
            if (!body.containsKey("name")) {
                byte[] content = JSON.object("message", "Required body field doesn't exists", "success", false).toString().getBytes();
                Utilities.of(exchange).sendResponse(400, content, "Content-Type", "application/json");
                return;
            }
            if (accountDAO.update(new Account(query.get("id"), body.get("name")))) {
                byte[] content = JSON.object("message", "Account updated successfully", "success", true).toString().getBytes();
                Utilities.of(exchange).sendResponse(200, content, "Content-Type", "application/json");
            } else {
                byte[] content = JSON.object("message", "Account update failed", "success", false).toString().getBytes();
                Utilities.of(exchange).sendResponse(400, content, "Content-Type", "application/json");
            }
        });

        DELETE("/api/accounts", exchange -> {
            Map<String, String> query = Utilities.of(exchange).getRequestQuery();
            if (!query.containsKey("id")) {
                byte[] content = JSON.object("message", "Required query parameter doesn't exists", "success", false).toString().getBytes();
                Utilities.of(exchange).sendResponse(400, content, "Content-Type", "application/json");
                return;
            }
            Account account = new Account(query.get("id"));
            if (accountDAO.delete(account)) {
                byte[] content = JSON.object("message", "Account deleted successfully", "success", true).toString().getBytes();
                Utilities.of(exchange).sendResponse(200, content, "Content-Type", "application/json");
            } else {
                byte[] content = JSON.object("message", "Account delete failed", "success", false).toString().getBytes();
                Utilities.of(exchange).sendResponse(400, content, "Content-Type", "application/json");
            }
        });
    }

    private void GET(String path, Handler handler) {
        addHandler(path, "GET", handler);
    }

    private void POST(String path, Handler handler) {
        addHandler(path, "POST", handler);
    }

    private void PUT(String path, Handler handler) {
        addHandler(path, "PUT", handler);
    }

    private void DELETE(String path, Handler handler) {
        addHandler(path, "DELETE", handler);
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
