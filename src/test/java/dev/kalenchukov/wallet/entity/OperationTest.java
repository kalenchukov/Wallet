/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import dev.kalenchukov.wallet.exceptions.NegativeAmountOperationException;
import dev.kalenchukov.wallet.resources.OperationType;
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
		Account account = mock(Account.class);
		OperationType operationType = mock(OperationType.class);
		BigDecimal amount = mock(BigDecimal.class);
		Operation operation = new Operation(account, operationType, amount);

		long actual = operation.getOperationId();

		assertThat(actual).isPositive();
	}

	/**
	 * Проверка метода {@link Operation#getAccount()}.
	 */
	@Test
	public void getAccount() {
		Account account = mock(Account.class);
		OperationType operationType = mock(OperationType.class);
		BigDecimal amount = mock(BigDecimal.class);
		Operation operation = new Operation(account, operationType, amount);

		Account actual = operation.getAccount();
		Account expected = account;

		assertThat(actual).isEqualTo(expected);
	}

	/**
	 * Проверка метода {@link Operation#getOperationType()}.
	 */
	@Test
	public void getOperationType() {
		Account account = mock(Account.class);
		OperationType operationType = mock(OperationType.class);
		BigDecimal amount = mock(BigDecimal.class);
		Operation operation = new Operation(account, operationType, amount);

		OperationType actual = operation.getOperationType();
		OperationType expected = operationType;

		assertThat(actual).isEqualTo(expected);
	}

	/**
	 * Проверка метода {@link Operation#getAmount()}.
	 */
	@Test
	public void getAmount() {
		Account account = mock(Account.class);
		OperationType operationType = mock(OperationType.class);
		BigDecimal amount = mock(BigDecimal.class);
		Operation operation = new Operation(account, operationType, amount);

		BigDecimal actual = operation.getAmount();
		BigDecimal expected = amount;

		assertThat(actual).isEqualTo(expected);
	}

	/**
	 * Проверка метода {@link Operation#toString()}.
	 */
	@Test
	public void testToString() {
		Account account = mock(Account.class);
		OperationType operationType = mock(OperationType.class);
		BigDecimal amount = mock(BigDecimal.class);
		Operation operation = new Operation(account, operationType, amount);

		String actual = operation.toString();

		assertThat(actual).isNotEmpty();
	}

	/**
	 * Класс проверки метода {@link Operation#Operation(Account, OperationType, BigDecimal)}.
	 */
	@Nested
	public class Constructor {
		/**
		 * Проверка метода {@link Operation#Operation(Account, OperationType, BigDecimal)}.
		 */
		@Test
		public void constructorWithZeroAmount() {
			Account account = mock(Account.class);
			OperationType operationType = mock(OperationType.class);

			assertThatNoException().isThrownBy(() -> {
				new Operation(account, operationType, BigDecimal.ZERO);
			});
		}

		/**
		 * Проверка метода {@link Operation#Operation(Account, OperationType, BigDecimal)}.
		 */
		@Test
		public void constructorWithPositiveAmount() {
			Account account = mock(Account.class);
			OperationType operationType = mock(OperationType.class);

			assertThatNoException().isThrownBy(() -> {
				new Operation(account, operationType, BigDecimal.ONE);
			});
		}

		/**
		 * Проверка метода {@link Operation#Operation(Account, OperationType, BigDecimal)}.
		 */
		@Test
		public void constructorWithNegativeAmount() {
			Account account = mock(Account.class);
			OperationType operationType = mock(OperationType.class);

			assertThatExceptionOfType(NegativeAmountOperationException.class).isThrownBy(() -> {
				new Operation(account, operationType, BigDecimal.valueOf(-1.50));
			});
		}

		/**
		 * Проверка метода {@link Operation#Operation(Account, OperationType, BigDecimal)} с {@code null} в качестве счёта.
		 */
		@Test
		public void constructorWithNullAccount() {
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				new Operation(null, operationType, amount);
			});
		}

		/**
		 * Проверка метода {@link Operation#Operation(Account, OperationType, BigDecimal)} с {@code null} в качестве типа операции.
		 */
		@Test
		public void constructorWithNullOperationType() {
			Account account = mock(Account.class);
			BigDecimal amount = mock(BigDecimal.class);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				new Operation(account, null, amount);
			});
		}

		/**
		 * Проверка метода {@link Operation#Operation(Account, OperationType, BigDecimal)} с {@code null} в качестве суммы.
		 */
		@Test
		public void constructorWithNullAmount() {
			Account account = mock(Account.class);
			OperationType operationType = mock(OperationType.class);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				new Operation(account, operationType, null);
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
			Account account = mock(Account.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(account, operationType, amount);
			Operation operation2 = operation1;

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link Operation#equals(Object)} с {@code null} в качестве операции.
		 */
		@Test
		public void testEqualsWithNullOperation() {
			Account account = mock(Account.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(account, operationType, amount);
			Operation operation2 = null;

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Operation#equals(Object)}.
		 */
		@Test
		public void testEqualsWithDifferentOperation() {
			Account account = mock(Account.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(account, operationType, amount);
			Operation operation2 = new Operation(account, operationType, amount);

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Operation#equals(Object)}.
		 */
		@Test
		public void testEqualsWithDifferentOperationAccount() {
			Account account1 = mock(Account.class);
			Account account2 = mock(Account.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(account1, operationType, amount);
			Operation operation2 = new Operation(account2, operationType, amount);

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Operation#equals(Object)}.
		 */
		@Test
		public void testEqualsWithDifferentOperationType() {
			Account account = mock(Account.class);
			OperationType operationType1 = mock(OperationType.class);
			OperationType operationType2 = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(account, operationType1, amount);
			Operation operation2 = new Operation(account, operationType2, amount);

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Operation#equals(Object)}.
		 */
		@Test
		public void testEqualsWithDifferentOperationAmount() {
			Account account = mock(Account.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount1 = mock(BigDecimal.class);
			BigDecimal amount2 = mock(BigDecimal.class);
			Operation operation1 = new Operation(account, operationType, amount1);
			Operation operation2 = new Operation(account, operationType, amount2);

			boolean actual = operation1.equals(operation2);

			assertThat(actual).isFalse();
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
			Account account = mock(Account.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(account, operationType, amount);
			Operation operation2 = operation1;

			int expected = operation1.hashCode();
			int actual = operation2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Operation#hashCode()}.
		 */
		@Test
		public void testHashCodeWithDifferentOperation() {
			Account account = mock(Account.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(account, operationType, amount);
			Operation operation2 = new Operation(account, operationType, amount);

			int expected = operation1.hashCode();
			int actual = operation2.hashCode();

			assertThat(actual).isNotEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Operation#hashCode()}.
		 */
		@Test
		public void testHashCodeWithDifferentOperationAccount() {
			Account account1 = mock(Account.class);
			Account account2 = mock(Account.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(account1, operationType, amount);
			Operation operation2 = new Operation(account2, operationType, amount);

			int expected = operation1.hashCode();
			int actual = operation2.hashCode();

			assertThat(actual).isNotEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Operation#hashCode()}.
		 */
		@Test
		public void testHashCodeWithDifferentOperationType() {
			Account account = mock(Account.class);
			OperationType operationType1 = mock(OperationType.class);
			OperationType operationType2 = mock(OperationType.class);
			BigDecimal amount = mock(BigDecimal.class);
			Operation operation1 = new Operation(account, operationType1, amount);
			Operation operation2 = new Operation(account, operationType2, amount);

			int expected = operation1.hashCode();
			int actual = operation2.hashCode();

			assertThat(actual).isNotEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Operation#hashCode()}.
		 */
		@Test
		public void testHashCodeWithDifferentOperationAmount() {
			Account account = mock(Account.class);
			OperationType operationType = mock(OperationType.class);
			BigDecimal amount1 = mock(BigDecimal.class);
			BigDecimal amount2 = mock(BigDecimal.class);
			Operation operation1 = new Operation(account, operationType, amount1);
			Operation operation2 = new Operation(account, operationType, amount2);

			int expected = operation1.hashCode();
			int actual = operation2.hashCode();

			assertThat(actual).isNotEqualTo(expected);
		}
	}
}