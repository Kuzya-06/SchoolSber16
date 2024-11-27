package ru.sber.terminal.gui;

import ru.sber.terminal.exception.AccountLockedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;


public class PinValidator {
    private final String PIN_CODE = "1234";
    private final JFrame frame = new JFrame("Введите PIN");
    private final JTextField textField = new JTextField();
    private final StringBuilder pinBuffer = new StringBuilder();
    private int attempts = 0; // Счётчик неправильных попыток
    private LocalDateTime lockEndTime = null; // Время окончания блокировки
    private final PinCodeCallback callback; // Callback для успешного ввода PIN

    // Конструктор с обратным вызовом
    public PinValidator(PinCodeCallback callback) {
        this.callback = callback;
    }

    public void pinCode() {
        // Настройки текстового поля
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(new Font("Arial", Font.BOLD, 36));

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                e.consume();
                textField.setText("");
            }

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                try {
                    // Проверяем, заблокирован ли аккаунт
                    checkAccountLock();

                    // Проверяем, является ли символ цифрой
                    if (Character.isDigit(c) && pinBuffer.length() < 4) {
                        pinBuffer.append(c);
                        e.consume();
                        textField.setText(pinBuffer.toString());
                        System.out.println("Введено => " + pinBuffer);
                    } else if (!Character.isDigit(c)) {
                        e.consume(); // Игнорируем недопустимый ввод
                        JOptionPane.showMessageDialog(frame, "Внимание: не \"" + Character.toUpperCase(c) + "\" " +
                                "только цифры", "Ввод", JOptionPane.WARNING_MESSAGE);
                        System.err.println("Недопустимый символ игнорируется.");
                    }

                    // Проверка PIN-кода при вводе 4 символов
                    if (pinBuffer.length() == 4) {
                        if (pinBuffer.toString().equals(PIN_CODE)) {

                            JOptionPane.showMessageDialog(frame, "Доступ разрешен", "Доступ", JOptionPane.QUESTION_MESSAGE);
                            System.out.println("Доступ разрешен. " + pinBuffer);
                            pinBuffer.setLength(0); // Очистить буфер
                            textField.setText(""); // Очистить поле
                            frame.dispose();
                            // Вызываем callback после успешного ввода PIN
                            if (callback != null) {
                                callback.onPinValidated();
                            }
                        } else {
                            handleInvalidPin();
                        }
                    }

                } catch (AccountLockedException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "ПИН-Код", JOptionPane.WARNING_MESSAGE);
                    textField.setText(""); // Очистить поле
                    pinBuffer.setLength(0); // Очистить буфер
                }
            }
        });

        frame.add(textField);
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Центрирование окна
        frame.setVisible(true);
    }

    private void handleInvalidPin() {
        attempts++;
        pinBuffer.setLength(0); // Сброс буфера
        textField.setText(""); // Очистить текстовое поле

        if (attempts >= 3) {
            lockEndTime = LocalDateTime.now().plusSeconds(10);
            attempts = 0; // Сброс счётчика попыток
            JOptionPane.showMessageDialog(frame, "Заблокировано на 10 секунд", "ПИН-Код", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "ПИН-код неверный. Попыток осталось: " + (3 - attempts), "ПИН-Код",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkAccountLock() throws AccountLockedException {
        if (lockEndTime != null && LocalDateTime.now().isBefore(lockEndTime)) {
            long secondsLeft = java.time.Duration.between(LocalDateTime.now(), lockEndTime).getSeconds();
            throw new AccountLockedException("Заблокировано. Попробуйте еще раз через " + secondsLeft + " сек.");
        } else if (lockEndTime != null && LocalDateTime.now().isAfter(lockEndTime)) {
            lockEndTime = null; // Снимаем блокировку
        }
    }
}
