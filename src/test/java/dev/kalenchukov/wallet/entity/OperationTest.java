/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import dev.kalenchukov.wallet.type.OperationType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Класс проверки методов класса {@link Operation}.
 */
public class OperationTest {
	/**
	 * Проверка метода {@link Operation#getOperationId()}.
	 */
	@Test
	public void getOperationId() {
		long operationId = 1L;
		long playerId = 11L;
		long accountId = 46L;
		OperationType operationType = mock(OperationType.class);
		BigDecimal amount = mock(BigDecimal.class);
		Operation operation = new Operation(operationId, playerId, accountId, operationType, amount);

		long actual = operation.getOperationId();

		assertThat(actual).isPositive();
	}

	/**
	 * Проверка метода {@link Operation#getAccountId()}.
	 */
	@Test
	public void getAccountId() {
		long operationId = 1L;
		long playerId = 11L;
		long accountId = 46L;
		OperationType operationType = mock(OperationType.class);
		BigDecimal amount = mock(BigDecimal.class);
		Operation operation = new Operation(operationId, playerId, accountId, operationType, amount);

		long actual = operation.getAccountId();
		long expected = accountId;

		assertThat(actual).isEqualTo(expected);
	}

	/**
	 * Проверка метода {@link Operation#getOperationType()}.
	 */
	@Test
	public void getOperationType() {
		long operationId = 1L;
		long playerId = 11L;
		long accountId = 46L;
		OperationType operationType = mock(OperationType.class);
		BigDecimal amount = mock(BigDecimal.class);
		Operation operation = new Operation(operationId, playerId, accountId, operationType, amount);

		OperationType actual = operation.getOperationType();
		OperationType expected = operationType;

		assertThat(actual).isEqualTo(expected);
	}

	/**
	 * Проверка метода {@link Operation#getAmount()}.
	 */
	@Test
	public void getAmount() {
		long operationId = 1L;
		long playerId = 11L;
		long accountId = 46L;
		OperationType operationType = mock(OperationType.class);
		BigDecimal amount = mock(BigDecimal.class);
		Operation operation = new Operation(operationId, playerId, accountId, operationType, amount);

		BigDecimal actual = operation.getAmount();
		BigDecimal expected = amount;

		assertThat(actual).isEqualTo(expected);
	}

	/**
	 * Проверка метода {@link Operation#toString()}.
	 */
	@Test
	public void testToString() {
		long operationId = 1L;
		long playerId = 11L;
		long accountId = 46L;
		OperationType operationType = mock(OperationType.class);
		BigDecimal amount = mock(BigDecimal.class);
		Operation operation = new Operation(operationId, playerId, accountId, operationType, amount);

		String actual = operation.toString();

		assertThat(actual).isNotEmpty();
	}

	/**
	 * Класс проверки метода {@link Operation#Operation(long, long, long, OperationType, BigDecimal)}.
	 */
	@Nested
	public class Constructor {
		/**
		 * Проверка метода {@link Operation#Operation(long, long, long, OperationType, BigDecimal)}.
		 */
		@Test
		public void constructor() {
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);

			assertThatNoException().isThrownBy(() -> {
				new Operation(playerId, accountId, operationType, BigDecimal.ONE);
			});
		}

		/**
		 * Проверка метода {@link Operation#Operation(long, long, long, OperationType, BigDecimal)} со всеми аргументами.
		 */
		@Test
		public void constructorWithAllArgs() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);

			assertThatNoException().isThrownBy(() -> {
				new Operation(operationId, playerId, accountId, operationType, BigDecimal.ONE);
			});
		}

		/**
		 * Проверка метода {@link Operation#Operation(long, long, long, OperationType, BigDecimal)} с нулевой суммой.
		 */
		@Test
		public void constructorWithZeroAmount() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);

			assertThatNoException().isThrownBy(() -> {
				new Operation(operationId, playerId, accountId, operationType, BigDecimal.ZERO);
			});
		}

		/**
		 * Проверка метода {@link Operation#Operation(long, long, long, OperationType, BigDecimal)} с отрицательной суммой.
		 */
		@Test
		public void constructorWithNegativeAmount() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);

			assertThatNoException().isThrownBy(() -> {
				new Operation(operationId, playerId, accountId, operationType, BigDecimal.valueOf(-1.50));
			});
		}

		/**
		 * Проверка метода {@link Operation#Operation(long, long, long, OperationType, BigDecimal)} с {@code null} в качестве типа операции.
		 */
		@Test
		public void constructorWithNullOperationType() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			BigDecimal amount = mock(BigDecimal.class);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				new Operation(operationId, playerId, accountId, null, amount);
			});
		}

		/**
		 * Проверка метода {@link Operation#Operation(long, long, long, OperationType, BigDecimal)} с {@code null} в качестве суммы.
		 */
		@Test
		public void constructorWithNullAmount() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				new Operation(operationId, playerId, accountId, operationType, null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link Operation#equals(Object)}.
	 */
	@Nested
	public class Equals {
		/**
		 * Проверка метода {@link Operation#equals(Object)}.
		 */
		@Test
		public void testEquals() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId, playerId, accountId, operationType, amount);
			Operation operation2 = operation1;

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link Operation#equals(Object)} с {@code null} в качестве операции.
		 */
		@Test
		public void testEqualsWithNull() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId, playerId, accountId, operationType, amount);
			Operation operation2 = null;

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Operation#equals(Object)} с разными по идентификатору операции классами.
		 */
		@Test
		public void testEqualsWithDifferentOperationId() {
			long operationId1 = 1L;
			long playerId = 11L;
			long operationId2 = 14L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId1, playerId, accountId, operationType, amount);
			Operation operation2 = new Operation(operationId2, playerId, accountId, operationType, amount);

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Operation#equals(Object)} с разными по идентификатору игрока классами.
		 */
		@Test
		public void testEqualsWithDifferentPlayerId() {
			long operationId = 1L;
			long playerId1 = 11L;
			long playerId2 = 54564L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId, playerId1, accountId, operationType, amount);
			Operation operation2 = new Operation(operationId, playerId2, accountId, operationType, amount);

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link Operation#equals(Object)} с разными по идентификатору счёта классами.
		 */
		@Test
		public void testEqualsWithDifferentAccountId() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId1 = 46L;
			long accountId2 = 666L;
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId, playerId, accountId1, operationType, amount);
			Operation operation2 = new Operation(operationId, playerId, accountId2, operationType, amount);

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link Operation#equals(Object)} с разными по типу операции классами.
		 */
		@Test
		public void testEqualsWithDifferentOperationType() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType1 = mock(OperationType.class);
			OperationType operationType2 = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId, playerId, accountId, operationType1, amount);
			Operation operation2 = new Operation(operationId, playerId, accountId, operationType2, amount);

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link Operation#equals(Object)} с разными по сумме операции классами.
		 */
		@Test
		public void testEqualsWithDifferentAmount() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount1 = mock(BigDecimal.class);
			BigDecimal amount2 = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId, playerId, accountId, operationType, amount1);
			Operation operation2 = new Operation(operationId, playerId, accountId, operationType, amount2);

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isTrue();
		}
	}

	/**
	 * Класс проверки метода {@link Operation#hashCode()}.
	 */
	@Nested
	public class HashCode {
		/**
		 * Проверка метода {@link Operation#hashCode()}.
		 */
		@Test
		public void testHashCode() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId, playerId, accountId, operationType, amount);
			Operation operation2 = operation1;

			int expected = operation1.hashCode();
			int actual = operation2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Operation#hashCode()} с разными по идентификатору операции классами.
		 */
		@Test
		public void testHashCodeWithDifferentOperationId() {
			long operationId1 = 1L;
			long operationId2 = 17L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId1, playerId, accountId, operationType, amount);
			Operation operation2 = new Operation(operationId2, playerId, accountId, operationType, amount);

			int expected = operation1.hashCode();
			int actual = operation2.hashCode();

			assertThat(actual).isNotEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Operation#hashCode()} с разными по идентификатору счёта классами.
		 */
		@Test
		public void testHashCodeWithDifferentAccountId() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId1 = 46L;
			long accountId2 = 666L;
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId, playerId, accountId1, operationType, amount);
			Operation operation2 = new Operation(operationId, playerId, accountId2, operationType, amount);

			int expected = operation1.hashCode();
			int actual = operation2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Operation#hashCode()} с разными по типу операции классами.
		 */
		@Test
		public void testHashCodeWithDifferentOperationType() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType1 = mock(OperationType.class);
			OperationType operationType2 = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId, playerId, accountId, operationType1, amount);
			Operation operation2 = new Operation(operationId, playerId, accountId, operationType2, amount);

			int expected = operation1.hashCode();
			int actual = operation2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Operation#hashCode()} с разными по сумме операции классами.
		 */
		@Test
		public void testHashCodeWithDifferentAmount() {
			long operationId = 1L;
			long playerId = 11L;
			long accountId = 46L;
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount1 = mock(BigDecimal.class);
			BigDecimal amount2 = mock(BigDecimal.class);
			Operation operation1 = new Operation(operationId, playerId, accountId, operationType, amount1);
			Operation operation2 = new Operation(operationId, playerId, accountId, operationType, amount2);

			int expected = operation1.hashCode();
			int actual = operation2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}
	}
}