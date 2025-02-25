package ru.sber;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExecutionManagerImplTest {

    @RepeatedTest(3)
    public void testExecutionManager() throws InterruptedException {
        ExecutionManager manager = new ExecutionManagerImpl(3);

        Runnable task = () -> System.out.println("Задача выполнена");
        Runnable task2 = () -> System.out.println("Задача 2 выполнена");
        Runnable callback = () -> System.out.println("Callback выполн");
        Context context = manager.execute(callback, task, task2, task);
//        context.interrupt();
        Thread.sleep(500); // Для завершения выполнения задач
        context.interrupt();
        assertEquals(3, context.getCompletedTaskCount());
        assertEquals(0, context.getFailedTaskCount());
        assertEquals(0, context.getInterruptedTaskCount());
        assertTrue(context.isFinished());
    }

    @Test
    public void testFailedTasks() throws InterruptedException {
        ExecutionManager manager = new ExecutionManagerImpl(3);
        Runnable task = () -> System.out.println("Задача выполнена");
        Runnable failingTask = () -> {
            throw new RuntimeException("Задача не выполнена");
        };
        Runnable callback = () -> System.out.println("Callback выполнен");
        Context context = manager.execute(callback, task, failingTask, failingTask);

        Thread.sleep(1000); // Для завершения выполнения задач
        assertEquals(2, context.getFailedTaskCount());
        assertEquals(1, context.getCompletedTaskCount());
        assertEquals(0, context.getInterruptedTaskCount());
        assertTrue(context.isFinished());
    }

    @Test
    public void testInterruptedTasks() throws InterruptedException {
        ExecutionManager manager = new ExecutionManagerImpl(2);
        Runnable longTask = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    if (Thread.interrupted()) {
                        throw new InterruptedException(); // Прерывание, если флаг установлен
                    }
                    Thread.sleep(100); // Имитация работы
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Задача прервана");
            }
        };
        Runnable callback = () -> System.out.println("Callback выполнен");
        Context context = manager.execute(callback, longTask, longTask, longTask);

        // Прерываем выполнение
        context.interrupt();

        Thread.sleep(1200); // Для завершения выполнения задач

        // Проверяем, что хотя бы одна задача была прервана
        assertTrue(context.getInterruptedTaskCount() > 0);
//        assertEquals(1, context.getInterruptedTaskCount());
        assertTrue(context.isFinished());
    }

}
