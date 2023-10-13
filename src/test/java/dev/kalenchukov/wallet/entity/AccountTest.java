/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import dev.kalenchukov.wallet.exceptions.NegativeAmountOperationException;
import dev.kalenchukov.wallet.exceptions.OutOfAmountOperationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Класс проверки методов класса {@link Account}.
 */
public class AccountTest {
	/**
	 * Проверка метода {@link Account#getAccountId()}.
	 */
	@Test
	public void getAccountId() {
		long playerId = 24L;
		Account account = new Account(playerId);

		long actual = account.getAccountId();

		assertThat(actual).isPositive();
	}

	/**
	 * Проверка метода {@link Account#getAmount()}.
	 */
	@Test
	public void getAmount() {
		long playerId = 24L;
		Account account = new Account(playerId);

		BigDecimal actual = account.getAmount();
		BigDecimal expected = BigDecimal.ZERO;

		assertThat(actual).isEqualTo(expected);
	}

	/**
	 * Проверка метода {@link Account#getPlayerId()}.
	 */
	@Test
	public void getPlayerId() {
		long playerId = 24L;
		Account account = new Account(playerId);

		long actual = account.getPlayerId();

		assertThat(actual).isEqualTo(playerId);
	}

	/**
	 * Проверка метода {@link Account#toString()}.
	 */
	@Test
	public void testToString() {
		long playerId = 24L;
		Account account = new Account(playerId);

		String actual = account.toString();

		assertThat(actual).isNotEmpty();
	}

	/**
	 * Класс проверки метода {@link Account#Account(long)}.
	 */
	@Nested
	public class Constructor {
		/**
		 * Проверка метода {@link Account#Account(long)}.
		 */
		@Test
		public void constructorWithValidPlayer() {
			long playerId = 24L;

			assertThatNoException().isThrownBy(() -> {
				new Account(playerId);
			});
		}
	}

	/**
	 * Класс проверки метода {@link Account#credit(BigDecimal)}.
	 */
	@Nested
	public class Credit {
		/**
		 * Проверка метода {@link Account#credit(BigDecimal)}.
		 */
		@Test
		public void credit() {
			long playerId = 24L;
			Account account = new Account(playerId);
			BigDecimal amount = mock(BigDecimal.class);

			account.credit(amount);

			verify(amount, times(1)).add(any(BigDecimal.class));
		}

		/**
		 * Проверка метода {@link Account#credit(BigDecimal)} с {@code null} в качестве суммы.
		 */
		@Test
		public void creditWithNullAmount() {
			long playerId = 24L;
			Account account = new Account(playerId);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				account.credit(null);
			});
		}

		/**
		 * Проверка метода {@link Account#credit(BigDecimal)}.
		 */
		@Test
		public void creditWithNegativeAmount() {
			long playerId = 24L;
			Account account = new Account(playerId);
			BigDecimal amount = BigDecimal.valueOf(-99.9);

			assertThatExceptionOfType(NegativeAmountOperationException.class).isThrownBy(() -> {
				account.credit(amount);
			});
		}
	}

	/**
	 * Класс проверки метода {@link Account#debit(BigDecimal)}.
	 */
	@Nested
	public class Debit {
		/**
		 * Проверка метода {@link Account#debit(BigDecimal)}.
		 */
		@Test
		public void debit() {
			long playerId = 24L;
			Account account = new Account(playerId);
			BigDecimal amount1 = mock(BigDecimal.class);
			BigDecimal amount2 = mock(BigDecimal.class);
			when(amount1.subtract(any(BigDecimal.class))).thenReturn(amount2);
			when(amount2.abs()).thenReturn(BigDecimal.TEN);

			account.debit(amount1);

			verify(amount1, times(1)).subtract(any(BigDecimal.class));
			verify(amount2, times(1)).abs();
		}

		/**
		 * Проверка метода {@link Account#debit(BigDecimal)} с {@code null} в качестве суммы.
		 */
		@Test
		public void creditWithNullAmount() {
			long playerId = 24L;
			Account account = new Account(playerId);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				account.debit(null);
			});
		}

		/**
		 * Проверка метода {@link Account#debit(BigDecimal)}.
		 */
		@Test
		public void debitWithNegativeAmount() {
			long playerId = 24L;
			Account account = new Account(playerId);
			BigDecimal amount = BigDecimal.valueOf(-99.9);

			assertThatExceptionOfType(NegativeAmountOperationException.class).isThrownBy(() -> {
				account.debit(amount);
			});
		}

		/**
		 * Проверка метода {@link Account#debit(BigDecimal)}.
		 */
		@Test
		public void debitWithOutOfAmount() {
			long playerId = 24L;
			Account account = new Account(playerId);
			BigDecimal amount = BigDecimal.valueOf(7.77);

			assertThatExceptionOfType(OutOfAmountOperationException.class).isThrownBy(() -> {
				account.debit(amount);
			});
		}
	}

	/**
	 * Класс проверки метода {@link Account#equals(Object)}.
	 */
	@Nested
	public class Equals {
		/**
		 * Проверка метода {@link Account#equals(Object)}.
		 */
		@Test
		public void testEquals() {
			long playerId = 24L;
			Account account1 = new Account(playerId);
			Account account2 = account1;

			boolean actual = account1.equals(account2);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link Account#equals(Object)} с {@code null} в качестве счёта.
		 */
		@Test
		public void testEqualsWithNullAccount() {
			long playerId = 24L;
			Account account1 = new Account(playerId);
			Account account2 = null;

			boolean actual = account1.equals(account2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Account#equals(Object)}.
		 */
		@Test
		public void testEqualsWithDifferentAccount() {
			long playerId1 = 24L;
			long playerId2 = 753L;
			Account account1 = new Account(playerId1);
			Account account2 = new Account(playerId2);

			boolean actual = account1.equals(account2);

			assertThat(actual).isFalse();
		}
	}

	/**
	 * Класс проверки метода {@link Account#hashCode()}.
	 */
	@Nested
	public class HashCode {
		/**
		 * Проверка метода {@link Account#hashCode()}.
		 */
		@Test
		public void testHashCode() {
			long playerId = 24L;
			Account account1 = new Account(playerId);
			Account account2 = account1;

			int expected = account1.hashCode();
			int actual = account2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Account#hashCode()}.
		 */
		@Test
		public void testHashCodeWithDifferentAccount() {
			long playerId = 24L;
			Account account1 = new Account(playerId);
			Account account2 = new Account(playerId);

			int expected = account1.hashCode();
			int actual = account2.hashCode();

			assertThat(actual).isNotEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Account#hashCode()}.
		 */
		@Test
		public void testHashCodeWithDifferentAccountPlayer() {
			long playerId1 = 24L;
			long playerId2 = 753L;
			Account account1 = new Account(playerId1);
			Account account2 = new Account(playerId2);

			int expected = account1.hashCode();
			int actual = account2.hashCode();

			assertThat(actual).isNotEqualTo(expected);
		}
	}
}