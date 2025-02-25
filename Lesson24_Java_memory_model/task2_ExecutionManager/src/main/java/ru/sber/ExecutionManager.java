package ru.sber;

public interface ExecutionManager {

    /**
     * принимает массив тасков, это задания которые ExecutionManager должен выполнять параллельно (в своем пуле
     * потоков). После завершения всех тасков должен выполниться callback (ровно 1 раз)
     */
    Context execute(Runnable callback, Runnable... tasks);
}