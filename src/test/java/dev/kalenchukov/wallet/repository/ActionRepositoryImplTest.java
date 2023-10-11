/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Action;
import dev.kalenchukov.wallet.entity.Player;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * Класс проверки методов класса {@link ActionRepositoryImpl}.
 */
public class ActionRepositoryImplTest {
	/**
	 * Класс проверки метода {@link ActionRepositoryImpl#save(Action)}.
	 */
	@Nested
	public class Save {
		/**
		 * Проверка метода {@link ActionRepositoryImpl#save(Action)}.
		 */
		@Test
		public void save() throws Exception {
			Set<Action> data = spy(new HashSet<>());
			Action action = mock(Action.class);
			ActionRepository actionRepository = new ActionRepositoryImpl();
			when(data.add(action)).thenReturn(true);

			Field field = actionRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(actionRepository, data);

			Action actual = actionRepository.save(action);

			assertThat(actual).isEqualTo(action);
			verify(data, only()).add(action);
		}

		/**
		 * Проверка метода {@link ActionRepositoryImpl#save(Action)} с {@code null} в качестве действия.
		 */
		@Test
		public void saveWithNullAction() {
			ActionRepository actionRepository = new ActionRepositoryImpl();

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
		public void find() throws NoSuchFieldException, IllegalAccessException {
			Set<Action> data = spy(new HashSet<>());
			long playerId = 417L;
			Action action = mock(Action.class);
			when(action.getPlayerId()).thenReturn(playerId);
			ActionRepository actionRepository = new ActionRepositoryImpl();
			when(data.add(action)).thenReturn(true);

			Field field = actionRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(actionRepository, data);

			Set<Action> actual = actionRepository.find(playerId);

			assertThat(actual).hasSize(1);
		}

		/**
		 * Проверка метода {@link ActionRepositoryImpl#find(long)}.
		 */
		@Test
		public void findWithNotFound() throws NoSuchFieldException, IllegalAccessException {
			Set<Action> data = spy(new HashSet<>());
			long playerId1 = 417L;
			long playerId2 = 1000L;
			Action action = mock(Action.class);
			when(action.getPlayerId()).thenReturn(playerId1);
			ActionRepository actionRepository = new ActionRepositoryImpl();
			when(data.add(action)).thenReturn(true);

			Field field = actionRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(actionRepository, data);

			Set<Action> actual = actionRepository.find(playerId2);

			assertThat(actual).isEmpty();
		}
	}
}