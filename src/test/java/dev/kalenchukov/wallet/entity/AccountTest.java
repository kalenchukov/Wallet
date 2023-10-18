/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Класс проверки методов класса {@link Account}.
 */
public class AccountTest {
	/**
	 * Проверка метода {@link Account#getAccountId()}.
	 */
	@Test
	public void getAccountId() {
		long accountId = 1L;
		long playerId = 24L;
		BigDecimal amount = BigDecimal.TEN;
		Account account = new Account(accountId, playerId, amount);

		long actual = account.getAccountId();

		assertThat(actual).isPositive();
	}

	/**
	 * Проверка метода {@link Account#getAmount()}.
	 */
	@Test
	public void getAmount() {
		long accountId = 1L;
		long playerId = 24L;
		BigDecimal amount = BigDecimal.TEN;
		Account account = new Account(accountId, playerId, amount);

		BigDecimal actual = account.getAmount();
		BigDecimal expected = BigDecimal.TEN;

		assertThat(actual).isEqualTo(expected);
	}

	/**
	 * Проверка метода {@link Account#getPlayerId()}.
	 */
	@Test
	public void getPlayerId() {
		long accountId = 1L;
		long playerId = 24L;
		BigDecimal amount = BigDecimal.TEN;
		Account account = new Account(accountId, playerId, amount);

		long actual = account.getPlayerId();

		assertThat(actual).isEqualTo(playerId);
	}

	/**
	 * Проверка метода {@link Account#toString()}.
	 */
	@Test
	public void testToString() {
		long accountId = 1L;
		long playerId = 24L;
		BigDecimal amount = BigDecimal.TEN;
		Account account = new Account(accountId, playerId, amount);

		String actual = account.toString();

		assertThat(actual).isNotEmpty();
	}

	/**
	 * Класс проверки метода {@link Account#Account(long, long, BigDecimal)}.
	 */
	@Nested
	public class Constructor {
		/**
		 * Проверка метода {@link Account#Account(long, long, BigDecimal)}.
		 */
		@Test
		public void constructor() {
			long playerId = 24L;
			BigDecimal amount = BigDecimal.TEN;

			assertThatNoException().isThrownBy(() -> {
				new Account(playerId, amount);
			});
		}

		/**
		 * Проверка метода {@link Account#Account(long, long, BigDecimal)} со всеми аргументами.
		 */
		@Test
		public void constructorWithAllArgs() {
			long accountId = 1L;
			long playerId = 24L;
			BigDecimal amount = BigDecimal.TEN;

			assertThatNoException().isThrownBy(() -> {
				new Account(accountId, playerId, amount);
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
			long accountId = 1L;
			long playerId = 24L;
			BigDecimal amount = BigDecimal.TEN;
			Account account1 = new Account(accountId, playerId, amount);
			Account account2 = account1;

			boolean actual = account1.equals(account2);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link Account#equals(Object)} с {@code null} в качестве счёта.
		 */
		@Test
		public void testEqualsWithNull() {
			long accountId = 1L;
			long playerId = 24L;
			BigDecimal amount = BigDecimal.TEN;
			Account account1 = new Account(accountId, playerId, amount);
			Account account2 = null;

			boolean actual = account1.equals(account2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Account#equals(Object)} с разными по идентификатору счёта классами.
		 */
		@Test
		public void testEqualsWithDifferentAccountId() {
			long accountId1 = 1L;
			long accountId2 = 19L;
			long playerId = 24L;
			BigDecimal amount = BigDecimal.TEN;
			Account account1 = new Account(accountId1, playerId, amount);
			Account account2 = new Account(accountId2, playerId, amount);

			boolean actual = account1.equals(account2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Account#equals(Object)} с разными по сумме счёта классами.
		 */
		@Test
		public void testEqualsWithDifferentAmount() {
			long accountId = 1L;
			long playerId = 24L;
			BigDecimal amount1 = BigDecimal.TEN;
			BigDecimal amount2 = BigDecimal.TEN;
			Account account1 = new Account(accountId, playerId, amount1);
			Account account2 = new Account(accountId, playerId, amount2);

			boolean actual = account1.equals(account2);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link Account#equals(Object)} с разными по идентификатору игрока классами.
		 */
		@Test
		public void testEqualsWithDifferentPlayerId() {
			long accountId = 1L;
			long playerId1 = 24L;
			long playerId2 = 81L;
			BigDecimal amount = BigDecimal.TEN;
			Account account1 = new Account(accountId, playerId1, amount);
			Account account2 = new Account(accountId, playerId2, amount);

			boolean actual = account1.equals(account2);

			assertThat(actual).isTrue();
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
			long accountId = 1L;
			long playerId = 24L;
			BigDecimal amount = BigDecimal.TEN;
			Account account1 = new Account(accountId, playerId, amount);
			Account account2 = account1;

			int expected = account1.hashCode();
			int actual = account2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Account#hashCode()} с разными по идентификатору счёта классами.
		 */
		@Test
		public void testHashCodeWithDifferentAccountId() {
			long accountId1 = 1L;
			long accountId2 = 19L;
			long playerId = 24L;
			BigDecimal amount = BigDecimal.TEN;
			Account account1 = new Account(accountId1, playerId, amount);
			Account account2 = new Account(accountId2, playerId, amount);

			int expected = account1.hashCode();
			int actual = account2.hashCode();

			assertThat(actual).isNotEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Account#hashCode()} с разными по идентификатору игрока классами.
		 */
		@Test
		public void testHashCodeWithDifferentPlayerId() {
			long accountId = 1L;
			long playerId1 = 24L;
			long playerId2 = 753L;
			BigDecimal amount = BigDecimal.TEN;
			Account account1 = new Account(accountId, playerId1, amount);
			Account account2 = new Account(accountId, playerId2, amount);

			int expected = account1.hashCode();
			int actual = account2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Account#hashCode()} с разными по сумме счёта классами.
		 */
		@Test
		public void testHashCodeWithDifferentAmount() {
			long accountId = 1L;
			long playerId = 24L;
			BigDecimal amount1 = BigDecimal.TEN;
			BigDecimal amount2 = BigDecimal.TEN;
			Account account1 = new Account(accountId, playerId, amount1);
			Account account2 = new Account(accountId, playerId, amount2);

			int expected = account1.hashCode();
			int actual = account2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}
	}
}