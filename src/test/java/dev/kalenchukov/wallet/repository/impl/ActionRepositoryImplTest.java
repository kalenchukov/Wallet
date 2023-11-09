/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.starter.fixaction.entity.Action;
import dev.kalenchukov.wallet.WalletApplicationTest;
import dev.kalenchukov.wallet.repository.ActionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = WalletApplicationTest.class)
public class ActionRepositoryImplTest {

	@Autowired
	private ActionRepository actionRepository;

	@Nested
	public class Find {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void findValid() {
			long playerId = 1L;

			List<Action> actualSet = actionRepository.find(playerId);

			assertThat(actualSet).hasSize(11);
		}

		@DisplayName("Проверка с отсутствующими действиями.")
		@Test
		public void findWithNotFound() {
			long playerId = 446651L;

			List<Action> actualSet = actionRepository.find(playerId);

			assertThat(actualSet).isEmpty();
		}
	}
}