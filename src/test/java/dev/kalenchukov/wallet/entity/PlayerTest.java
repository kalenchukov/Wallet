/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.entity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Класс проверки методов класса {@link Player}.
 */
public class PlayerTest {
	/**
	 * Проверка метода {@link Player#getPlayerId()}.
	 */
	@Test
	public void getPlayerId() {
		long playerId = 1L;
		String name = "Имя";
		String password = "d41d8cd98f00b204e9800998ecf8427e";
		Player player = new Player(playerId, name, password);

		long actual = player.getPlayerId();

		assertThat(actual).isPositive();
	}

	/**
	 * Проверка метода {@link Player#getName()}.
	 */
	@Test
	public void getName() {
		long playerId = 1L;
		String name = "Имя";
		String password = "d41d8cd98f00b204e9800998ecf8427e";
		Player player = new Player(playerId, name, password);

		String actual = player.getName();
		String expected = "Имя";

		assertThat(actual).isEqualTo(expected);
	}

	/**
	 * Проверка метода {@link Player#getPassword()}.
	 */
	@Test
	public void getPassword() {
		long playerId = 1L;
		String name = "Имя";
		String password = "d41d8cd98f00b204e9800998ecf8427e";
		Player player = new Player(playerId, name, password);

		String actual = player.getPassword();

		assertThat(actual).containsPattern("[a-z0-9]{32}");
	}

	/**
	 * Проверка метода {@link Player#toString()}.
	 */
	@Test
	public void testToString() {
		long playerId = 1L;
		String name = "Имя";
		String password = "d41d8cd98f00b204e9800998ecf8427e";
		Player player = new Player(playerId, name, password);

		String actual = player.toString();

		assertThat(actual).isNotEmpty();
	}

	/**
	 * Класс проверки метода {@link Player#Player(long, String, String)}.
	 */
	@Nested
	public class Constructor {
		/**
		 * Проверка метода {@link Player#Player(long, String, String)}.
		 */
		@Test
		public void constructor() {
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			assertThatNoException().isThrownBy(() -> {
				new Player(name, password);
			});
		}

		/**
		 * Проверка метода {@link Player#Player(long, String, String)} со всеми аргументами.
		 */
		@Test
		public void constructorWithAllArgs() {
			long playerId = 1L;
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			assertThatNoException().isThrownBy(() -> {
				new Player(playerId, name, password);
			});
		}

		/**
		 * Проверка метода {@link Player#Player(long, String, String)} с {@code null} в качестве имени.
		 */
		@Test
		public void constructorWithNullName() {
			long playerId = 1L;
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				new Player(playerId, null, password);
			});
		}

		/**
		 * Проверка метода {@link Player#Player(long, String, String)} с {@code null} в качестве пароля.
		 */
		@Test
		public void constructorWithNullPassword() {
			long playerId = 1L;
			String name = "Имя";
			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				new Player(playerId, name, null);
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
			long playerId = 1L;
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			Player player1 = new Player(playerId, name, password);
			Player player2 = player1;

			boolean actual = player1.equals(player2);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link Player#equals(Object)} с {@code null} в качестве игрока.
		 */
		@Test
		public void testEqualsWithNull() {
			long playerId = 1L;
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			Player player1 = new Player(playerId, name, password);
			Player player2 = null;

			boolean actual = player1.equals(player2);

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link Player#equals(Object)} с разными по имени игрока классами.
		 */
		@Test
		public void testEqualsWithDifferentName() {
			long playerId = 1L;
			String name1 = "Имя1";
			String name2 = "Имя2";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			Player player1 = new Player(playerId, name1, password);
			Player player2 = new Player(playerId, name2, password);

			boolean actual = player1.equals(player2);

			assertThat(actual).isTrue();
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
			long playerId = 1L;
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			Player player1 = new Player(playerId, name, password);
			Player player2 = player1;

			int expected = player1.hashCode();
			int actual = player2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}

		/**
		 * Проверка метода {@link Player#hashCode()} с разными по имени игрока классами.
		 */
		@Test
		public void testHashCodeWithDifferentName() {
			long playerId = 1L;
			String name1 = "Имя1";
			String name2 = "Имя2";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			Player player1 = new Player(playerId, name1, password);
			Player player2 = new Player(playerId, name2, password);

			int expected = player1.hashCode();
			int actual = player2.hashCode();

			assertThat(actual).isEqualTo(expected);
		}
	}
}