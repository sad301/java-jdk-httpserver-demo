package com.example;

import javax.swing.*;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Main extends JFrame {

    private JToggleButton btn;
    private JTextArea taLog;
    private JTextField tfAddress;
    private JTextField tfDir;
    private Server server;
    private Map<String, DAO<?>> daoMap;

    private void configure() {

        JLabel lAddr = new JLabel("Address / Port :");
        tfAddress = new JTextField("127.0.0.1:8080");
        tfAddress.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                btn.setEnabled(!tfAddress.getText().isEmpty());
            }
        });

        JLabel lDir = new JLabel("Public Directory :");
        tfDir = new JTextField("public");
        tfDir.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                btn.setEnabled(!tfDir.getText().isEmpty());
            }
        });

        btn = new JToggleButton("Start");
        btn.addActionListener(_ -> {
            try {
                if (btn.isSelected()) {
                    startServer();
                    btn.setText("Stop");
                } else {
                    stopServer();
                    btn.setText("Start");
                }
            } catch (Exception e) {
                log("[error] %s".formatted(e.getMessage()));
            }
        });

        taLog = new JTextArea();
        JScrollPane sp = new JScrollPane(taLog);

        Container pane = getContentPane();
        GroupLayout layout = new GroupLayout(pane);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(lAddr)
                                .addComponent(tfAddress)
                                .addComponent(lDir)
                                .addComponent(tfDir)
                                .addComponent(btn))
                        .addComponent(sp)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lAddr)
                        .addComponent(tfAddress)
                        .addComponent(lDir)
                        .addComponent(tfDir)
                        .addComponent(btn))
                .addComponent(sp));
        pane.setLayout(layout);

        Dimension dim = new Dimension(640, 480);

        setTitle("Java JDK Server Demo");
        setSize(dim);
        setMinimumSize(dim);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void startServer() throws IOException {
        String addr = tfAddress.getText();
        String dir = tfDir.getText();
        if (addr.isEmpty() || dir.isEmpty())
            return;
        String[] pair = addr.split(":");
        int port = Integer.parseInt(pair[1]);
        InetSocketAddress sockAddr = new InetSocketAddress(pair[0], port);

        daoMap = new HashMap<>();
        daoMap.put(AccountDAO.ID, new AccountDAO("data", daoMap));
        daoMap.put(TransactionDAO.ID, new TransactionDAO("data", daoMap));
        for (DAO<?> dao : daoMap.values()) {
            dao.read();
        }
        server = new Server(sockAddr, dir, daoMap);
        server.configure();
        for (HttpContext ctx : server.getContextMap().values()) {
            ctx.getFilters().add(Filter.afterHandler("session logger", getSessionLogger()));
        }
        server.start();
        log("Server started");
    }

    private void stopServer() throws IOException {
        if (server == null) throw new NullPointerException("Server is null");
        server.stop();
        server = null;
        for (DAO<?> dao : daoMap.values()) {
            dao.write();
        }
        log("Server stopped");
    }

    private Consumer<HttpExchange> getSessionLogger() {
        return e -> log("[%s] %s %s %s".formatted(
                e.getRemoteAddress().getHostString(),
                e.getRequestMethod(),
                e.getRequestURI().toString(),
                e.getResponseCode()));
    }

    public void log(String message) {
        String now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        taLog.append("%s> %s\n".formatted(now, message));
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.configure();
            main.setVisible(true);
        });
    }
}
