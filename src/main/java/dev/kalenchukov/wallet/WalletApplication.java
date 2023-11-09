/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс запуска приложения.
 */
@SpringBootApplication
public class WalletApplication {
	/**
	 * Метод запуска.
	 *
	 * @param args параметры.
	 */
	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}
}
