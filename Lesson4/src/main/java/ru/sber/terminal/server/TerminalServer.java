package ru.sber.terminal.server;


import ru.sber.terminal.exception.TerminalException;

public class TerminalServer {
    private int balance = 5000; // Example balance

    public void withdraw(int amount) throws TerminalException {
        if (amount > balance) {
            throw new TerminalException("Недостаточно средств. Текущий баланс: " + balance);
        }
        balance -= amount;
        System.out.println("Снятие прошло успешно. Новый баланс: " + balance);
    }

    public void deposit(int amount) {
        balance += amount;
        System.out.println("Перевод успешен. Новый баланс: " + balance);
    }

    public int getBalance() {
        return balance;
    }
}

