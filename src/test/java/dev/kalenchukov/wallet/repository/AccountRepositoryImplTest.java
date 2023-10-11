/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Account;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Класс проверки методов класса {@link AccountRepositoryImpl}.
 */
public class AccountRepositoryImplTest {
	/**
	 * Класс проверки метода {@link AccountRepositoryImpl#save(Account)}.
	 */
	@Nested
	public class Save {
		/**
		 * Проверка метода {@link AccountRepositoryImpl#save(Account)}.
		 */
		@Test
		public void save() throws IllegalAccessException, NoSuchFieldException {
			Set<Account> data = spy(new HashSet<>());
			Account account = mock(Account.class);
			AccountRepository accountRepository = new AccountRepositoryImpl();
			when(data.add(account)).thenReturn(true);

			Field field = accountRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(accountRepository, data);

			Account actual = accountRepository.save(account);

			assertThat(actual).isEqualTo(account);
			verify(data, only()).add(account);
		}

		/**
		 * Проверка метода {@link AccountRepositoryImpl#save(Account)} с {@code null} в качестве счёта.
		 */
		@Test
		public void saveWithNullAccount() {
			AccountRepository accountRepository = new AccountRepositoryImpl();

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountRepository.save(null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link AccountRepositoryImpl#updateAmount(long, BigDecimal)}.
	 */
	@Nested
	public class UpdateAmount {
		/**
		 * Проверка метода {@link AccountRepositoryImpl#updateAmount(long, BigDecimal)}.
		 */
		@Test
		public void updateAmount() throws NoSuchFieldException, IllegalAccessException {
			Set<Account> data = spy(new HashSet<>());
			long accountId = 53L;
			BigDecimal amount = mock(BigDecimal.class);
			Account account = mock(Account.class);
			when(account.getAccountId()).thenReturn(accountId);
			doNothing().when(account).setAmount(amount);
			AccountRepository accountRepository = new AccountRepositoryImpl();
			when(data.add(account)).thenReturn(true);

			Field field = accountRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(accountRepository, data);

			accountRepository.updateAmount(accountId, amount);

			verify(account, times(1)).setAmount(amount);
		}

		/**
		 * Проверка метода {@link AccountRepositoryImpl#updateAmount(long, BigDecimal)} с отсутствующем счётом.
		 */
		@Test
		public void updateAmountWithNotExistsAccount() throws NoSuchFieldException, IllegalAccessException {
			Set<Account> data = spy(new HashSet<>());
			long accountId = 53L;
			BigDecimal amount = mock(BigDecimal.class);
			Account account = mock(Account.class);
			when(account.getAccountId()).thenReturn(accountId);
			doNothing().when(account).setAmount(amount);
			AccountRepository accountRepository = new AccountRepositoryImpl();
			when(data.add(account)).thenReturn(true);

			Field field = accountRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(accountRepository, data);

			accountRepository.updateAmount(999L, amount);

			verify(account, times(0)).setAmount(amount);
		}

		/**
		 * Проверка метода {@link AccountRepositoryImpl#updateAmount(long, BigDecimal)} с {@code null} в качестве суммы.
		 */
		@Test
		public void updateAmountWithNullAmount() {
			long accountId = 11L;
			AccountRepository accountRepository = new AccountRepositoryImpl();

			assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
				accountRepository.updateAmount(accountId, null);
			});
		}
	}

	/**
	 * Класс проверки метода {@link AccountRepositoryImpl#getById(long)}.
	 */
	@Nested
	public class GetById {
		/**
		 * Проверка метода {@link AccountRepositoryImpl#getById(long)}.
		 */
		@Test
		public void getById() throws NoSuchFieldException, IllegalAccessException {
			Set<Account> data = spy(new HashSet<>());
			long accountId = 53L;
			Account account = mock(Account.class);
			when(account.getAccountId()).thenReturn(accountId);
			AccountRepository accountRepository = new AccountRepositoryImpl();
			when(data.add(account)).thenReturn(true);

			Field field = accountRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(accountRepository, data);

			Optional<Account> actual = accountRepository.getById(accountId);

			assertThat(actual.isPresent()).isTrue();
			assertThat(actual.get()).isEqualTo(account);
		}

		/**
		 * Проверка метода {@link AccountRepositoryImpl#getById(long)} с отсутствующем счётом.
		 */
		@Test
		public void getByIdWithNotExists() throws NoSuchFieldException, IllegalAccessException {
			Set<Account> data = spy(new HashSet<>());
			long accountId = 53L;
			Account account = mock(Account.class);
			when(account.getAccountId()).thenReturn(accountId);
			AccountRepository accountRepository = new AccountRepositoryImpl();
			when(data.add(account)).thenReturn(true);

			Field field = accountRepository.getClass().getDeclaredField("DATA");
			field.setAccessible(true);
			field.set(accountRepository, data);

			Optional<Account> actual = accountRepository.getById(1099L);

			assertThat(actual.isEmpty()).isTrue();
		}
	}
}