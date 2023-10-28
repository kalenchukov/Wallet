/*
 * Copyright © 2023 Алексей Каленчуков
 * GitHub: https://github.com/kalenchukov
 * E-mail: mailto:aleksey.kalenchukov@yandex.ru
 */

/**
 * Предоставляет классы, необходимые для конкретизации исключений.
 * <p>
 * Exception
 * |
 * --------------------
 * ApplicationException
 * ------------------------------------------------------------------------------------------
 * /                              |                               |                           \
 * AccountException               OperationException               PlayerException             AccessTokenException
 * |                                 |                               |                           |
 * ---------------------------  -------------------------------  ------------------------------  ---------------------------
 * OutOfAmountAccountException  InvalidAmountOperationException  InvalidPasswordPlayerException  InvalidAccessTokenException
 * InvalidIdAccountException    InvalidIdOperationException      DuplicateNamePlayerException
 * NoAccessAccountException     NotFoundOperationException       InvalidIdPlayerException
 * NotFoundAccountException     NegativeAmountOperationException InvalidNamePlayerException
 * NeedAuthPlayerException
 * NotFoundPlayerException
 * </p>
 */
package dev.kalenchukov.wallet.exceptions;