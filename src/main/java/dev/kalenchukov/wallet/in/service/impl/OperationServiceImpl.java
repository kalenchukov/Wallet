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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс сервиса операций.
 */
@Service
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
	@Autowired
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
	public Operation add(long playerId, long accountId, final OperationType operationType, final BigDecimal amount) {
		Objects.requireNonNull(operationType);
		Objects.requireNonNull(amount);

		return this.operationRepository.save(
				new Operation(0L, playerId, accountId, operationType, amount)
		);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId    {@inheritDoc}
	 * @param accountId   {@inheritDoc}
	 * @param operationId {@inheritDoc}
	 * @return {@inheritDoc}
	 * @throws NotFoundOperationException {@inheritDoc}
	 */
	public Operation findById(final long playerId, final long accountId, final long operationId) throws NotFoundOperationException {
		Optional<Operation> operation = this.operationRepository.findById(playerId, accountId, operationId);
		return operation.orElseThrow(() -> new NotFoundOperationException(operationId));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param playerId  {@inheritDoc}
	 * @param accountId {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public List<Operation> find(final long playerId, final long accountId) {
		return this.operationRepository.find(playerId, accountId);
	}
}
