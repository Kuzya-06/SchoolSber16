# Домашнее задание 16
## Разработать продвинутый кэш, который помнит о кэшированных данных после перезапуска приложения 
### Подготовка для Windows
1. Запустить Docker 
2. Запустить следующую команду
```bash
docker run --name cashDB -p 5433:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -v c:\postgresdocker:/var/lib/postgresql/data --restart=unless-stopped postgres:16
```
3. Запустить приложение
