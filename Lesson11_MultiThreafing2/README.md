# MULTITHREADING part 2
# Java: Вопросы и Ответы

## <span style="color:green">  # 1. Как получить ссылку на текущий поток ?
</b></details>
<details>
<summary> Ответ: </summary><br>

Используй статический метод Thread.currentThread(). Он возвращает объект Thread, представляющий текущий поток выполнения.

```java
Thread currentThread = Thread.currentThread();
System.out.println("Current thread: " + currentThread.getName());
```
</b></details>

## <span style="color:green">  #2. Зачем нужно ключевое слово synchronized ? На что его можно вещать(поле, метод, класс, конструктор..) ?
</b></details>
<details>
<summary> Ответ: </summary><br>

Ключевое слово <b>synchronized</b> обеспечивает взаимное исключение (mutual exclusion), предотвращая одновременный 
доступ к критическим секциям кода несколькими потоками. Оно используется для синхронизации потоков.

<b>Где можно использовать synchronized?</b>
* <span style="color:#556abb"> Методы:
Синхронизация всего метода:

```java
public synchronized void exampleMethod() {
// synchronized block
}
```
* <span style="color:#556abb"> Блоки кода:
Синхронизация внутри метода:

```java
public void exampleMethod() {
synchronized(this) {
// synchronized block
}
}
```
* <span style="color:#556abb"> Статические методы:
Синхронизация на уровне класса:

```java
public static synchronized void staticMethod() {
// synchronized block
}
```
</b></details>

## <span style="color:green">  #3. Захват какого монитора происходит при входе в synchronized метод/статик метод/блок ?
</b></details>
<details>
<summary> Ответ: </summary><br> 
  
* <b>Экземплярный метод:</b> Захватывается монитор объекта, на котором вызван метод (this).  
* <b>Статический метод:</b> Захватывается монитор класса (ClassName.class).  
* <b>Блок synchronized:</b> Захватывается монитор объекта, указанного в synchronized.  
Пример:

```java
synchronized (someObject) {
// Монитор объекта someObject
}
```
</b></details>

## <span style="color:green">  #4. Зачем нужно ключевое слово volatile ? На что его можно вещать(поле, метод, класс, конструктор..) ?
</b></details>
<details>
<summary> Ответ: </summary><br>
Ключевое слово <b>volatile</b> обеспечивает видимость изменений переменной между потоками. Оно предотвращает 
кэширование 
переменной потоком, гарантируя, что значение всегда читается из главной памяти.

Где можно использовать <b>volatile</b>?
Только для полей класса:

```java
private volatile boolean flag;
```
* <span style="color:orangered"> Нельзя использовать для методов, классов или конструкторов.
* <span style="color:dodgerblue"> Это не альтернатива synchronized, так как volatile не обеспечивает атомарность операций.
</b></details>

## <span style="color:green">  #5. Что делает метод Object#wait, Object#notify, Object#notifyAll
</b></details>
<details>
<summary> Ответ: </summary><br><b>
Эти методы используются для управления потоками в контексте мониторной синхронизации.

* <span style="color:#556abb">wait()  </span>  
Освобождает монитор объекта и переводит поток в состояние ожидания (waiting) до вызова notify или notifyAll.

* <span style="color:#556abb">notify()  </span>  
Будит один поток, ожидающий на мониторе объекта.

* <span style="color:#556abb">notifyAll()  </span>  
Будит все потоки, ожидающие на мониторе объекта.

Пример:

```java
synchronized (lock) {
while (!condition) {
lock.wait(); // поток ждет
}
lock.notify(); // пробуждение потока
}
```
</b></details>

## <span style="color:green">  #6. Что за исключение IllegalMonitorStateException ?
</b></details>
<details>
<summary> Ответ: </summary>
Это исключение возникает, если:

* Поток пытается вызвать wait, notify, или notifyAll без владения монитором объекта.  
Пример ошибки:

```java

Object lock = new Object();
lock.notify(); // Ошибка: IllegalMonitorStateException
```
Решение:

```java
synchronized (lock) {
lock.notify(); // Корректно
}
```
</b></details>

## <span style="color:green">  #7. Что делает метод Thread#join ?
</b></details>
<details>
<summary> Ответ: </summary><br>
Метод <b>join</b> заставляет текущий поток дождаться завершения другого потока.

Пример:

```java
Thread t = new Thread(() -> {
System.out.println("Поток работает...");
});
t.start();
t.join(); // Ожидание завершения потока t
System.out.println("Поток завершен.");
```
</b></details>

## <span style="color:green">  #8. Что делает метод Thread#interrupt ?
</b></details>
<details>
<summary> Ответ: </summary><br>
Метод interrupt сигнализирует потоку, что его следует прервать. Он не завершает поток, а только устанавливает флаг прерывания.

Пример:

```java
Thread t = new Thread(() -> {
    try {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Работаю...");
            Thread.sleep(1000);
        }
    } catch (InterruptedException e) {
        System.out.println("Поток прерван!");
    }
});
t.start();
Thread.sleep(3000);
t.interrupt(); // Прерывание потока
```
</b></details>

