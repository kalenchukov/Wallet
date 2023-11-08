# Справка

## Wallet

Проект по управлению счетами игроков.

# Загрузка

Репозиторий для загрузки исходного кода приложения:

```bash
git clone https://github.com/kalenchukov/Wallet.git
```

# Подготовка

Для установить базы данных в docker, необходимо выполнить команду:

```bash
docker-compose up -d --build
```

Проверить работу базы данных в docker, можно выполнив команду:

```bash
docker ps -a
```

Нас интересует контейнер с названием "wallet-database", у которого статус должен быть "запущен".

# Сборка

```bash
mvn clean install
```

# Использование

## Документация

[http://localhost:1111/swagger-ui/index.html](http://localhost:1111/swagger-ui/index.html)

