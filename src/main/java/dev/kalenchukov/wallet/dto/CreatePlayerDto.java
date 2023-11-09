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
 * Класс добавления игрока.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность создания игрока")
public class CreatePlayerDto {
	/**
	 * Имя.
	 */
	@Schema(description = "Имя игрока", example = "Petya")
	@NotNull(message = "Имя не должно быть null.")
	@Length(min = 1, max = 100, message = "Имя должно быть от 1 до 100 символов.")
	@Pattern(regexp = "[a-zA-Z]+", message = "Имя должно быть из латинских символов.")
	private String name;

	/**
	 * Пароль.
	 */
	@Schema(description = "Пароль игрока", example = "^%Vr65V^678b8fo")
	@NotNull(message = "Пароль не должен быть null.")
	@NotEmpty(message = "Пароль не должен быть пустым.")
	private String password;
}
