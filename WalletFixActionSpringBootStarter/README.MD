# Справка

## WalletFixActionSpringBootStarter

Проект Spring Boot Starter для проекта Wallet фиксирующий выполнение действий игроков.

# Использование

## Добавление

Для проекта maven:

```
<dependency>
    <groupId>dev.kalenchukov.starter</groupId>
    <artifactId>wallet-fix-action-spring-boot-starter</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

## Подключение

Для включения функционала необходимо использовать аннотацию @EnableFixAction.

## Фиксация действия

Для пометки метода, выполнение которого необходимо зафиксировать, используется аннотация @FixAction.
