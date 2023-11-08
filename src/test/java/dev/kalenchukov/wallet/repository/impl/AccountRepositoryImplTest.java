/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository.impl;

import dev.kalenchukov.wallet.WalletApplicationTest;
import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = WalletApplicationTest.class)
public class AccountRepositoryImplTest {
	@Autowired
	private AccountRepository accountRepository;

	@Nested
	public class Save {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void saveValid() {
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(1L);
			when(account.getAmount()).thenReturn(BigDecimal.TEN);

			Account actualAccount = accountRepository.save(account);

			assertThat(actualAccount.getAccountId()).isPositive();
			assertThat(actualAccount.getPlayerId()).isEqualTo(account.getPlayerId());
			assertThat(actualAccount.getAmount()).isEqualTo(account.getAmount());
		}

		@DisplayName("Проверка с null в качестве счёта.")
		@Test
		public void saveWithNull() {
			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountRepository.save(null);
			});
		}
	}

	@Nested
	public class UpdateAmount {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void updateAmountValid() {
			long playerId = 1L;
			long accountId = 2L;

			boolean actual = accountRepository.updateAmount(playerId, accountId, new BigDecimal("97.88"));

			assertThat(actual).isTrue();
		}

		@DisplayName("Проверка с отсутствующим счётом.")
		@Test
		public void updateAmountNotFoundAccountId() {
			long playerId = 32L;
			long accountId = 24565464L;

			boolean actual = accountRepository.updateAmount(playerId, accountId, new BigDecimal("97.88"));

			assertThat(actual).isFalse();
		}

		@DisplayName("Проверка с null в качестве суммы.")
		@Test
		public void updateAmountWithNullAmount() {
			long playerId = 32L;
			long accountId = 11L;

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountRepository.updateAmount(playerId, accountId, null);
			});
		}
	}

	@Nested
	public class FindById {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void findByIdValid() {
			long playerId = 2L;
			long accountId = 4L;

			Optional<Account> actualAccount = accountRepository.findById(playerId, accountId);

			assertThat(actualAccount).isPresent();
			assertThat(actualAccount.get().getAccountId()).isEqualTo(accountId);
			assertThat(actualAccount.get().getPlayerId()).isEqualTo(2L);
			assertThat(actualAccount.get().getAmount()).isEqualTo(new BigDecimal("40.0"));
		}

		@DisplayName("Проверка с отсутствующим счётом.")
		@Test
		public void findByIdWithNotFound() {
			long playerId = 91L;
			long accountId = 7897894L;

			Optional<Account> actualAccount = accountRepository.findById(playerId, accountId);

			assertThat(actualAccount).isEmpty();
		}
	}
}