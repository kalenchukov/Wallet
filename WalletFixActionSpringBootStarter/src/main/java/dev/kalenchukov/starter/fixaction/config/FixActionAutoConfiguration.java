/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.starter.fixaction.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Класс конфигурации стартера.
 */
@ConditionalOnClass(name = "dev.kalenchukov.wallet.WalletApplication")
@AutoConfigureAfter(NamedParameterJdbcTemplate.class)
@ComponentScan("dev.kalenchukov.starter.fixaction")
public class FixActionAutoConfiguration {

}
