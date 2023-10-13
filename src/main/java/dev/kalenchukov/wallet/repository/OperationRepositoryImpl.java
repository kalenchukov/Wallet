/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.repository;

import dev.kalenchukov.wallet.entity.Operation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс хранилища операций.
 */
public class OperationRepositoryImpl implements OperationRepository {
	/**
	 * Хранилище операций.
	 */
	private static Set<Operation> DATA = new HashSet<>();

	/**
	 * Конструирует хранилище операций.
	 */
	public OperationRepositoryImpl() {
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param operation {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Operation save(final Operation operation) {
		Objects.requireNonNull(operation);
		DATA.add(operation);
		return operation;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param operationId {@inheritDoc}
	 * @param playerId    {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	public Optional<Operation> getById(long operationId, final long playerId) {
		return DATA.stream()
				.filter(elm -> (Objects.equals(elm.getOperationId(), operationId) &&
						Objects.equals(elm.getAccount().getPlayerId(), playerId)))
				.findFirst();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param accountId {@inheritDoc}
	 * @param playerId  {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Set<Operation> find(final long accountId, final long playerId) {
		return DATA.stream()
				.filter(elm -> (Objects.equals(elm.getAccount().getAccountId(), accountId) &&
						Objects.equals(elm.getAccount().getPlayerId(), playerId)))
				.collect(Collectors.toUnmodifiableSet());
	}
}
