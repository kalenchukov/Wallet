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

# Запуск

Запуск осуществляется с помощью Tomcat. Я использовал Tomcat 10.1.6.

# Использование

Управление происходит с помощью HTTP запросов отправляемых серверу.

## Игроки

### Добавление игрока

Запрос:

```bash
curl -v -X POST http://{host}:{port}/players \
-H "Content-Type: application/json; charset=UTF-8" \
-d @- << EOF
{
	"name": "{имя}",
	"password": "{пароль}"
}
EOF
```

Ответ:

```bash
{
	"playerId": "{идентификатор игрока}",
	"name": "{имя}"
}
```

### Авторизация

Запрос:

```bash
curl -v -X POST http://{host}:{port}/players/auth \
-H "Content-Type: application/json; charset=UTF-8" \
-d @- << EOF
{
	"name": "{имя}",
	"password": "{пароль}"
}
EOF
```

Ответ:

```bash
{
	"accessToken": "{токен}",
}
```

### Список всех действий

Запрос:

```bash
curl -v -X GET http://{host}:{port}/players/actions/list \
-H "Content-Type: application/json; charset=UTF-8" \
-d @- << EOF
{
	"accessToken": "{токен}"
}
EOF
```

Ответ:

```bash
[
    {
        "actionId": "{идентификатор действия}",
        "actionType": "{тип действия}",
        "actionTypeStatus": "{статус действия}"
    },
    {
      ...
    }
]
```

## Счета игроков

### Добавление счёта

Запрос:

```bash
curl -v -X POST http://{host}:{port}/players/accounts \
-H "Content-Type: application/json; charset=UTF-8" \
-d @- << EOF
{
	"accessToken": "{токен}"
}
EOF
```

Ответ:

```bash
{
	"accountId": "{идентификатор счёта}",
	"playerId": "{идентификатор игрока}",
	"amount": "{сумма на счёте}"
}
```

### Пополнение счёта

Запрос:

```bash
curl -v -X POST http://{host}:{port}/players/accounts/credit \
-H "Content-Type: application/json; charset=UTF-8" \
-d @- << EOF
{
	"accountId": {идентификатор счёта},
	"amount": {сумма},
	"accessToken": "{токен}"
}
EOF
```

Ответ:

```bash
{
	"accountId": "{идентификатор счёта}",
	"amount": "{сумма пополнения}",
	"accessToken": "{токен}",
}
```

### Списание со счёта

Запрос:

```bash
curl -v -X POST http://{host}:{port}/players/accounts/debit \
-H "Content-Type: application/json; charset=UTF-8" \
-d @- << EOF
{
	"accountId": {идентификатор счёта},
	"amount": {сумма},
	"accessToken": "{токен}"
}
EOF
```

Ответ:

```bash
{
	"accountId": "{идентификатор счёта}",
	"amount": "{сумма списания}",
	"accessToken": "{токен}",
}
```

### Информация по операции счёта

Запрос:

```bash
curl -v -X GET http://{host}:{port}/players/accounts/operations \
-H "Content-Type: application/json; charset=UTF-8" \
-d @- << EOF
{
	"operationId": {идентификатор операции},
	"accessToken": "{токен}"
}
EOF
```

Ответ:

```bash
{
	"operationId": "{идентификатор операции}",
	"accountId": "{идентификатор счёта}",
	"operationType": "{тип операции}",
	"amount": "{сумма}",
}
```

### Список всех операций со счётом

Запрос:

```bash
curl -v -X GET http://{host}:{port}/players/accounts/operations/list \
-H "Content-Type: application/json; charset=UTF-8" \
-d @- << EOF
{
	"accountId": 1,
	"accessToken": "{токен}"
}
EOF
```

Ответ:

```bash
[
    {
        "operationId": "{идентификатор операции}",
        "accountId": "{идентификатор счёта}",
        "operationType": "{тип операции}",
        "amount": "{сумма}",
    },
    {
      ...
    }
]
```