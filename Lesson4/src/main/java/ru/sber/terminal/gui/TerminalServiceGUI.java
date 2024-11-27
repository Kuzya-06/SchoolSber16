package ru.sber.terminal.gui;


import ru.sber.terminal.exception.TerminalException;
import ru.sber.terminal.server.TerminalServer;

import javax.swing.*;
import java.awt.*;


public class TerminalServiceGUI {
    private final TerminalServer server;
    private final JFrame frame;
    private final JTextField amountField;
    private final JLabel balanceLabel; // Метка для отображения баланса

    public TerminalServiceGUI(TerminalServer server) {
        this.server = server;
        this.frame = new JFrame("Terminal Service GUI");
        this.amountField = new JTextField();
        this.balanceLabel = new JLabel("Баланс: RUB", SwingConstants.CENTER); // Инициализация баланса
    }

    public void launch() {
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Панель с балансом
        JPanel balancePanel = new JPanel(new BorderLayout());
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balancePanel.add(balanceLabel, BorderLayout.CENTER);

        // Верхняя панель с вводом суммы
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel amountLabel = new JLabel("Сумма (RUB):");
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setFont(new Font("Arial", Font.BOLD, 26));
        inputPanel.add(amountLabel, BorderLayout.WEST);
        inputPanel.add(amountField, BorderLayout.CENTER);

        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton depositButton = new JButton("Положить");
        JButton withdrawButton = new JButton("Снять");
        JButton closeButton = new JButton("Выйти");

        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(closeButton);

        // Обработчики событий
        depositButton.addActionListener(e -> handleTransaction(true));

        withdrawButton.addActionListener(e -> handleTransaction(false));

        closeButton.addActionListener(e -> frame.dispose());

        // Инициализация начального баланса
        updateBalance();
        // Добавляем элементы в окно
        frame.add(balancePanel, BorderLayout.NORTH);
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);


        frame.setLocationRelativeTo(null); // Центрируем окно
        frame.setVisible(true);
    }

    private void handleTransaction(boolean isDeposit) {
        try {
            String input = amountField.getText().trim();

            if (input.isEmpty() || !input.matches("\\d+")) {
                throw new TerminalException("Неверный ввод. Введите корректную сумму.");
            }

            int amount = Integer.parseInt(input);

            if (amount % 100 != 0) {
                throw new TerminalException("Сумма должна быть кратна 100.");
            }

            if (isDeposit) {
                server.deposit(amount);
                updateBalance();
                JOptionPane.showMessageDialog(frame,
                        "Сумма успешно зачислена! Текущий баланс: " + server.getBalance(), "Баланс",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                server.withdraw(amount);
                updateBalance();
                JOptionPane.showMessageDialog(frame, "Сумма успешно снята! Текущий баланс: " + server.getBalance(),
                        "Баланс", JOptionPane.INFORMATION_MESSAGE);
            }

            amountField.setText("");
        } catch (TerminalException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(frame, "Ошибка: " + e.getMessage(), "Ошибка транзакции", JOptionPane.ERROR_MESSAGE);
        } catch(NumberFormatException e){
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(frame, "Ошибка: " + e.getMessage(), "Ошибка размера суммы",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBalance() {
        balanceLabel.setText("Баланс: " + server.getBalance() + " RUB");
    }
}
