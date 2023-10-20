/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet;

import dev.kalenchukov.wallet.repository.modules.DataBase;

import java.io.IOException;
import java.util.Properties;

/**
 * Класс конфигурации.
 */
public final class Config {
	/**
	 * Параметры конфигурации.
	 */
	private static final Properties config = Config.load();

	/**
	 * Возвращает параметры конфигурации.
	 *
	 * @return параметры конфигурации.
	 */
	public static Properties get() {
		return Config.config;
	}

	/**
	 * Загружает параметры из файла конфигурации.
	 */
	private static Properties load() {
		Properties properties = new Properties();

		try {
			properties.load(DataBase.class.getResourceAsStream("/application.properties"));
		} catch (IOException exception) {
			System.out.println("Не удалось загрузить конфигурацию приложения.");
		}

		return properties;
	}
}
