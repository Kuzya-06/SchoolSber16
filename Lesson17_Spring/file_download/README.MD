# Домашнее задание 17
## Тестовое задание:  
Есть список ссылок в текстовом файле. Нужно написать программу, которая при старте будет скачивать эти файлы и 
складывать в указанную папку на локальном диске. Программа должна уметь качать несколько файлов одновременно (в 
несколько потоков, например, 3 потока) и выдерживать указанное ограничение на скорость загрузки, например, 500 
килобайт в секунду. Программу можно сделать консольной, можно использовать spring-boot. Для работы с http, с 
ограничением скорости и любыми манипуляциями с данными можно брать любую библиотеку. При желании, можно любую часть 
сделать самостоятельно. Нужно учитывать, что программа может развиваться, из нее могут быть переиспользованы 
полезные части. Поэтому крайне желательно использовать абстракции (ООП, интерфейсы, паттерны, SOLID и т.д.).
---
## Реализация задачи с использованием **Spring Boot**, **Java 17**, и **Maven**.
Ниже описан план структуры:
### 1. **Проект Maven**

</b></details>
<details>
<summary>pom.xml</summary><br><b>

```xml
   <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents.client5</groupId>
            <artifactId>httpclient5</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
</b></details>


### 2. **Пакетная структура**
</b></details>
<details>
<summary>ru.sber.downloader</summary><br><b>

```
ru.sber.downloader
├── controller   # REST или консольный интерфейс для управления загрузками
├── service      # Бизнес-логика для загрузки файлов
└── util         # Утилитарный класс (для ограничения скорости)
```
</b></details>
### 3. **Основные компоненты**
#### **3.1. Config**
- **`application.yml`**: Конфигурационный файл для настройки приложения. Содержит:
    - Количество потоков (например, `3`).
    - Ограничение скорости загрузки (например, `500 кБ/с`).
    - Путь к папке для сохранения файлов.
    - Путь к файлу со ссылками.

</b></details>
<details>
<summary>application.yml</summary><br><b>

```yaml
spring:
  application:
    name: file_download
download:
  folder: Lesson17_Spring/file_download/downloads    # Путь для сохранения файлов
  threads: 3               # Количество потоков для загрузки
  speed-limit: 500         # Ограничение скорости загрузки (кБ/сек)
links-file: "classpath:links.txt"  # Файл со ссылками
server:
  port: 8080               # Порт приложения
``` 
</b></details>

#### **3.2. Util**
- **`RateLimitedOutputStream`**: Реализация ограничения скорости загрузки. Использует библиотеку **bucket4j** или кастомный
  механизм с таймингами. 

#### **3.3. Service**
- **`DownloadService`**: Основной сервис, отвечающий за:
    - Управление многопоточностью.
    - Скачивание файлов с использованием HTTP.
    - Применение ограничения скорости.
    - Логирование и обработку ошибок.

#### **3.4. Controller**
- **`DownloadController`**: Предоставляет конечные точки ( `/start` для запуска загрузки).

### 4. **Основной поток выполнения**
1. Программа считывает ссылки из указанного файла.
2. На основе списка создаются задачи загрузки.
3. Задачи выполняются параллельно с учетом ограничения на количество потоков и скорость.
4. Загруженные файлы сохраняются в указанной папке.
5. Логируются результаты выполнения (успешно/с ошибкой).

# Выполнить запрос
```yaml
###
GET http://localhost:8080/api/download/start
```
Файл links.txt хранит ссылки на файлы в S3
```txt
https://6d3842e0-e3df-43b8-b35c-2d5d460fe4ac.selstorage.ru/10mb-example-jpg-1.jpg
https://6d3842e0-e3df-43b8-b35c-2d5d460fe4ac.selstorage.ru/10mb-example-jpg-2.jpg
https://6d3842e0-e3df-43b8-b35c-2d5d460fe4ac.selstorage.ru/10mb-example-jpg-3.jpg
https://6d3842e0-e3df-43b8-b35c-2d5d460fe4ac.selstorage.ru/10mb-example-jpg-4.jpg
https://6d3842e0-e3df-43b8-b35c-2d5d460fe4ac.selstorage.ru/10mb-example-jpg-5.jpg
```
Произойдет загрузка файлов в директорию `downloads`.