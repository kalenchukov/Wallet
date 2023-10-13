/**
 * Определяет API по управлению счетами игроков.
 */
module dev.kalenchukov.wallet
{
	requires org.apache.commons.codec;

	exports dev.kalenchukov.wallet;
	exports dev.kalenchukov.wallet.entity;
	exports dev.kalenchukov.wallet.resources;
	exports dev.kalenchukov.wallet.exceptions;
	exports dev.kalenchukov.wallet.in.service;
	exports dev.kalenchukov.wallet.repository;
}