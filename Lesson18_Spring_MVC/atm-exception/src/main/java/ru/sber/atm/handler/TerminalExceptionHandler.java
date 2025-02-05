package ru.sber.atm.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sber.atm.exception.AccountLockedException;
import ru.sber.atm.exception.TerminalException;

@RestControllerAdvice
public class TerminalExceptionHandler {

    @ExceptionHandler({TerminalException.class, AccountLockedException.class})
    public String getTerminalException(final RuntimeException e) {
        return e.getMessage();
    }

//    @ExceptionHandler(AccountLockedException.class)
//    public String getAccountLockedException(final AccountLockedException e) {
//        return e.getMessage();
//    }

}
