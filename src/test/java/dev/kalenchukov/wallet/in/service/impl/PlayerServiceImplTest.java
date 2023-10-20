/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.DuplicatePlayerException;
import dev.kalenchukov.wallet.exceptions.NotFoundPlayerException;
import dev.kalenchukov.wallet.in.service.PlayerService;
import dev.kalenchukov.wallet.repository.PlayerRepository;
import dev.kalenchukov.wallet.repository.impl.PlayerRepositoryImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * Класс проверки методов класса {@link PlayerServiceImpl}.
 */
public class PlayerServiceImplTest {
	/**
	 * Класс проверки метода {@link PlayerServiceImpl#add(String, String)}.
	 */
	@Nested
	public class Add {
		/**
		 * Проверка метода {@link PlayerServiceImpl#add(String, String)}.
		 */
		@Test
		public void add() throws DuplicatePlayerException {
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			Player player = mock(Player.class);
			PlayerRepository playerRepository = mock(PlayerRepositoryImpl.class);
			when(playerRepository.existsByName(anyString())).thenReturn(false);
			when(playerRepository.save(any(Player.class))).thenReturn(player);
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			Player actualPlayer = playerService.add(name, password);

			assertThat(actualPlayer).isEqualTo(player);
			verify(playerRepository, times(1)).existsByName(name);
			verify(playerRepository, times(1)).save(any(Player.class));
		}

		/**
		 * Проверка метода {@link PlayerServiceImpl#add(String, String)} с {@code null} в качестве имени.
		 */
		@Test
		public void addWithNullName() {
			String name = null;
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			PlayerRepository playerRepository = mock(PlayerRepositoryImpl.class);
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerService.add(name, password);
			});
		}

		/**
		 * Проверка метода {@link PlayerServiceImpl#add(String, String)} с {@code null} в качестве пароля.
		 */
		@Test
		public void addWithNullPassword() {
			String name = "Имя";
			String password = null;
			PlayerRepository playerRepository = mock(PlayerRepositoryImpl.class);
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerService.add(name, password);
			});
		}

		/**
		 * Проверка метода {@link PlayerServiceImpl#add(String, String)}.
		 */
		@Test
		public void addWithDuplicateName() {
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			PlayerRepository playerRepository = mock(PlayerRepositoryImpl.class);
			when(playerRepository.existsByName(anyString())).thenReturn(true);
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(DuplicatePlayerException.class).isThrownBy(() -> {
				playerService.add(name, password);
			});
		}
	}

	/**
	 * Класс проверки метода {@link PlayerServiceImpl#find(String, String)}.
	 */
	@Nested
	public class FindByNameAndPassword {
		/**
		 * Проверка метода {@link PlayerServiceImpl#find(String, String)}.
		 */
		@Test
		public void find() throws NotFoundPlayerException {
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			Player player = mock(Player.class);
			PlayerRepository playerRepository = mock(PlayerRepositoryImpl.class);
			when(playerRepository.find(anyString(), anyString())).thenReturn(Optional.of(player));
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			Player actual = playerService.find(name, password);

			verify(playerRepository, only()).find(name, password);
			assertThat(actual).isEqualTo(player);
		}

		/**
		 * Проверка метода {@link PlayerServiceImpl#find(String, String)} с отсутствующим игроком.
		 */
		@Test
		public void findNotFound() {
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			PlayerRepository playerRepository = mock(PlayerRepositoryImpl.class);
			when(playerRepository.find(anyString(), anyString())).thenReturn(Optional.empty());
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(NotFoundPlayerException.class).isThrownBy(() -> {
				playerService.find(name, password);
			});
		}

		/**
		 * Проверка метода {@link PlayerServiceImpl#find(String, String)} с {@code null} в качестве имени.
		 */
		@Test
		public void findWithNullName() {
			PlayerRepository playerRepository = mock(PlayerRepositoryImpl.class);
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerService.find(null, "d41d8cd98f00b204e9800998ecf8427e");
			});
		}

		/**
		 * Проверка метода {@link PlayerServiceImpl#find(String, String)} с {@code null} в качестве пароля.
		 */
		@Test
		public void findWithNullPassword() {
			PlayerRepository playerRepository = mock(PlayerRepositoryImpl.class);
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerService.find("Имя", null);
			});
		}
	}
}