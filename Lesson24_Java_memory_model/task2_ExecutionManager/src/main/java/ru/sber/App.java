package ru.sber;


public class App {
    public static void main(String[] args) {
        Context context = getContext();

        System.out.println("Задачи успешно выполнились: " + context.getCompletedTaskCount());
        System.out.println("количество тасков, при выполнении которых произошел Exception: " + context.getFailedTaskCount());
        System.out.println("количество тасков, которые не были выполены из-за отмены (вызовом предыдущего метода): " + context.getInterruptedTaskCount());
        System.out.println("все таски были выполнены или отменены: " + context.isFinished());

    }

    private static Context getContext() {
        ExecutionManager manager = new ExecutionManagerImpl(6);
        Runnable callback = () -> System.out.println("Все задачи выполнены.");
        Runnable task1 = () -> System.out.println("Задача 1 выполняется");
        Runnable task2 = () -> System.out.println("Задача 2 выполняется");
        Runnable task3 = () -> System.out.println("Задача 3 выполняется");
        Runnable task4 = () -> System.out.println("Задача 4 выполняется");

        Context context = manager.execute(callback, task1, task2, task3, task4);

        context.interrupt();
        return context;
    }
}
