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
import dev.kalenchukov.wallet.repository.impl.OperationRepositoryImpl;
import dev.kalenchukov.wallet.type.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {
	private AccountRepository accountRepository;

	private OperationRepository operationRepository;

	@BeforeEach
	public void beforeEach() {
		this.accountRepository = mock(AccountRepositoryImpl.class);
		this.operationRepository = mock(OperationRepositoryImpl.class);
	}

	@Nested
	public class Add {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void addValid() {
			long playerId = 738L;
			Account account = mock(Account.class);
			when(accountRepository.save(any(Account.class))).thenReturn(account);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			Account actualAccount = accountService.add(playerId);

			assertThat(actualAccount).isEqualTo(account);
			verify(accountRepository, only()).save(any(Account.class));
		}
	}

	@Nested
	public class FindById {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void findByIdValid() throws NotFoundAccountException {
			long playerId = 78L;
			long accountId = 13L;
			Account account = mock(Account.class);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			Account actualAccount = accountService.findById(playerId, accountId);

			assertThat(actualAccount).isEqualTo(account);
			verify(accountRepository, only()).findById(anyLong(), anyLong());
		}

		@DisplayName("Проверка с отсутствующим счётом.")
		@Test
		public void findByIdNotFoundAccount() {
			long playerId = 78L;
			long accountId = 0L;
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.empty());
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NotFoundAccountException.class).isThrownBy(() -> {
				accountService.findById(playerId, accountId);
			});
		}
	}

	@Nested
	public class Credit {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void creditValid() throws NotFoundAccountException, NoAccessAccountException, NegativeAmountOperationException {
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
			when(operationRepository.save(any(Operation.class))).thenReturn(operation);
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

		@DisplayName("Проверка с null в качестве суммы.")
		@Test
		public void creditWithNullAmount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(0.58);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountService.credit(playerId, accountId, amount);
			});
		}

		@DisplayName("Проверка с отсутствием счёта.")
		@Test
		public void creditWithNotFoundAccount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(13.43);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.empty());
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NotFoundAccountException.class).isThrownBy(() -> {
				accountService.credit(playerId, accountId, amount);
			});
		}

		@DisplayName("Проверка с отсутствием доступа к счёту.")
		@Test
		public void creditWithNoAccessAccount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(13.43);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(54654L);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NoAccessAccountException.class).isThrownBy(() -> {
				accountService.credit(playerId, accountId, amount);
			});
		}

		@DisplayName("Проверка с отрицательной суммой.")
		@Test
		public void creditWithNegativeAmount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(-19.43);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NegativeAmountOperationException.class).isThrownBy(() -> {
				accountService.credit(playerId, accountId, amount);
			});
		}
	}

	@Nested
	public class Debit {
		@DisplayName("Проверка с корректными данными.")
		@Test
		public void debitValid() throws NotFoundAccountException, NoAccessAccountException, OutOfAmountAccountException, NegativeAmountOperationException {
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
			when(operationRepository.save(any(Operation.class))).thenReturn(operation);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			Operation actualOperation = accountService.debit(playerId, accountId, amount);

			assertThat(actualOperation).isEqualTo(operation);
			verify(accountRepository, times(1)).findById(anyLong(), anyLong());
			verify(currentAmountAccount, times(1)).subtract(amount);
			verify(accountRepository, times(1)).updateAmount(anyLong(), anyLong(), any(BigDecimal.class));
			verify(operationRepository, times(1)).save(any(Operation.class));
		}

		@DisplayName("Проверка с null в качестве суммы.")
		@Test
		public void debitWithNullAmount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(0.58);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountService.debit(playerId, accountId, amount);
			});
		}

		@DisplayName("Проверка с отсутствием счёта.")
		@Test
		public void debitWithNotFoundAccount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(13.43);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.empty());
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NotFoundAccountException.class).isThrownBy(() -> {
				accountService.debit(playerId, accountId, amount);
			});
		}

		@DisplayName("Проверка с отсутствием доступа к счёту.")
		@Test
		public void debitWithNoAccessAccount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(13.43);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(54654L);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NoAccessAccountException.class).isThrownBy(() -> {
				accountService.debit(playerId, accountId, amount);
			});
		}

		@DisplayName("Проверка с отрицательной суммой.")
		@Test
		public void debitWithNegativeAmount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(-19.43);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			when(accountRepository.updateAmount(anyLong(), anyLong(), any(BigDecimal.class))).thenReturn(true);
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(NegativeAmountOperationException.class).isThrownBy(() -> {
				accountService.debit(playerId, accountId, amount);
			});
		}

		@DisplayName("Проверка с превышением суммы на счету.")
		@Test
		public void debitWithOutOfAmount() {
			long accountId = 79L;
			long playerId = 64L;
			BigDecimal amount = BigDecimal.valueOf(50.47);
			Account account = mock(Account.class);
			when(account.getPlayerId()).thenReturn(playerId);
			when(account.getAmount()).thenReturn(BigDecimal.TEN);
			when(accountRepository.findById(anyLong(), anyLong())).thenReturn(Optional.of(account));
			AccountService accountService = new AccountServiceImpl(accountRepository, operationRepository);

			assertThatExceptionOfType(OutOfAmountAccountException.class).isThrownBy(() -> {
				accountService.debit(playerId, accountId, amount);
			});
		}
	}
}