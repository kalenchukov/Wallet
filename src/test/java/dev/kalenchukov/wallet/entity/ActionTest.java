/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import dev.kalenchukov.wallet.resources.ActionType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Класс проверки методов класса {@link Action}.
 */
public class ActionTest {
	/**
	 * Проверка метода {@link Action#getPlayerId()}.
	 */
	@Test
	public void getPlayerId() {
		long playerId = 99L;
		ActionType actionType = mock(ActionType.class);
		ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
		Action action = new Action(playerId, actionType, actionTypeStatus);

		long actual = action.getPlayerId();

		assertThat(actual).isEqualTo(playerId);
	}

	/**
	 * Проверка метода {@link Action#getActionType()}.
	 */
	@Test
	public void getActionType() {
		long playerId = 99L;
		ActionType actionType = mock(ActionType.class);
		ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
		Action action = new Action(playerId, actionType, actionTypeStatus);

		ActionType actual = action.getActionType();

		assertThat(actual).isEqualTo(actionType);
	}

	/**
	 * Проверка метода {@link Action#toString()}.
	 */
	@Test
	public void testToString() {
		long playerId = 99L;
		ActionType actionType = mock(ActionType.class);
		ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
		Action action = new Action(playerId, actionType, actionTypeStatus);

		String actual = action.toString();

		assertThat(actual).isNotEmpty();
	}

	/**
	 * Класс проверки метода {@link Action#Action(long, ActionType, ActionType.Status)}.
	 */
	@Nested
	public class Constructor {
		/**
		 * Проверка метода {@link Action#Action(long, ActionType, ActionType.Status)}.
		 */
		@Test
		public void constructorWithValidPlayerAndActionType() {
			long playerId = 99L;
			ActionType actionType = mock(ActionType.class);
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);

			assertThatNoException().isThrownBy(() -> {
				new Action(playerId, actionType, actionTypeStatus);
			});
		}

		/**
		 * Проверка метода {@link Action#Action(long, ActionType, ActionType.Status)} с {@code null} в качестве типа действия.
		 */
		@Test
		public void constructorWithNullActionType() {
			long playerId = 99L;
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				new Action(playerId, null, actionTypeStatus);
			});
		}

		/**
		 * Проверка метода {@link Action#Action(long, ActionType, ActionType.Status)} с {@code null} в качестве статуса действия.
		 */
		@Test
		public void constructorWithNullActionTypeStatus() {
			long playerId = 99L;
			ActionType actionType = mock(ActionType.class);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				new Action(playerId, actionType, null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link Action#equals(Object)}.
	 */
	@Nested
	public class Equals {
		/**
		 * Проверка метода {@link Action#equals(Object)}.
		 */
		@Test
		public void testEquals() {
			long playerId = 99L;
			ActionType actionType = mock(ActionType.class);
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
			Action action1 = new Action(playerId, actionType, actionTypeStatus);
			Action action2 = action1;

			boolean actual = action1.equals(action2);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link Action#equals(Object)} с {@code null} в качестве действия.
		 */
		@Test
		public void testEqualsWithNullAction() {
			long playerId = 99L;
			ActionType actionType = mock(ActionType.class);
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
			Action action1 = new Action(playerId, actionType, actionTypeStatus);
			Action action2 = null;

			boolean actual = action1.equals(action2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Action#equals(Object)}.
		 */
		@Test
		public void testEqualsWithDifferentAction() {
			long playerId1 = 99L;
			long playerId2 = 111L;
			ActionType actionType = mock(ActionType.class);
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
			Action action1 = new Action(playerId1, actionType, actionTypeStatus);
			Action action2 = new Action(playerId2, actionType, actionTypeStatus);

			boolean actual = action1.equals(action2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Action#equals(Object)}.
		 */
		@Test
		public void testEqualsWithDifferentActionType() {
			long playerId = 99L;
			ActionType type1 = mock(ActionType.class);
			ActionType type2 = mock(ActionType.class);
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
			Action action1 = new Action(playerId, type1, actionTypeStatus);
			Action action2 = new Action(playerId, type2, actionTypeStatus);

			boolean actual = action1.equals(action2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Action#equals(Object)}.
		 */
		@Test
		public void testEqualsWithDifferentActionTypeStatus() {
			long playerId = 99L;
			ActionType type = mock(ActionType.class);
			ActionType.Status actionTypeStatus1 = mock(ActionType.Status.class);
			ActionType.Status actionTypeStatus2 = mock(ActionType.Status.class);
			Action action1 = new Action(playerId, type, actionTypeStatus1);
			Action action2 = new Action(playerId, type, actionTypeStatus2);

			boolean actual = action1.equals(action2);

			assertThat(actual).isFalse();
		}
	}

	/**
	 * Класс проверки метода {@link Action#hashCode()}.
	 */
	@Nested
	public class HashCode {
		/**
		 * Проверка метода {@link Action#hashCode()}.
		 */
		@Test
		public void testHashCode() {
			long playerId = 99L;
			ActionType actionType = mock(ActionType.class);
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
			Action action1 = new Action(playerId, actionType, actionTypeStatus);
			Action action2 = action1;

			int expected = action1.hashCode();
			int actual = action2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Action#hashCode()}.
		 */
		@Test
		public void testHashCodeWithDifferentAction() {
			long playerId1 = 99L;
			long playerId2 = 111L;
			ActionType actionType = mock(ActionType.class);
			ActionType.Status actionTypeStatus = mock(ActionType.Status.class);
			Action action1 = new Action(playerId1, actionType, actionTypeStatus);
			Action action2 = new Action(playerId2, actionType, actionTypeStatus);

			int expected = action1.hashCode();
			int actual = action2.hashCode();

			assertThat(actual).isNotEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Action#hashCode()}.
		 */
		@Test
		public void testHashCodeWithDifferentActionType() {
			long playerId = 99L;
			ActionType type = mock(ActionType.class);
			ActionType.Status actionTypeStatus1 = mock(ActionType.Status.class);
			ActionType.Status actionTypeStatus2 = mock(ActionType.Status.class);
			Action action1 = new Action(playerId, type, actionTypeStatus1);
			Action action2 = new Action(playerId, type, actionTypeStatus2);

			int expected = action1.hashCode();
			int actual = action2.hashCode();

			assertThat(actual).isNotEqualTo(expected);
		}
	}
}