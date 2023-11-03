/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.modules;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import dev.kalenchukov.wallet.exceptions.NeedAuthPlayerException;
import dev.kalenchukov.wallet.properties.Props;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Класс токена авторизации.
 */
public final class AuthToken {
	/**
	 * Создаёт токен доступа с идентификаторов игрока.
	 *
	 * @param playerId идентификатор игрока.
	 * @return токен доступа.
	 */
	public static String createToken(final long playerId) {
		return JWT.create()
				.withIssuer(Props.get().getAccessToken().getServer())
				.withClaim("playerId", playerId)
				.withExpiresAt(Instant.now().plusSeconds(Props.get().getAccessToken().getTtl()))
				.sign(Algorithm.HMAC256(Props.get().getAccessToken().getSecret()));
	}

	/**
	 * Проверяет достоверность и актуальность токена доступа.
	 *
	 * @param accessToken токен доступа.
	 * @return идентификатор игрока.
	 * @throws NeedAuthPlayerException если токен доступа не достоверный или не актуальный.
	 */
	public static long verifyToken(final String accessToken) throws NeedAuthPlayerException {
		Objects.requireNonNull(accessToken);

		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(Props.get().getAccessToken().getSecret()))
					.withIssuer(Props.get().getAccessToken().getServer())
					.acceptExpiresAt(Props.get().getAccessToken().getTtl())
					.build();
			DecodedJWT decodedJWT = verifier.verify(accessToken);
			Map<String, Claim> claims = decodedJWT.getClaims();

			if (claims.containsKey("playerId")) {
				return claims.get("playerId").asLong();
			} else {
				throw new NeedAuthPlayerException();
			}
		} catch (JWTVerificationException exception) {
			throw new NeedAuthPlayerException();
		}
	}
}
