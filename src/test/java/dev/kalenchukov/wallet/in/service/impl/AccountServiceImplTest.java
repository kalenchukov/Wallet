/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.exceptions.NotFoundAccountException;
import dev.kalenchukov.wallet.in.service.AccountService;
import dev.kalenchukov.wallet.repository.AccountRepository;
import dev.kalenchukov.wallet.repository.impl.AccountRepositoryImpl;
import dev.kalenchukov.wallet.repository.OperationRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * Класс проверки методов класса {@link AccountServiceImpl}.
 */
public class AccountServiceImplTest {
	/**
	 * Класс проверки метода {@link AccountServiceImpl#add(long)}.
	 */
	@Nested
	public class Add {
		/**
		 * Проверка метода {@link AccountServiceImpl#add(long)}.
		 */
		@Test
		public void add() {
			long playerId = 738L;
			Account account = mock(Account.class);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.save(any(Account.class))).thenReturn(account);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			Account actualAccount = accountService.add(playerId);

			assertThat(actualAccount).isEqualTo(account);
			verify(accountRepository, only()).save(any(Account.class));
		}
	}

	/**
	 * Класс проверки метода {@link AccountServiceImpl#findById(long)}.
	 */
	@Nested
	public class FindById {
		/**
		 * Проверка метода {@link AccountServiceImpl#findById(long)}.
		 */
		@Test
		public void findById() throws NotFoundAccountException {
			long accountId = 13L;
			Account account = mock(Account.class);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			Account actualAccount = accountService.findById(accountId);

			assertThat(actualAccount).isEqualTo(account);
			verify(accountRepository, only()).findById(anyLong());
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#findById(long)} с отсутствующим счётом.
		 */
		@Test
		public void findByIdNotFoundAccount() {
			long accountId = 0L;
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NotFoundAccountException.class).isThrownBy(() -> {
				accountService.findById(accountId);
			});
		}
	}
}