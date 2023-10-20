/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

package dev.kalenchukov.wallet.in.service.impl;

import dev.kalenchukov.wallet.entity.Operation;
import dev.kalenchukov.wallet.exceptions.NotFoundOperationException;
import dev.kalenchukov.wallet.in.service.OperationService;
import dev.kalenchukov.wallet.repository.OperationRepository;
import dev.kalenchukov.wallet.type.OperationType;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Класс сервиса операций.
 */
public class OperationServiceImpl implements OperationService {
	/**
	 * Хранилище операций.
	 */
	private final OperationRepository operationRepository;

	/**
	 * Конструирует сервис операций.
	 *
	 * @param operationRepository хранилище операций.
	 */
	public OperationServiceImpl(final OperationRepository operationRepository) {
		Objects.requireNonNull(operationRepository);

		this.operationRepository = operationRepository;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId      {@inheritDoc}
	 * @param accountId     {@inheritDoc}
	 * @param operationType {@inheritDoc}
	 * @param amount        {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Operation add(long playerId, long accountId, OperationType operationType, BigDecimal amount) {
		Objects.requireNonNull(operationType);
		Objects.requireNonNull(amount);

		return this.operationRepository.save(
				new Operation(playerId, accountId, operationType, amount)
		);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param operationId {@inheritDoc}
	 * @param playerId    {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NotFoundOperationException {@inheritDoc}
	 */
	public Operation findById(final long operationId, final long playerId)
			throws NotFoundOperationException {
		Optional<Operation> operation = this.operationRepository.findById(operationId, playerId);

		return operation.orElseThrow(() -> new NotFoundOperationException(operationId));
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
		return this.operationRepository.find(accountId, playerId);
	}
}
