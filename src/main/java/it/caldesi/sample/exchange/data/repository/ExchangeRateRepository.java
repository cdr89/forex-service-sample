package it.caldesi.sample.exchange.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.caldesi.sample.exchange.data.domain.ExchangeRate;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {

	ExchangeRate findByCurrency(String currency);

	List<ExchangeRate> findAll();

	void delete(ExchangeRate exchangeRate);

}
