package dev.kalenchukov.wallet;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * Класс конфигурации Swagger.
 */
public class Swagger implements WebMvcConfigurer {
	/**
	 * {@inheritDoc}
	 *
	 * @param registry {@inheritDoc}
	 */
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
				.resourceChain(false);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param registry {@inheritDoc}
	 */
	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/swagger-ui/")
				.setViewName("forward:" + "/swagger-ui/index.html");
	}

	/**
	 * Конфигурация Swagger.
	 *
	 * @return конфигурацию.
	 */
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.OAS_30)
				.useDefaultResponseMessages(false)
				.apiInfo(this.apiInfo());
	}

	/**
	 * Возвращает информацию о проекте.
	 *
	 * @return информацию о проекте.
	 */
	public ApiInfo apiInfo() {
		return new ApiInfo("Wallet API",
				"Документация проекта по управлению счетами игроков",
				"4.0.0",
				"",
				this.contact(),
				"",
				"",
				Collections.emptyList());
	}

	/**
	 * Возвращает контакт по вопросам API проекта.
	 *
	 * @return контакт по вопросам API проекта.
	 */
	public Contact contact() {
		return new Contact(
				"Алексей Каленчуков",
				"https://github.com/kalenchukov",
				"aleksey.kalenchukov@yandex.ru"
		);
	}
}
