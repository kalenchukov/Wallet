/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Player;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * Класс проверки методов класса {@link PlayerRepositoryImpl}.
 */
public class PlayerRepositoryImplTest {
	/**
	 * Класс проверки метода {@link PlayerRepositoryImpl#save(Player)}.
	 */
	@Nested
	public class Save {
		/**
		 * Проверка метода {@link PlayerRepositoryImpl#save(Player)}.
		 */
		@Test
		public void save() throws NoSuchFieldException, IllegalAccessException {
			Set<Player> data = spy(new HashSet<>());
			Player player = mock(Player.class);
			PlayerRepository playerRepository = new PlayerRepositoryImpl();
			when(data.add(player)).thenReturn(true);

			Field field = playerRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(playerRepository, data);

			Player actual = playerRepository.save(player);

			assertThat(actual).isEqualTo(player);
			verify(data, only()).add(player);
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#save(Player)} с {@code null} в качестве игрока.
		 */
		@Test
		public void saveWithNullPlayer() {
			PlayerRepository playerRepository = new PlayerRepositoryImpl();

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.save(null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link PlayerRepositoryImpl#isByName(String)}.
	 */
	@Nested
	public class IsByName {
		/**
		 * Проверка метода {@link PlayerRepositoryImpl#isByName(String)}.
		 */
		@Test
		public void isByName() throws NoSuchFieldException, IllegalAccessException {
			String name = "qwe";
			Player player = mock(Player.class);
			when(player.getName()).thenReturn(name);
			Set<Player> data = spy(new HashSet<>());
			data.add(player);
			PlayerRepository playerRepository = new PlayerRepositoryImpl();

			Field field = playerRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(playerRepository, data);

			boolean actual = playerRepository.isByName(name);

			assertThat(actual).isTrue();
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#isByName(String)} с отсутствием игрока с таким именем.
		 */
		@Test
		public void isByNameWithNotExistsName() throws NoSuchFieldException, IllegalAccessException {
			String name = "qwe";
			Player player = mock(Player.class);
			when(player.getName()).thenReturn(name);
			Set<Player> data = spy(new HashSet<>());
			data.add(player);
			PlayerRepository playerRepository = new PlayerRepositoryImpl();

			Field field = playerRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(playerRepository, data);

			boolean actual = playerRepository.isByName("qwerty");

			assertThat(actual).isFalse();
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#isByName(String)} с {@code null} в качестве имени.
		 */
		@Test
		public void isByNameWithNullName() {
			PlayerRepository playerRepository = new PlayerRepositoryImpl();

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.isByName(null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link PlayerRepositoryImpl#getByNameAndPassword(String, String)}.
	 */
	@Nested
	public class GetByNameAndPassword {
		/**
		 * Проверка метода {@link PlayerRepositoryImpl#getByNameAndPassword(String, String)}.
		 */
		@Test
		public void getByNameAndPassword() throws NoSuchFieldException, IllegalAccessException {
			String name = "qwe";
			String password = "123";
			Player player = mock(Player.class);
			when(player.getName()).thenReturn(name);
			when(player.getPassword()).thenReturn(DigestUtils.md2Hex(password));
			Set<Player> data = spy(new HashSet<>());
			data.add(player);
			PlayerRepository playerRepository = new PlayerRepositoryImpl();

			Field field = playerRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(playerRepository, data);

			Optional<Player> actual = playerRepository.getByNameAndPassword(name, password);

			assertThat(actual).isPresent();
			assertThat(actual.get()).isEqualTo(player);
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#getByNameAndPassword(String, String)} с отсутствующем именем.
		 */
		@Test
		public void getByNameAndPasswordWithNotFoundName() throws NoSuchFieldException, IllegalAccessException {
			String name = "qwe";
			String password = "123";
			Player player = mock(Player.class);
			when(player.getName()).thenReturn(name);
			when(player.getPassword()).thenReturn(password);
			Set<Player> data = spy(new HashSet<>());
			data.add(player);
			PlayerRepository playerRepository = new PlayerRepositoryImpl();

			Field field = playerRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(playerRepository, data);

			Optional<Player> actual = playerRepository.getByNameAndPassword("qwerty", password);

			assertThat(actual.isEmpty()).isTrue();
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#getByNameAndPassword(String, String)} с отсутствием игрока с таким паролем.
		 */
		@Test
		public void getByNameAndPasswordWithNotExistsPassword() throws NoSuchFieldException, IllegalAccessException {
			String name = "qwe";
			String password = "123";
			Player player = mock(Player.class);
			when(player.getName()).thenReturn(name);
			when(player.getPassword()).thenReturn(password);
			Set<Player> data = spy(new HashSet<>());
			data.add(player);
			PlayerRepository playerRepository = new PlayerRepositoryImpl();

			Field field = playerRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(playerRepository, data);

			Optional<Player> actual = playerRepository.getByNameAndPassword(name, "012345678");

			assertThat(actual.isEmpty()).isTrue();
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#getByNameAndPassword(String, String)} с {@code null} в качестве имени.
		 */
		@Test
		public void getByNameAndPasswordWithNullName() {
			String password = "123";
			PlayerRepository playerRepository = new PlayerRepositoryImpl();

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.getByNameAndPassword(null, password);
			});
		}

		/**
		 * Проверка метода {@link PlayerRepositoryImpl#getByNameAndPassword(String, String)} с {@code null} в качестве пароля.
		 */
		@Test
		public void getByNameAndPasswordWithNullPassword() {
			String name = "qwe";
			PlayerRepository playerRepository = new PlayerRepositoryImpl();

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.getByNameAndPassword(name, null);
			});
		}
	}
}