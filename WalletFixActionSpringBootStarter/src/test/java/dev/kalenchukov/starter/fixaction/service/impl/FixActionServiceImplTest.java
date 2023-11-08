/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.starter.fixaction.service.impl;

import dev.kalenchukov.starter.fixaction.entity.Action;
import dev.kalenchukov.starter.fixaction.repository.FixActionRepository;
import dev.kalenchukov.starter.fixaction.repository.impl.FixActionRepositoryImpl;
import dev.kalenchukov.starter.fixaction.service.FixActionService;
import dev.kalenchukov.starter.fixaction.types.ActionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class FixActionServiceImplTest {
	private FixActionRepository fixActionRepository;

	@BeforeEach
	public void beforeEach() {
		this.fixActionRepository = mock(FixActionRepositoryImpl.class);
	}

	@Nested
	public class Add {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void addValid() {
			long playerId = 36L;
			Action action = mock(Action.class);
			ActionType actionType = mock(ActionType.class);
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
			when(fixActionRepository.save(any(Action.class))).thenReturn(action);
			FixActionService fixActionService = new FixActionServiceImpl(fixActionRepository);

			Action actualAction = fixActionService.add(playerId, actionType, actionTypeStatus);

			assertThat(actualAction).isEqualTo(action);
			verify(fixActionRepository, only()).save(any(Action.class));
		}

		@DisplayName("Проверка с null в качестве типа действия.")
		@Test
		public void addWithNullActionType() {
			long playerId = 36L;
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
			FixActionService fixActionService = new FixActionServiceImpl(fixActionRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				fixActionService.add(playerId, null, actionTypeStatus);
			});
		}

		@DisplayName("Проверка с null в качестве статуса действия.")
		@Test
		public void addWithNullActionTypeStatus() {
			long playerId = 36L;
			ActionType actionType = mock(ActionType.class);
			FixActionService fixActionService = new FixActionServiceImpl(fixActionRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				fixActionService.add(playerId, actionType, null);
			});
		}
	}
}