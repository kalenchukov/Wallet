/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import dev.kalenchukov.wallet.exceptions.NeedAuthPlayerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Класс токена авторизации.
 */
@Component
public class AuthTokenImpl implements AuthToken {
	@Value("${access-token.server}")
	private String server;

	@Value("${access-token.ttl}")
	private int ttl;

	@Value("${access-token.secret}")
	private String secret;

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public String createToken(final long playerId) {
		return JWT.create().withIssuer(this.server)
				.withClaim("playerId", playerId)
				.withExpiresAt(Instant.now().plusSeconds(this.ttl))
				.sign(Algorithm.HMAC256(this.secret));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param accessToken {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NeedAuthPlayerException {@inheritDoc}
	 */
	@Override
	public long verifyToken(final String accessToken) throws NeedAuthPlayerException {
		Objects.requireNonNull(accessToken);

		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(this.secret))
					.withIssuer(this.server).acceptExpiresAt(this.ttl).build();
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
