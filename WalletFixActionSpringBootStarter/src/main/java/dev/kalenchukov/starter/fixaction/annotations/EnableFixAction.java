/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.starter.fixaction.annotations;

import dev.kalenchukov.starter.fixaction.config.FixActionAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Аннотация включения функционала фиксации действия игроков.
 *
 * @see FixAction
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(FixActionAutoConfiguration.class)
public @interface EnableFixAction {

}
