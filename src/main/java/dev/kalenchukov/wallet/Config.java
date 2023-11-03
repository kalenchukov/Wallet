/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet;

import dev.kalenchukov.wallet.modules.DataBase;
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
}
