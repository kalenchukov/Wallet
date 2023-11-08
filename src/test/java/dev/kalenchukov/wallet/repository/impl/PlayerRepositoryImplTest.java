/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.WalletApplicationTest;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.repository.PlayerRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = WalletApplicationTest.class)
public class PlayerRepositoryImplTest {
	@Autowired
	private PlayerRepository playerRepository;

	@Nested
	public class Save {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void saveValid() {
			Player player = mock(Player.class);
			when(player.getName()).thenReturn("fedya");
			when(player.getPassword()).thenReturn("d5478af3238e9f2332ce87eb3958b38a");

			Player actualPlayer = playerRepository.save(player);

			assertThat(actualPlayer.getPlayerId()).isPositive();
			assertThat(actualPlayer.getName()).isEqualTo(player.getName());
			assertThat(actualPlayer.getPassword()).isEqualTo(player.getPassword());
		}

		@DisplayName("Проверка с null в качестве игрока.")
		@Test
		public void saveWithNull() {
			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.save(null);
			});
		}
	}

	@Nested
	public class IsByName {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void existsByNameValid() {
			String name = "igor";

			boolean actual = playerRepository.existsByName(name);

			assertThat(actual).isTrue();
		}

		@DisplayName("Проверка с отсутствием игроком.")
		@Test
		public void existsByNameWithNotFound() {
			String name = "fedya";

			boolean actual = playerRepository.existsByName(name);

			assertThat(actual).isFalse();
		}

		@DisplayName("Проверка с null в качестве имени.")
		@Test
		public void existsByNameWithNullName() {
			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.existsByName(null);
			});
		}
	}

	@Nested
	public class FindByNameAndPassword {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void findValid() {
			String name = "igor";
			String password = DigestUtils.md5Hex("igor");

			Optional<Player> actualPlayer = playerRepository.find(name, password);

			assertThat(actualPlayer).isPresent();
			assertThat(actualPlayer.get().getPlayerId()).isEqualTo(3L);
			assertThat(actualPlayer.get().getName()).isEqualTo(name);
			assertThat(actualPlayer.get().getPassword()).isEqualTo(password);
		}

		@DisplayName("Проверка с отсутствующим игроком.")
		@Test
		public void findWithNotFound() {
			String name = "fedya";
			String password = DigestUtils.md5Hex("igor");

			Optional<Player> actualPlayer = playerRepository.find(name, password);

			assertThat(actualPlayer).isEmpty();
		}

		@DisplayName("Проверка с null в качестве имени.")
		@Test
		public void findWithNullName() {
			String password = "45b645g45f35dt34454b57g4v53";

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.find(null, password);
			});
		}

		@DisplayName("Проверка с null в качестве пароля.")
		@Test
		public void findWithNullPassword() {
			String name = "qwe";

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				playerRepository.find(name, null);
			});
		}
	}
}