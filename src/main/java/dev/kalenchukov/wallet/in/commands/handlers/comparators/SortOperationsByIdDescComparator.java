/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers.comparators;

import dev.kalenchukov.wallet.entity.Operation;

import java.util.Comparator;

/**
 * Класс реализации сравнения для сортировки операций.
 */
public class SortOperationsByIdDescComparator implements Comparator<Operation> {
	/**
	 * {@inheritDoc}
	 *
	 * @param o1 {@inheritDoc}
	 * @param o2 {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public int compare(final Operation o1, final Operation o2) {
		return Long.compare(o2.getOperationId(), o1.getOperationId());
	}
}
