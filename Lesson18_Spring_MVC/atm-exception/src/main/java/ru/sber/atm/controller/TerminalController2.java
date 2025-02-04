//package ru.sber.atm.controller;
//
//import org.springframework.web.bind.annotation.*;
//import java.time.LocalDateTime;
//import java.util.concurrent.ConcurrentHashMap;
//
//@CrossOrigin(origins = "http://localhost:5173")
//@RestController
//@RequestMapping("/api")
//public class TerminalController2 {
//
//    private static final String PIN_CODE = "1234";
//    private static final ConcurrentHashMap<String, Integer> userBalances = new ConcurrentHashMap<>();
//    private static final ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();
//    private static final ConcurrentHashMap<String, LocalDateTime> lockEndTimes = new ConcurrentHashMap<>();
//
//    @PostMapping("/validate-pin")
//    public String validatePin(@RequestBody PinRequest request) {
//        String pin = request.getPin();
//        System.out.println("Введен PIN: " + pin);
//
//        // Проверяем, заблокирован ли пользователь
//        if (lockEndTimes.containsKey(pin) && LocalDateTime.now().isBefore(lockEndTimes.get(pin))) {
//            return "Заблокировано. Попробуйте позже.";
//        }
//
//        // Если PIN правильный
//        if (PIN_CODE.equals(pin)) {
//            attempts.put(pin, 0);
//            userBalances.putIfAbsent(pin, 5000); // Устанавливаем баланс, если пользователя нет
//            return "Доступ разрешен";
//        } else {
//            // Неверный PIN
//            int currentAttempts = attempts.getOrDefault(pin, 0) + 1;
//            attempts.put(pin, currentAttempts);
//
//            if (currentAttempts >= 3) {
//                lockEndTimes.put(pin, LocalDateTime.now().plusSeconds(10));
//                attempts.put(pin, 0);
//                return "Заблокировано на 10 секунд";
//            }
//            return "Неверный PIN. Осталось попыток: " + (3 - currentAttempts);
//        }
//    }
//
//    @GetMapping("/balance/{pin}")
//    public int getBalance(@PathVariable String pin) {
//        return userBalances.getOrDefault(pin, 0);
//    }
//
//    @PostMapping("/deposit")
//    public String deposit(@RequestBody TransactionRequest request) {
//        String pin = request.getPin();
//        int amount = request.getAmount();
//
//        if (amount % 100 != 0) {
//            return "Сумма должна быть кратна 100.";
//        }
//
//        userBalances.put(pin, userBalances.getOrDefault(pin, 0) + amount);
//        return "Пополнено успешно. Новый баланс: " + userBalances.get(pin);
//    }
//
//    @PostMapping("/withdraw")
//    public String withdraw(@RequestBody TransactionRequest request) {
//        String pin = request.getPin();
//        int amount = request.getAmount();
//
//        if (amount % 100 != 0) {
//            return "Сумма должна быть кратна 100.";
//        }
//        if (amount > userBalances.getOrDefault(pin, 0)) {
//            return "Недостаточно средств. Баланс: " + userBalances.get(pin);
//        }
//
//        userBalances.put(pin, userBalances.get(pin) - amount);
//        return "Снятие успешно. Новый баланс: " + userBalances.get(pin);
//    }
//}
//
//// Класс для передачи PIN-кода
//class PinRequest {
//    private String pin;
//
//    public String getPin() {
//        return pin;
//    }
//
//    public void setPin(String pin) {
//        this.pin = pin;
//    }
//}
//
//// Класс для передачи PIN и суммы
//class TransactionRequest {
//    private String pin;
//    private int amount;
//
//    public String getPin() {
//        return pin;
//    }
//
//    public void setPin(String pin) {
//        this.pin = pin;
//    }
//
//    public int getAmount() {
//        return amount;
//    }
//
//    public void setAmount(int amount) {
//        this.amount = amount;
//    }
//}
