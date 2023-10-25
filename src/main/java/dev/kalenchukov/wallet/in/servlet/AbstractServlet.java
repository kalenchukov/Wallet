/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kalenchukov.wallet.in.service.ActionService;
import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.in.servlet.type.MimeType;
import dev.kalenchukov.wallet.modules.DataBase;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import dev.kalenchukov.wallet.type.ActionType;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Класс обработки абстрактного HTTP-запроса.
 */
public class AbstractServlet extends HttpServlet {
	/**
	 * Средство отображения объектов.
	 */
	protected final ObjectMapper objectMapper;
	/**
	 * Сервис действий.
	 */
	protected final ActionService actionService;

	/**
	 * Конструирует обработчик абстрактного HTTP-запроса.
	 *
	 * @param actionService Сервис действий.
	 */
	public AbstractServlet(final ActionService actionService) {
		this.actionService = actionService;
		this.objectMapper = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * Генерирует ответ на запрос.
	 *
	 * @param data     ответные данные. Предназначены для тела запроса.
	 * @param response объект сервлета для ответа.
	 * @param httpCode HTTP-код ответа.
	 */
	protected void generateResponse(final Object data, final HttpServletResponse response, final int httpCode) {
		Objects.requireNonNull(response);
		if (data != null) {
			try (PrintWriter bodyWriter = response.getWriter()) {
				String json = this.objectMapper.writeValueAsString(data);
				bodyWriter.write(json);
				response.setContentLength(json.getBytes().length);
				response.setContentType(MimeType.JSON.getValue());
				response.setCharacterEncoding("UTF-8");
				response.setStatus(httpCode);
			} catch (Exception exception) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} else {
			response.setStatus(httpCode);
		}
	}

	/**
	 * Фиксирует действия игрока.
	 *
	 * @param playerId         идентификатор игрока.
	 * @param actionType       тип действия.
	 * @param actionTypeStatus статус действия.
	 */
	protected void fixAction(final long playerId, final ActionType actionType, final ActionType.Status actionTypeStatus) {
		Objects.requireNonNull(actionType);
		Objects.requireNonNull(actionTypeStatus);;

		this.actionService.add(playerId, actionType, actionTypeStatus);
	}
}
