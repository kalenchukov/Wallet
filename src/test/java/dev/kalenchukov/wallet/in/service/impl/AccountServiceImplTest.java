/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.wallet.entity.Account;
import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.NegativeAmountOperationException;
import dev.kalenchukov.wallet.exceptions.NoAccessAccountException;
import dev.kalenchukov.wallet.exceptions.NotFoundAccountException;
import dev.kalenchukov.wallet.exceptions.OutOfAmountAccountException;
import dev.kalenchukov.wallet.in.service.AccountService;
import dev.kalenchukov.wallet.repository.AccountRepository;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.repository.impl.AccountRepositoryImpl;
import dev.kalenchukov.wallet.type.OperationType;
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
	 * Класс проверки метода {@link AccountServiceImpl#findById(long, long)}.
	 */
	@Nested
	public class FindById {
		/**
		 * Проверка метода {@link AccountServiceImpl#findById(long, long)}.
		 */
		@Test
		public void findById() throws NotFoundAccountException {
			long playerId = 78L;
			long accountId = 13L;
			Account account = mock(Account.class);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			Account actualAccount = accountService.findById(playerId, accountId);

			assertThat(actualAccount).isEqualTo(account);
			verify(accountRepository, only()).findById(anyLong(), anyLong());
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#findById(long, long)}
		 * с отсутствующим счётом.
		 */
		@Test
		public void findByIdNotFoundAccount() {
			long playerId = 78L;
			long accountId = 0L;
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.empty());
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NotFoundAccountException.class).isThrownBy(() -> {
				accountService.findById(playerId, accountId);
			});
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
		public void credit() throws NotFoundAccountException, NoAccessAccountException,
				NegativeAmountOperationException {
			long operationId = 3L;
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(99.58);
			BigDecimal currentAmountAccount = spy(BigDecimal.valueOf(10.00));
			OperationType operationType = OperationType.CREDIT;
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			when(account.getAmount()).thenReturn(currentAmountAccount);
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(operationId);
			when(operation.getAccountId()).thenReturn(accountId);
			when(operation.getPlayerId()).thenReturn(playerId);
			when(operation.getOperationType()).thenReturn(operationType);
			when(operation.getAmount()).thenReturn(amount);
			OperationRepository operationRepository = mock(OperationRepository.class);
			when(operationRepository.save(any(Operation.class))).thenReturn(operation);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			Operation actualOperation = accountService.credit(playerId, accountId, amount);

			assertThat(actualOperation).isEqualTo(operation);
			verify(accountRepository, times(1)).findById(anyLong(), anyLong());
			verify(currentAmountAccount, times(1)).add(amount);
			verify(accountRepository, times(1)).updateAmount(anyLong(), anyLong(), any(BigDecimal.class));
			verify(operationRepository, times(1)).save(any(Operation.class));
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#credit(long, long, BigDecimal)}
		 * с {@code null} в качестве суммы.
		 */
		@Test
		public void creditWithNullAmount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(0.58);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountService.credit(playerId, accountId, amount);
			});
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#credit(long, long, BigDecimal)}
		 * с отсутствием счёта.
		 */
		@Test
		public void creditWithNotFoundAccount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(13.43);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.empty());
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NotFoundAccountException.class).isThrownBy(() -> {
				accountService.credit(playerId, accountId, amount);
			});
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#credit(long, long, BigDecimal)}
		 * с отсутствием доступа к счёту.
		 */
		@Test
		public void creditWithNoAccessAccount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(13.43);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(54654L);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NoAccessAccountException.class).isThrownBy(() -> {
				accountService.credit(playerId, accountId, amount);
			});
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#credit(long, long, BigDecimal)}
		 * с отрицательной суммой.
		 */
		@Test
		public void creditWithNegativeAmount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(-19.43);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NegativeAmountOperationException.class).isThrownBy(() -> {
				accountService.credit(playerId, accountId, amount);
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
		public void debit() throws NotFoundAccountException, NoAccessAccountException,
				OutOfAmountAccountException, NegativeAmountOperationException {
			long operationId = 3L;
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(99.58);
			BigDecimal currentAmountAccount = spy(BigDecimal.valueOf(100.00));
			OperationType operationType = OperationType.DEBIT;
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			when(account.getAmount()).thenReturn(currentAmountAccount);
			Operation operation = mock(Operation.class);
			when(operation.getOperationId()).thenReturn(operationId);
			when(operation.getAccountId()).thenReturn(accountId);
			when(operation.getPlayerId()).thenReturn(playerId);
			when(operation.getOperationType()).thenReturn(operationType);
			when(operation.getAmount()).thenReturn(amount);
			OperationRepository operationRepository = mock(OperationRepository.class);
			when(operationRepository.save(any(Operation.class))).thenReturn(operation);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			Operation actualOperation = accountService.debit(playerId, accountId, amount);

			assertThat(actualOperation).isEqualTo(operation);
			verify(accountRepository, times(1)).findById(anyLong(), anyLong());
			verify(currentAmountAccount, times(1)).subtract(amount);
			verify(accountRepository, times(1))
					.updateAmount(anyLong(), anyLong(), any(BigDecimal.class));
			verify(operationRepository, times(1)).save(any(Operation.class));
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#debit(long, long, BigDecimal)}
		 * с {@code null} в качестве суммы.
		 */
		@Test
		public void debitWithNullAmount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(0.58);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountService.debit(playerId, accountId, amount);
			});
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#debit(long, long, BigDecimal)}
		 * с отсутствием счёта.
		 */
		@Test
		public void debitWithNotFoundAccount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(13.43);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.empty());
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NotFoundAccountException.class).isThrownBy(() -> {
				accountService.debit(playerId, accountId, amount);
			});
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#debit(long, long, BigDecimal)}
		 * с отсутствием доступа к счёту.
		 */
		@Test
		public void debitWithNoAccessAccount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(13.43);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(54654L);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NoAccessAccountException.class).isThrownBy(() -> {
				accountService.debit(playerId, accountId, amount);
			});
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#debit(long, long, BigDecimal)}
		 * с отрицательной суммой.
		 */
		@Test
		public void debitWithNegativeAmount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(-19.43);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NegativeAmountOperationException.class).isThrownBy(() -> {
				accountService.debit(playerId, accountId, amount);
			});
		}

		/**
		 * Проверка метода {@link AccountServiceImpl#debit(long, long, BigDecimal)}
		 * с превышением суммы на счету.
		 */
		@Test
		public void debitWithOutOfAmount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(50.47);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			when(account.getAmount()).thenReturn(BigDecimal.TEN);
			OperationRepository operationRepository = mock(OperationRepository.class);
			AccountRepository accountRepository = mock(AccountRepositoryImpl.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(OutOfAmountAccountException.class).isThrownBy(() -> {
				accountService.debit(playerId, accountId, amount);
			});
		}
	}
}