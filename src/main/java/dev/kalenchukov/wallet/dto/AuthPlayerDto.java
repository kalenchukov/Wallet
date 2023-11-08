/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Класс авторизации игрока.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность авторизации игрока")
public class AuthPlayerDto {
	/**
	 * Имя.
	 */
	@Schema(description = "Имя игрока", example = "Vasya")
	@NotNull(message = "Имя не должно быть null.")
	@Length(min = 1, max = 100, message = "Имя должно быть от 1 до 100 символов.")
	@Pattern(regexp = "[a-zA-Z]+", message = "Имя должно быть из латинских символов.")
	private String name;

	/**
	 * Пароль.
	 */
	@Schema(description = "Пароль игрока", example = "R65^%r65r65")
	@NotNull(message = "Пароль не должен быть null.")
	@NotEmpty(message = "Пароль не должен быть пустым.")
	private String password;
}
