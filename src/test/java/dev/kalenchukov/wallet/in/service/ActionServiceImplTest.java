/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service;

import dev.kalenchukov.wallet.entity.Action;
import dev.kalenchukov.wallet.repository.ActionRepository;
import dev.kalenchukov.wallet.repository.ActionRepositoryImpl;
import dev.kalenchukov.wallet.resources.ActionType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * Класс проверки методов класса {@link ActionServiceImpl}.
 */
public class ActionServiceImplTest {
	/**
	 * Класс проверки метода {@link ActionServiceImpl#add(long, ActionType, ActionType.Status)}.
	 */
	@Nested
	public class Add {
		/**
		 * Проверка метода {@link ActionServiceImpl#add(long, ActionType, ActionType.Status)}.
		 */
		@Test
		public void add() {
			long playerId = 36L;
			Action action = mock(Action.class);
			ActionType actionType = mock(ActionType.class);
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
			ActionRepository actionRepository = mock(ActionRepositoryImpl.class);
			when(actionRepository.save(any(Action.class))).thenReturn(action);
			ActionService actionService = new ActionServiceImpl(actionRepository);

			Action actualAction = actionService.add(playerId, actionType, actionTypeStatus);

			assertThat(actualAction).isEqualTo(action);
			verify(actionRepository, only()).save(any(Action.class));
		}

		/**
		 * Проверка метода {@link ActionServiceImpl#add(long, ActionType, ActionType.Status)} с {@code null} в качестве типа действия.
		 */
		@Test
		public void addWithNullActionType() {
			long playerId = 36L;
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
			ActionRepository actionRepository = mock(ActionRepositoryImpl.class);
			ActionService actionService = new ActionServiceImpl(actionRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				actionService.add(playerId, null, actionTypeStatus);
			});
		}

		/**
		 * Проверка метода {@link ActionServiceImpl#add(long, ActionType, ActionType.Status)} с {@code null} в качестве статуса действия.
		 */
		@Test
		public void addWithNullActionTypeStatus() {
			long playerId = 36L;
			ActionType actionType = mock(ActionType.class);
			ActionRepository actionRepository = mock(ActionRepositoryImpl.class);
			ActionService actionService = new ActionServiceImpl(actionRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				actionService.add(playerId, actionType, null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link ActionServiceImpl#find(long)}.
	 */
	@Nested
	public class Find {
		/**
		 * Проверка метода {@link ActionServiceImpl#find(long)}.
		 */
		@Test
		public void find() {
			long playerId = 36L;
			Action action = mock(Action.class);
			ActionRepository actionRepository = mock(ActionRepositoryImpl.class);
			when(actionRepository.find(anyLong())).thenReturn(Set.of(action));
			ActionService actionService = new ActionServiceImpl(actionRepository);

			Set<Action> actual = actionService.find(playerId);
			Set<Action> expected = Set.of(action);

			verify(actionRepository, only()).find(playerId);
			assertThat(actual).containsExactlyElementsOf(expected);
		}

		/**
		 * Проверка метода {@link ActionServiceImpl#find(long)}.
		 */
		@Test
		public void findWithNotFound() {
			long playerId = 36L;
			ActionRepository actionRepository = mock(ActionRepositoryImpl.class);
			when(actionRepository.find(anyLong())).thenReturn(Set.of());
			ActionService actionService = new ActionServiceImpl(actionRepository);

			Set<Action> actual = actionService.find(playerId);

			verify(actionRepository, only()).find(playerId);
			assertThat(actual).isEmpty();
		}
	}
}