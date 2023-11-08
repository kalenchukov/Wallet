/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet;

import dev.kalenchukov.starter.fixaction.annotations.EnableFixAction;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Класс конфигурации приложения.
 */
@Configuration
@ComponentScan("dev.kalenchukov.wallet")
@EnableFixAction
public class Config {
	/**
	 * Конфигурация Swagger.
	 *
	 * @param info информация о проекте.
	 * @return конфигурацию.
	 */
	@Bean
	public OpenAPI openAPI(final Info info) {
		return new OpenAPI().info(info);
	}

	/**
	 * Возвращает информацию о проекте.
	 *
	 * @param contact контакт по вопросам API.
	 * @return информацию о проекте.
	 */
	@Bean
	public Info info(final Contact contact) {
		return new Info().title("Wallet API").description("Документация проекта по управлению счетами игроков").version(
				"5.0.0").contact(contact);
	}

	/**
	 * Возвращает контакт по вопросам API проекта.
	 *
	 * @return контакт.
	 */
	@Bean
	public Contact contact() {
		return new Contact().name("Алексей Каленчуков").email("aleksey.kalenchukov@yandex.ru").url(
				"https://github.com/kalenchukov");
	}
}
