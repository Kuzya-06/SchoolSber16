package ru.sber.terminal;

import ru.sber.terminal.gui.PinValidator;
import ru.sber.terminal.gui.TerminalServiceGUI;
import ru.sber.terminal.server.TerminalServer;

public class Main {
    public static void main(String[] args) {
        TerminalServer server = new TerminalServer();
        TerminalServiceGUI serviceGUI = new TerminalServiceGUI(server);
        PinValidator pinValidator = new PinValidator(serviceGUI::launch);

        pinValidator.pinCode();
    }
}