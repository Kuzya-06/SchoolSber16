# Домашнее задание 18 Фреймворк Spring – Spring WEB MVC
## Разработать 
основное:
Реализовать CRUD приложение с использованием spring web (mvc) + бд (фронт по желанию)

🔥 Перенести проект из темы
Exceptions в парадигму web’a

🔥🔥Написать WEB-приложение c сервлетом|сервлетным фильтром, который осуществляет получение содержимого удалённого ресурса и возвращает его в своём ответе (GET запрос).
Ссылка на ресурс передаётся в параметре url исходного запроса.

## 📁 Структура проекта основного задания
```css
springmvc/src/main/java/ru/sber/mvc
 ├── controller
 │   └── MenuController.java
 ├── mapper
 │   └── MenuItemMapper.java    
 ├── model
 │   ├── dto
 │   │   └── MenuItemDto.java
 │   └── entity
 │        └── MenuItem.java
 ├── repository
 │   └── MenuItemRepository.java
 ├── service
 │   └── MenuItemService.java
 └── SpringmvcApplication.java
```
## 📁 Структура проекта задания с 🔥 Перенести проект из темы Exceptions в парадигму web’a

```css
.\frontend

.\src\main\java\ru\sber\atm
├── config
│   ├── CorsConfig.java
│   └── SwaggerConfig.java
├── controller
│   └── TerminalController.java
├── exception
│   ├── AccountLockedException.java
│   └── TerminalException.java
├── handler
│   └── TerminalExceptionHandler.java
├── model
│   └── request
│       ├── AmountRequest.java
│       └── PinRequest.java
├── service
│   └── TerminalService.java
└── AtmExceptionApplication.java

```

## 📁 Структура проекта задания с 🔥🔥Написать WEB-приложение c сервлетом|сервлетным фильтром, который осуществляет получение содержимого удалённого ресурса и возвращает его в своём ответе (GET запрос). Ссылка на ресурс передаётся в параметре url исходного запроса.

```css
/src/main/java/ru/sber
 ├── apiservlet
 │   └── HelloServlet.java
 ├── filter
 │   └── LoggingFilter.java 
 └── servlet
     └── ProxyServlet.java

/src/main/java/webapp
 ├── WEB-INF
 │   └── web.xml
 └── index.jsp
```