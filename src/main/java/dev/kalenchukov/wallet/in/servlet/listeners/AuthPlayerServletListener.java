/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet.listeners;

import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.in.service.impl.PlayerServiceImpl;
import dev.kalenchukov.wallet.in.servlet.AuthPlayerServlet;
import dev.kalenchukov.wallet.modules.DataBase;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.PlayerRepositoryImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Класс реализующий регистрацию сервлета.
 */
@WebListener
public class AuthPlayerServletListener implements ServletContextListener {
	/**
	 * {@inheritDoc}
	 *
	 * @param sce {@inheritDoc}
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		context.addServlet("AuthPlayerServlet", new AuthPlayerServlet(
				new ActionServiceImpl(new ActionRepositoryImpl(DataBase.getDataSource())),
				new PlayerServiceImpl(new PlayerRepositoryImpl(DataBase.getDataSource()))
		)).addMapping("/players/auth");
	}
}
