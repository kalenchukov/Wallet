/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.type;

/**
 * Перечисление типов действий.
 */
public enum ActionType {
	/**
	 * Создание счёта.
	 */
	CREATE_ACCOUNT,

	/**
	 * Пополнение счёта.
	 */
	CREDIT_ACCOUNT,

	/**
	 * Списание со счёта.
	 */
	DEBIT_ACCOUNT,

	/**
	 * Получение операции по счёту.
	 */
	OPERATION_ACCOUNT,

	/**
	 * Получение списка операций по счёту.
	 */
	OPERATIONS_ACCOUNT,

	/**
	 * Получение списка действий игрока.
	 */
	ACTIONS;

	/**
	 * Перечисление типов статусов действий.
	 */
	public enum Status {
		/**
		 * Успешно.
		 */
		SUCCESS,

		/**
		 * Неудача.
		 */
		FAIL;
	}
}
