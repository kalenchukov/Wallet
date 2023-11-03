/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.properties.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс параметров конфигурации тестирования.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TestProperty {
	/**
	 * Образ базы данных для тест-контейнера.
	 */
	private String dockerImage;
}
