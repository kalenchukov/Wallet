/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.modules;

import dev.kalenchukov.wallet.auth.AuthToken;
import dev.kalenchukov.wallet.auth.AuthTokenImpl;
import dev.kalenchukov.wallet.exceptions.NeedAuthPlayerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@WebMvcTest(AuthTokenImpl.class)
public class AuthTokenImplTest {
	@Autowired
	private AuthToken authToken;

	@Nested
	public class CreateToken {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void createTokenValid() {
			long playerId = 1L;

			String actualToken = authToken.createToken(playerId);

			assertThat(actualToken).isNotNull();
		}
	}

	@Nested
	public class VerifyToken {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void verifyTokenValid() throws NeedAuthPlayerException {
			long playerId = 1L;
			String accessToken = authToken.createToken(playerId);

			long actualPlayerId = authToken.verifyToken(accessToken);

			assertThat(actualPlayerId).isEqualTo(playerId);
		}

		@DisplayName("Проверка с токеном доступа у которого истёк срок действия.")
		@Test
		public void verifyTokenWithExpiredTtl() {
			String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." + "eyJpc3MiOiJXYWxsZXRBdXRoIiwicGxheWVySWQiOjEsImV4cCI6MTY5Nzk3NDIwOH0." + "i86ceC0mzKDgbcA-P7BuHzOSpnd3nN_sRmQfKjWN0E4";

			assertThatExceptionOfType(NeedAuthPlayerException.class).isThrownBy(() -> {
				authToken.verifyToken(accessToken);
			});
		}

		@DisplayName("Проверка с пустым токеном доступа.")
		@Test
		public void verifyTokenWithEmptyToken() {
			String accessToken = "";

			assertThatExceptionOfType(NeedAuthPlayerException.class).isThrownBy(() -> {
				authToken.verifyToken(accessToken);
			});
		}

		@DisplayName("Проверка с некорректным токеном доступа.")
		@Test
		public void verifyTokenWithInvalidToken() {
			String accessToken = "eyJhbGciOif45gtdf7df87df767676BCI6IkpXVCJ9";

			assertThatExceptionOfType(NeedAuthPlayerException.class).isThrownBy(() -> {
				authToken.verifyToken(accessToken);
			});
		}

		@DisplayName("Проверка с null в качестве токена доступа.")
		@Test
		public void verifyTokenWithNull() {
			String accessToken = null;

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				authToken.verifyToken(accessToken);
			});
		}
	}
}