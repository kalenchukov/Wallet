/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.entity.Player;
import dev.kalenchukov.wallet.exceptions.NoAccessAccountException;
import dev.kalenchukov.wallet.exceptions.NotFoundAccountException;
import dev.kalenchukov.wallet.repository.AccountRepository;
import dev.kalenchukov.wallet.repository.AccountRepositoryImpl;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.resources.OperationType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
	 * Класс проверки метода {@link AccountServiceImpl#credit(long, long, BigDecimal)}.
	 */
	@Nested
	public class Credit {
		/**
		 * Проверка метода {@link AccountServiceImpl#credit(long, long, BigDecimal)}.
		 */
		@Test
		public void credit() throws NotFoundAccountException, NoAccessAccountException {
			long accountId = 13L;
			long playerId = 78L;
			Account account = mock(Account.class);
			BigDecimal amount = mock(BigDecimal.class);
			when(account.getPlayerId()).thenReturn(playerId);
			when(account.getAccountId()).thenReturn(accountId);
			when(account.getAmount()).thenReturn(amount);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.getById(anyLong())).thenReturn(Optional.of(account));
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			Operation actualOperation = accountService.credit(accountId, playerId, amount);

			assertThat(actualOperation).isNotNull();
			assertThat(actualOperation.getOperationId()).isPositive();
			assertThat(actualOperation.getAccount()).isEqualTo(account);
			assertThat(actualOperation.getOperationType()).isEqualTo(OperationType.CREDIT);
			assertThat(actualOperation.getAmount()).isPositive();
			verify(accountRepository, times(1)).getById(anyLong());
			verify(account, times(1)).credit(any(BigDecimal.class));
			verify(accountRepository, times(1)).updateAmount(anyLong(), any(BigDecimal.class));
			verify(operationRepository, times(1)).save(any(Operation.class));
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#credit(long, long, BigDecimal)} с {@code null} в качестве суммы.
		 */
		@Test
		public void creditWithNullAmount() {
			long accountId = 13L;
			long playerId = 78L;
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountService.credit(accountId, playerId, null);
			});
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#credit(long, long, BigDecimal)}.
		 */
		@Test
		public void creditWithNoAccessAccount() {
			long accountId = 13L;
			long playerId = 13L;
			long ownerId = 987L;
			BigDecimal amount = mock(BigDecimal.class);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(ownerId);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.getById(anyLong())).thenReturn(Optional.of(account));
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NoAccessAccountException.class).isThrownBy(() -> {
				accountService.credit(accountId, playerId, amount);
			});
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#credit(long, long, BigDecimal)} с отсутствующем счётом.
		 */
		@Test
		public void creditNotExistsAccount() {
			long accountId = 0L;
			long playerId = 78L;
			BigDecimal amount = mock(BigDecimal.class);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.getById(anyLong())).thenReturn(Optional.empty());
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NotFoundAccountException.class).isThrownBy(() -> {
				accountService.credit(accountId, playerId, amount);
			});
		}
	}

	/**
	 * Класс проверки метода {@link AccountServiceImpl#debit(long, long, BigDecimal)}.
	 */
	@Nested
	public class Debit {
		/**
		 * Проверка метода {@link AccountServiceImpl#debit(long, long, BigDecimal)}.
		 */
		@Test
		public void debit() throws NotFoundAccountException, NoAccessAccountException {
			long accountId = 13L;
			long playerId = 78L;
			Account account = mock(Account.class);
			BigDecimal amount = mock(BigDecimal.class);
			when(account.getPlayerId()).thenReturn(playerId);
			when(account.getAccountId()).thenReturn(accountId);
			when(account.getAmount()).thenReturn(amount);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.getById(anyLong())).thenReturn(Optional.of(account));
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			Operation actualOperation = accountService.debit(accountId, playerId, amount);

			assertThat(actualOperation).isNotNull();
			assertThat(actualOperation.getOperationId()).isPositive();
			assertThat(actualOperation.getAccount()).isEqualTo(account);
			assertThat(actualOperation.getOperationType()).isEqualTo(OperationType.DEBIT);
			assertThat(actualOperation.getAmount()).isPositive();
			verify(accountRepository, times(1)).getById(anyLong());
			verify(account, times(1)).debit(any(BigDecimal.class));
			verify(accountRepository, times(1)).updateAmount(anyLong(), any(BigDecimal.class));
			verify(operationRepository, times(1)).save(any(Operation.class));
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#debit(long, long, BigDecimal)} с {@code null} в качестве суммы.
		 */
		@Test
		public void debitWithNullAmount() {
			long accountId = 13L;
			long playerId = 78L;
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountService.debit(accountId, playerId, null);
			});
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#debit(long, long, BigDecimal)}.
		 */
		@Test
		public void debitWithNoAccessAccount() {
			long accountId = 13L;
			long playerId = 13L;
			long ownerId = 987L;
			BigDecimal amount = mock(BigDecimal.class);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(ownerId);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.getById(anyLong())).thenReturn(Optional.of(account));
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NoAccessAccountException.class).isThrownBy(() -> {
				accountService.debit(accountId, playerId, amount);
			});
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#debit(long, long, BigDecimal)} с отсутствующем счётом.
		 */
		@Test
		public void debitNotExistsAccount() {
			long accountId = 0L;
			long playerId = 78L;
			BigDecimal amount = mock(BigDecimal.class);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.getById(anyLong())).thenReturn(Optional.empty());
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NotFoundAccountException.class).isThrownBy(() -> {
				accountService.debit(accountId, playerId, amount);
			});
		}
	}

	/**
	 * Класс проверки метода {@link AccountServiceImpl#getById(long)}.
	 */
	@Nested
	public class GetById {
		/**
		 * Проверка метода {@link AccountServiceImpl#getById(long)}.
		 */
		@Test
		public void getById() throws NotFoundAccountException {
			long accountId = 13L;
			Account account = mock(Account.class);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.getById(anyLong())).thenReturn(Optional.of(account));
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			Account actualAccount = accountService.getById(accountId);

			assertThat(actualAccount).isEqualTo(account);
			verify(accountRepository, only()).getById(anyLong());
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#getById(long)} с отсутствующем счётом.
		 */
		@Test
		public void getByIdNotExistsAccount() {
			long accountId = 0L;
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.getById(anyLong())).thenReturn(Optional.empty());
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NotFoundAccountException.class).isThrownBy(() -> {
				accountService.getById(accountId);
			});
		}
	}
}