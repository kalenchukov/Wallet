/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.Config;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.repository.PlayerRepository;
import dev.kalenchukov.wallet.repository.modules.Liquibase;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Класс проверки методов класса {@link PlayerRepositoryImpl}.
 */
public class PlayerRepositoryImplTest {
	private static final PostgreSQLContainer<?> POSTGRES =
			new PostgreSQLContainer<>(Config.get().getProperty("docker.image"));

	private static DataSource DATA_SOURCE;

	@BeforeAll
	public static void beforeAll() {
		POSTGRES.withDatabaseName(Config.get().getProperty("database"));
		POSTGRES.withUsername(Config.get().getProperty("username"));
		POSTGRES.withPassword(Config.get().getProperty("password"));
		POSTGRES.start();

		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setUrl(POSTGRES.getJdbcUrl());
		dataSource.setUser(POSTGRES.getUsername());
		dataSource.setPassword(POSTGRES.getPassword());
		dataSource.setCurrentSchema(Config.get().getProperty("application.schema"));
		DATA_SOURCE = dataSource;

		Liquibase.init(
				POSTGRES.getJdbcUrl(),
				POSTGRES.getUsername(),
				POSTGRES.getPassword()
		);
	}

	@AfterAll
	public static void afterAll() {
		POSTGRES.stop();
	}

	/**
	 * Класс проверки метода {@link PlayerRepositoryImpl#save(Player)}.
	 */
	@Nested
	public class Save {
		/**
		 * Проверка метода {@link PlayerRepositoryImpl#save(Player)}.
		 */
		@Test
		public void save() {
			Player player = mock(Player.class);
			when(player.getName()).thenReturn("fedya");
			when(player.getPassword()).thenReturn("d5478af3238e9f2332ce87eb3958b38a");
			PlayerRepository playerRepository = new PlayerRepositoryImpl(DATA_SOURCE);

			Player actualPlayer = playerRepository.save(player);

			assertThat(actualPlayer.getPlayerId()).isPositive();
			assertThat(actualPlayer.getName()).isEqualTo(player.getName());
			assertThat(actualPlayer.getPassword()).isEqualTo(player.getPassword());
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#save(Player)} с {@code null} в качестве игрока.
		 */
		@Test
		public void saveWithNull() {
			PlayerRepository playerRepository = new PlayerRepositoryImpl(DATA_SOURCE);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.save(null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link PlayerRepositoryImpl#existsByName(String)}.
	 */
	@Nested
	public class IsByName {
		/**
		 * Проверка метода {@link PlayerRepositoryImpl#existsByName(String)}.
		 */
		@Test
		public void existsByName() {
			String name = "igor";
			PlayerRepository playerRepository = new PlayerRepositoryImpl(DATA_SOURCE);

			boolean actual = playerRepository.existsByName(name);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#existsByName(String)} с отсутствием игроком.
		 */
		@Test
		public void existsByNameWithNotFound() {
			String name = "fedya";
			PlayerRepository playerRepository = new PlayerRepositoryImpl(DATA_SOURCE);

			boolean actual = playerRepository.existsByName(name);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#existsByName(String)} с {@code null} в качестве имени.
		 */
		@Test
		public void existsByNameWithNullName() {
			PlayerRepository playerRepository = new PlayerRepositoryImpl(DATA_SOURCE);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.existsByName(null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link PlayerRepositoryImpl#find(String, String)}.
	 */
	@Nested
	public class FindByNameAndPassword {
		/**
		 * Проверка метода {@link PlayerRepositoryImpl#find(String, String)}.
		 */
		@Test
		public void find() {
			String name = "igor";
			String password = DigestUtils.md5Hex("igor");
			PlayerRepository playerRepository = new PlayerRepositoryImpl(DATA_SOURCE);

			Optional<Player> actualPlayer = playerRepository.find(name, password);

			assertThat(actualPlayer).isPresent();
			assertThat(actualPlayer.get().getPlayerId()).isEqualTo(3L);
			assertThat(actualPlayer.get().getName()).isEqualTo(name);
			assertThat(actualPlayer.get().getPassword()).isEqualTo(password);
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#find(String, String)} с отсутствующим игроком.
		 */
		@Test
		public void findWithNotFound() {
			String name = "fedya";
			String password = DigestUtils.md5Hex("igor");
			PlayerRepository playerRepository = new PlayerRepositoryImpl(DATA_SOURCE);

			Optional<Player> actualPlayer = playerRepository.find(name, password);

			assertThat(actualPlayer).isEmpty();
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#find(String, String)} с {@code null} в качестве имени.
		 */
		@Test
		public void findWithNullName() {
			String password = "45b645g45f35dt34454b57g4v53";
			PlayerRepository playerRepository = new PlayerRepositoryImpl(DATA_SOURCE);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.find(null, password);
			});
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#find(String, String)} с {@code null} в качестве пароля.
		 */
		@Test
		public void findWithNullPassword() {
			String name = "qwe";
			PlayerRepository playerRepository = new PlayerRepositoryImpl(DATA_SOURCE);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.find(name, null);
			});
		}
	}
}