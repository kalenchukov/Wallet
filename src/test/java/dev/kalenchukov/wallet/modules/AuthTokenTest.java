/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.modules;

import dev.kalenchukov.wallet.exceptions.NeedAuthPlayerException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Класс проверки методов класса {@link AuthToken}.
 */
public class AuthTokenTest {
	/**
	 * Класс проверки статических методов класса {@link AuthToken}.
	 */
	@Nested
	public class Static {
		/**
		 * Класс проверки метода {@link AuthToken#createToken(long)}.
		 */
		@Nested
		public class CreateToken {
			/**
			 * Проверка метода {@link AuthToken#createToken(long)}.
			 */
			@Test
			public void createToken() {
				long playerId = 1L;

				String actualToken = AuthToken.createToken(playerId);

				assertThat(actualToken).isNotNull();
			}
		}

		/**
		 * Класс проверки метода {@link AuthToken#verifyToken(String)}.
		 */
		@Nested
		public class VerifyToken {
			/**
			 * Проверка метода {@link AuthToken#verifyToken(String)}.
			 */
			@Test
			public void verifyToken() throws NeedAuthPlayerException {
				long playerId = 1L;
				String accessToken = AuthToken.createToken(playerId);

				long actualPlayerId = AuthToken.verifyToken(accessToken);

				assertThat(actualPlayerId).isEqualTo(playerId);
			}

			/**
			 * Проверка метода {@link AuthToken#verifyToken(String)}
			 * с токеном доступа у которого истёк срок действия.
			 */
			@Test
			public void verifyTokenWithExpiredTtl() {
				String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
						"eyJpc3MiOiJXYWxsZXRBdXRoIiwicGxheWVySWQiOjEsImV4cCI6MTY5Nzk3NDIwOH0." +
						"i86ceC0mzKDgbcA-P7BuHzOSpnd3nN_sRmQfKjWN0E4";

				assertThatExceptionOfType(NeedAuthPlayerException.class).isThrownBy(() -> {
					AuthToken.verifyToken(accessToken);
				});
			}

			/**
			 * Проверка метода {@link AuthToken#verifyToken(String)}
			 * с пустым токеном доступа.
			 */
			@Test
			public void verifyTokenWithEmptyToken() {
				String accessToken = "";

				assertThatExceptionOfType(NeedAuthPlayerException.class).isThrownBy(() -> {
					AuthToken.verifyToken(accessToken);
				});
			}

			/**
			 * Проверка метода {@link AuthToken#verifyToken(String)}
			 * с некорректным токеном доступа.
			 */
			@Test
			public void verifyTokenWithInvalidToken() {
				String accessToken = "eyJhbGciOif45gtdf7df87df767676BCI6IkpXVCJ9";

				assertThatExceptionOfType(NeedAuthPlayerException.class).isThrownBy(() -> {
					AuthToken.verifyToken(accessToken);
				});
			}

			/**
			 * Проверка метода {@link AuthToken#verifyToken(String)}
			 * с {@code null} в качестве токена доступа.
			 */
			@Test
			public void verifyTokenWithNull() {
				String accessToken = null;

				assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
					AuthToken.verifyToken(accessToken);
				});
			}
		}
	}
}