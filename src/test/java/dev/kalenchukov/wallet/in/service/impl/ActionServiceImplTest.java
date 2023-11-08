/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.starter.fixaction.entity.Action;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.repository.ActionRepository;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ActionServiceImplTest {
	private ActionRepository actionRepository;

	@BeforeEach
	public void beforeEach() {
		this.actionRepository = mock(ActionRepositoryImpl.class);
	}

	@Nested
	public class Find {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void findValid() {
			long playerId = 36L;
			Action action = mock(Action.class);
			when(actionRepository.find(anyLong())).thenReturn(List.of(action));
			ActionService actionService = new ActionServiceImpl(actionRepository);

			List<Action> actual = actionService.find(playerId);
			List<Action> expected = List.of(action);

			verify(actionRepository, only()).find(playerId);
			assertThat(actual).containsExactlyElementsOf(expected);
		}

		@DisplayName("Проверка с отсутствующими действиями.")
		@Test
		public void findWithNotFound() {
			long playerId = 3456456L;
			when(actionRepository.find(anyLong())).thenReturn(Collections.emptyList());
			ActionService actionService = new ActionServiceImpl(actionRepository);

			List<Action> actual = actionService.find(playerId);

			verify(actionRepository, only()).find(playerId);
			assertThat(actual).isEmpty();
		}
	}
}