package it.caldesi.sample.exchange.data.repository;

import org.springframework.data.repository.CrudRepository;

import it.caldesi.sample.exchange.data.domain.ExchangeRate;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {

	ExchangeRate findByCurrency(String currency);

	Iterable<ExchangeRate> findAll();

	void delete(ExchangeRate exchangeRate);

}
