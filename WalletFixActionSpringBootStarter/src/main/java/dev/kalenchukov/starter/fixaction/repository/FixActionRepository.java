/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.starter.fixaction.repository;

import dev.kalenchukov.starter.fixaction.entity.Action;

/**
 * Интерфейс для реализации класса хранилища действий.
 */
public interface FixActionRepository {
	/**
	 * Сохраняет действие.
	 *
	 * @param action действие.
	 * @return действие.
	 */
	Action save(Action action);
}
