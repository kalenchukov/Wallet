/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.auth;

import dev.kalenchukov.wallet.exceptions.NeedAuthPlayerException;

/**
 * Интерфейс для реализации токена авторизации.
 */
public interface AuthToken {
	/**
	 * Создаёт токен доступа с идентификаторов игрока.
	 *
	 * @param playerId идентификатор игрока.
	 * @return токен доступа.
	 */
	String createToken(long playerId);

	/**
	 * Проверяет достоверность и актуальность токена доступа.
	 *
	 * @param accessToken токен доступа.
	 * @return идентификатор игрока.
	 * @throws NeedAuthPlayerException если токен доступа не достоверный или не актуальный.
	 */
	long verifyToken(String accessToken) throws NeedAuthPlayerException;
}
