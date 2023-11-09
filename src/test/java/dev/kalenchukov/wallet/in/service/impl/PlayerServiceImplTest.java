/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.DuplicateNamePlayerException;
import dev.kalenchukov.wallet.exceptions.NotFoundPlayerException;
import dev.kalenchukov.wallet.in.service.PlayerService;
import dev.kalenchukov.wallet.repository.PlayerRepository;
import dev.kalenchukov.wallet.repository.impl.PlayerRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class PlayerServiceImplTest {
	private PlayerRepository playerRepository;

	@BeforeEach
	public void beforeEach() {
		this.playerRepository = mock(PlayerRepositoryImpl.class);
	}

	@Nested
	public class Add {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void addValid() throws DuplicateNamePlayerException {
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			Player player = mock(Player.class);
			when(playerRepository.existsByName(anyString())).thenReturn(false);
			when(playerRepository.save(any(Player.class))).thenReturn(player);
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			Player actualPlayer = playerService.add(name, password);

			assertThat(actualPlayer).isEqualTo(player);
			verify(playerRepository, times(1)).existsByName(name);
			verify(playerRepository, times(1)).save(any(Player.class));
		}

		@DisplayName("Проверка с null в качестве имени.")
		@Test
		public void addWithNullName() {
			String name = null;
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerService.add(name, password);
			});
		}

		@DisplayName("Проверка с null в качестве пароля.")
		@Test
		public void addWithNullPassword() {
			String name = "Имя";
			String password = null;
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerService.add(name, password);
			});
		}

		@DisplayName("Проверка с дублирующимся именем.")
		@Test
		public void addWithDuplicateName() {
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			when(playerRepository.existsByName(anyString())).thenReturn(true);
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(DuplicateNamePlayerException.class).isThrownBy(() -> {
				playerService.add(name, password);
			});
		}
	}

	@Nested
	public class FindByNameAndPassword {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void findValid() throws NotFoundPlayerException {
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			Player player = mock(Player.class);
			when(playerRepository.find(anyString(), anyString())).thenReturn(Optional.of(player));
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			Player actual = playerService.find(name, password);

			verify(playerRepository, only()).find(name, password);
			assertThat(actual).isEqualTo(player);
		}

		@DisplayName("Проверка с отсутствующим игроком.")
		@Test
		public void findNotFound() {
			String name = "Имя";
			String password = "d41d8cd98f00b204e9800998ecf8427e";
			when(playerRepository.find(anyString(), anyString())).thenReturn(Optional.empty());
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(NotFoundPlayerException.class).isThrownBy(() -> {
				playerService.find(name, password);
			});
		}

		@DisplayName("Проверка с null в качестве имени.")
		@Test
		public void findWithNullName() {
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerService.find(null, "d41d8cd98f00b204e9800998ecf8427e");
			});
		}

		@DisplayName("Проверка с null в качестве пароля.")
		@Test
		public void findWithNullPassword() {
			PlayerService playerService = new PlayerServiceImpl(playerRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerService.find("Имя", null);
			});
		}
	}
}