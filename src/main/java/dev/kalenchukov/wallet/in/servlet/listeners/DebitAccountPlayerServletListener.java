/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.servlet.listeners;

import dev.kalenchukov.wallet.in.service.impl.AccountServiceImpl;
import dev.kalenchukov.wallet.in.service.impl.ActionServiceImpl;
import dev.kalenchukov.wallet.in.servlet.DebitAccountPlayerServlet;
import dev.kalenchukov.wallet.modules.DataBase;
import dev.kalenchukov.wallet.repository.impl.AccountRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.ActionRepositoryImpl;
import dev.kalenchukov.wallet.repository.impl.OperationRepositoryImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Класс реализующий регистрацию сервлета.
 */
@WebListener
public class DebitAccountPlayerServletListener implements ServletContextListener {
	/**
	 * {@inheritDoc}
	 *
	 * @param sce {@inheritDoc}
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		context.addServlet("DebitAccountPlayerServlet", new DebitAccountPlayerServlet(
				new ActionServiceImpl(new ActionRepositoryImpl(DataBase.getDataSource())),
				new AccountServiceImpl(
						new AccountRepositoryImpl(DataBase.getDataSource()),
						new OperationRepositoryImpl(DataBase.getDataSource())
				)
		)).addMapping("/players/accounts/debit");
	}
}
