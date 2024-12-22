package ru.sber;

public interface ThreadPool {
    /**
     * Запускает потоки. Потоки бездействуют, до тех пор пока не появится новое задание в очереди (см. execute)
     */
    void start();

    /**
     * Складывает это задание в очередь. Освободившийся поток должен выполнить это задание. Каждое задание должно
     * быть выполнено ровно 1 раз
     * @param runnable
     */
    void execute(Runnable runnable);

    void shutdown();
}
