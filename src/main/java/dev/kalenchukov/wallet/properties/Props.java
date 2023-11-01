/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.properties;

import dev.kalenchukov.wallet.properties.entity.MasterProperty;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

/**
 * Класс конфигурации.
 */
public final class Props {
	/**
	 * Параметры конфигурации.
	 */
	private static final MasterProperty props = Props.load();

	/**
	 * Возвращает параметры конфигурации.
	 *
	 * @return параметры конфигурации.
	 */
	public static MasterProperty get() {
		return Props.props;
	}

	/**
	 * Загружает параметры из файла конфигурации.
	 */
	private static MasterProperty load() {
		MasterProperty properties;

		try (InputStream inputStreamFile = Props.class.getResourceAsStream("/application.yml")) {
			Yaml yaml = new Yaml();
			properties = yaml.loadAs(inputStreamFile, MasterProperty.class);
		} catch (IOException exception) {
			throw new RuntimeException("Не удалось загрузить конфигурацию приложения.");
		}

		return properties;
	}
}
