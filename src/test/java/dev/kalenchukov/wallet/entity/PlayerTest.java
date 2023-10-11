/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import dev.kalenchukov.wallet.exceptions.EmptyNamePlayerException;
import dev.kalenchukov.wallet.exceptions.EmptyPasswordPlayerException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Класс проверки методов класса {@link Player}.
 */
public class PlayerTest {
	/**
	 * Проверка метода {@link Player#getPlayerId()}.
	 */
	@Test
	public void getPlayerId() {
		Player player = new Player("Имя", "pa$$w0rd");

		long actual = player.getPlayerId();

		assertThat(actual).isPositive();
	}

	/**
	 * Проверка метода {@link Player#getName()}.
	 */
	@Test
	public void getName() {
		Player player = new Player("Имя", "pa$$w0rd");

		String actual = player.getName();
		String expected = "Имя";

		assertThat(actual).isEqualTo(expected);
	}

	/**
	 * Проверка метода {@link Player#getPassword()}.
	 */
	@Test
	public void getPassword() {
		Player player = new Player("Имя", "pa$$w0rd");

		String actual = player.getPassword();

		assertThat(actual).containsPattern("[a-z0-9]{32}");
	}

	/**
	 * Проверка метода {@link Player#toString()}.
	 */
	@Test
	public void testToString() {
		Player player = new Player("Имя", "pa$$w0rd");

		String actual = player.toString();

		assertThat(actual).isNotEmpty();
	}

	/**
	 * Класс проверки метода {@link Player#Player(String, String)}.
	 */
	@Nested
	public class Constructor {
		/**
		 * Проверка метода {@link Player#Player(String, String)}.
		 */
		@Test
		public void constructorWithValidNameAndPassword() {
			assertThatNoException().isThrownBy(() -> {
				new Player("Имя", "pa$$w0rd");
			});
		}

		/**
		 * Проверка метода {@link Player#Player(String, String)} с {@code null} в качестве имени.
		 */
		@Test
		public void constructorWithNullName() {
			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				new Player(null, "pa$$w0rd");
			});
		}

		/**
		 * Проверка метода {@link Player#Player(String, String)}.
		 */
		@Test
		public void constructorWithEmptyName() {
			assertThatExceptionOfType(EmptyNamePlayerException.class).isThrownBy(() -> {
				new Player("", "pa$$w0rd");
			});
		}

		/**
		 * Проверка метода {@link Player#Player(String, String)} с {@code null} в качестве пароля.
		 */
		@Test
		public void constructorWithNullPassword() {
			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				new Player("Имя", null);
			});
		}

		/**
		 * Проверка метода {@link Player#Player(String, String)}.
		 */
		@Test
		public void constructorWithEmptyPassword() {
			assertThatExceptionOfType(EmptyPasswordPlayerException.class).isThrownBy(() -> {
				new Player("Имя", "");
			});
		}
	}

	/**
	 * Класс проверки метода {@link Player#equals(Object)}.
	 */
	@Nested
	public class Equals {
		/**
		 * Проверка метода {@link Player#equals(Object)}.
		 */
		@Test
		public void testEquals() {
			Player player1 = new Player("Имя", "pa$$w0rd");
			Player player2 = player1;

			boolean actual = player1.equals(player2);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link Player#equals(Object)} с {@code null} в качестве игрока.
		 */
		@Test
		public void testEqualsWithNullPlayer() {
			Player player1 = new Player("Имя", "pa$$w0rd");
			Player player2 = null;

			boolean actual = player1.equals(player2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Player#equals(Object)}.
		 */
		@Test
		public void testEqualsWithDifferentPlayerName() {
			Player player1 = new Player("Имя1", "pa$$w0rd");
			Player player2 = new Player("Имя2", "pa$$w0rd");

			boolean actual = player1.equals(player2);

			assertThat(actual).isFalse();
		}
	}

	/**
	 * Класс проверки метода {@link Player#hashCode()}.
	 */
	@Nested
	public class HashCode {
		/**
		 * Проверка метода {@link Player#hashCode()}.
		 */
		@Test
		public void testHashCode() {
			Player player1 = new Player("Имя", "pa$$w0rd");
			Player player2 = player1;

			int expected = player1.hashCode();
			int actual = player2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Player#hashCode()}.
		 */
		@Test
		public void testHashCodeWithDifferentPlayerName() {
			Player player1 = new Player("Имя1", "pa$$w0rd");
			Player player2 = new Player("Имя2", "pa$$w0rd");

			int expected = player1.hashCode();
			int actual = player2.hashCode();

			assertThat(actual).isNotEqualTo(expected);
		}
	}
}