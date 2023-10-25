/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.Config;
import dev.kalenchukov.wallet.entity.Action;
import dev.kalenchukov.wallet.modules.Liquibase;
import dev.kalenchukov.wallet.repository.ActionRepository;
import dev.kalenchukov.wallet.type.ActionType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Класс проверки методов класса {@link ActionRepositoryImpl}.
 */
public class ActionRepositoryImplTest {
	private static final PostgreSQLContainer<?> POSTGRES =
			new PostgreSQLContainer<>(Config.get().getProperty("test.docker.image"));

	private static DataSource DATA_SOURCE;

	@BeforeAll
	public static void beforeAll() {
		POSTGRES.withDatabaseName(Config.get().getProperty("database.name"));
		POSTGRES.withUsername(Config.get().getProperty("database.username"));
		POSTGRES.withPassword(Config.get().getProperty("database.password"));
		POSTGRES.start();

		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setUrl(POSTGRES.getJdbcUrl());
		dataSource.setUser(POSTGRES.getUsername());
		dataSource.setPassword(POSTGRES.getPassword());
		dataSource.setCurrentSchema(Config.get().getProperty("liquibase.schema.app"));
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
	 * Класс проверки метода {@link ActionRepositoryImpl#save(Action)}.
	 */
	@Nested
	public class Save {
		/**
		 * Проверка метода {@link ActionRepositoryImpl#save(Action)}.
		 */
		@Test
		public void save() {
			Action action = mock(Action.class);
			when(action.getPlayerId()).thenReturn(1L);
			when(action.getActionType()).thenReturn(ActionType.AUTH);
			when(action.getActionTypeStatus()).thenReturn(ActionType.Status.SUCCESS);
			ActionRepository actionRepository = new ActionRepositoryImpl(DATA_SOURCE);

			Action actualAction = actionRepository.save(action);

			assertThat(actualAction.getActionId()).isPositive();
			assertThat(actualAction.getPlayerId()).isEqualTo(action.getPlayerId());
			assertThat(actualAction.getActionType()).isEqualTo(action.getActionType());
			assertThat(actualAction.getActionTypeStatus()).isEqualTo(action.getActionTypeStatus());
		}

		/**
		 * Проверка метода {@link ActionRepositoryImpl#save(Action)} с {@code null} в качестве действия.
		 */
		@Test
		public void saveWithNull() {
			ActionRepository actionRepository = new ActionRepositoryImpl(DATA_SOURCE);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				actionRepository.save(null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link ActionRepositoryImpl#find(long)}.
	 */
	@Nested
	public class Find {
		/**
		 * Проверка метода {@link ActionRepositoryImpl#find(long)}.
		 */
		@Test
		public void find() {
			long playerId = 1L;
			ActionRepository actionRepository = new ActionRepositoryImpl(DATA_SOURCE);

			List<Action> actualSet = actionRepository.find(playerId);

			assertThat(actualSet).hasSize(11);
		}

		/**
		 * Проверка метода {@link ActionRepositoryImpl#find(long)} с отсутствующими действиями.
		 */
		@Test
		public void findWithNotFound() {
			long playerId = 446651L;
			ActionRepository actionRepository = new ActionRepositoryImpl(DATA_SOURCE);

			List<Action> actualSet = actionRepository.find(playerId);

			assertThat(actualSet).isEmpty();
		}
	}
}