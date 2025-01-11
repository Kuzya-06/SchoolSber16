package ru.sbt;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        final var scanner = new Scanner(System.in);

        System.out.println("Введите путь до директории с плагином. Например: file:///D:\\plugins или просто нажмите " +
                "ВВОД");

        final var defaultDirectory = "file:///D:\\SchoolJavaSber\\project\\SchoolSber16\\Lesson7\\pluginManager\\plugins";

        final var inputDirectory = scanner.nextLine();

        if(Objects.equals(inputDirectory, "")){
            final var pluginManager = new PluginManager(defaultDirectory);

            final var basicPlugin = pluginManager.load("BasicPlugin/target/BasicPlugin-1.0.jar", "ru.sbt.BasicPlugin");
            basicPlugin.doUsefull();

            final var hugePlugin = pluginManager.load("HugePlugin/target/HugePlugin-1.0.jar", "ru.sbt.HugePlugin");
            hugePlugin.doUsefull();

            final var myPlugin = pluginManager.load("MyPlugin/target/MyPlugin-1.0.jar", "ru.sbt.MyPlugin");
            myPlugin.doUsefull();
        } else {
            final var pluginManager = new PluginManager(inputDirectory);
            System.out.println("Введите jar. Например MyPlugin-1.0.jar");
            String pluginName = scanner.nextLine();
            System.out.println("Введите имя class. Например ru.sbt.MyPlugin");
            String pluginClassName = scanner.nextLine();
            final var myPlugin = pluginManager.load(pluginName, pluginClassName);
            myPlugin.doUsefull();
        }

    }
}
