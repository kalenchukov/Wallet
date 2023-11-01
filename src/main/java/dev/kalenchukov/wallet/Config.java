/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet;

import dev.kalenchukov.wallet.aop.annotations.FixAction;
import dev.kalenchukov.wallet.aop.aspects.ExecuteAllMethodAspect;
import dev.kalenchukov.wallet.aop.aspects.ExecuteAnnotationFixActionMethodAspect;
import dev.kalenchukov.wallet.in.service.AccountService;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.service.OperationService;
import dev.kalenchukov.wallet.in.service.PlayerService;
import dev.kalenchukov.wallet.in.service.impl.AccountServiceImpl;
import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.in.service.impl.OperationServiceImpl;
import dev.kalenchukov.wallet.in.service.impl.PlayerServiceImpl;
import dev.kalenchukov.wallet.modules.DataBase;
import dev.kalenchukov.wallet.repository.AccountRepository;
import dev.kalenchukov.wallet.repository.ActionRepository;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.repository.PlayerRepository;
import dev.kalenchukov.wallet.repository.impl.AccountRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.OperationRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.PlayerRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;

import javax.sql.DataSource;

/**
 * Класс конфигурации приложения.
 */
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableOpenApi
@ComponentScan("dev.kalenchukov.wallet")
public class Config {
	/**
	 * Возвращает источник данных.
	 *
	 * @return источник данных.
	 */
	@Bean
	public DataSource dataSource() {
		return DataBase.getDataSource();
	}

	/**
	 * Возвращает репозиторий счетов.
	 *
	 * @return репозиторий счетов.
	 */
	@Bean
	public AccountRepository accountRepository() {
		return new AccountRepositoryImpl(this.dataSource());
	}

	/**
	 * Возвращает репозиторий действий.
	 *
	 * @return репозиторий действий.
	 */
	@Bean
	public ActionRepository actionRepository() {
		return new ActionRepositoryImpl(this.dataSource());
	}

	/**
	 * Возвращает репозиторий операций.
	 *
	 * @return репозиторий операций.
	 */
	@Bean
	public OperationRepository operationRepository() {
		return new OperationRepositoryImpl(this.dataSource());
	}

	/**
	 * Возвращает репозиторий игроков.
	 *
	 * @return репозиторий игроков.
	 */
	@Bean
	public PlayerRepository playerRepository() {
		return new PlayerRepositoryImpl(this.dataSource());
	}

	/**
	 * Возвращает сервис игроков.
	 *
	 * @return сервис игроков.
	 */
	@Bean
	public PlayerService playerService() {
		return new PlayerServiceImpl(this.playerRepository());
	}

	/**
	 * Возвращает сервис счетов.
	 *
	 * @return сервис счетов.
	 */
	@Bean
	public AccountService accountService() {
		return new AccountServiceImpl(this.accountRepository(), this.operationRepository());
	}

	/**
	 * Возвращает сервис действий.
	 *
	 * @return сервис действий.
	 */
	@Bean
	public ActionService actionService() {
		return new ActionServiceImpl(this.actionRepository());
	}

	/**
	 * Возвращает сервис операций.
	 *
	 * @return сервис операций.
	 */
	@Bean
	public OperationService operationService() {
		return new OperationServiceImpl(this.operationRepository());
	}

	/**
	 * Возвращает аспект при вызове всех методов.
	 *
	 * @return аспект.
	 */
	@Bean
	public ExecuteAllMethodAspect executeAllMethodAspect() {
		return new ExecuteAllMethodAspect();
	}

	/**
	 * Возвращает аспект при вызове аннотированных {@link FixAction} методов.
	 *
	 * @return аспект.
	 */
	@Bean
	public ExecuteAnnotationFixActionMethodAspect executeAnnotationFixActionMethodAspect() {
		return new ExecuteAnnotationFixActionMethodAspect();
	}
}
