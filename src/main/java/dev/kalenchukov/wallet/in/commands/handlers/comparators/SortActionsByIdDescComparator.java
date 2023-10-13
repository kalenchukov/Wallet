/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.commands.handlers.comparators;

import dev.kalenchukov.wallet.entity.Action;

import java.util.Comparator;

/**
 * Класс реализации сравнения для сортировки действий.
 */
public class SortActionsByIdDescComparator implements Comparator<Action> {
	@Override
	public int compare(final Action o1, final Action o2) {
		return Long.compare(o2.getActionId(), o1.getActionId());
	}
}
