package it.caldesi.sample.exchange.service;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.caldesi.sample.exchange.data.domain.ExchangeRate;
import it.caldesi.sample.exchange.data.repository.ExchangeRateRepository;

@Service
public class ExchangeRateService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ExchangeRateRepository exchangeRateRepository;

	@Transactional
	public void updateRates(Collection<ExchangeRate> rates) {
		log.debug("Deleting all rates");
		exchangeRateRepository.findAll().forEach(rate -> exchangeRateRepository.delete(rate));
		entityManager.flush();
		
		log.debug("Writing new rates");
		rates.stream().forEach(rate -> entityManager.persist(rate));
	}

	public List<ExchangeRate> getAllRates() {
		return exchangeRateRepository.findAll();
	}

}
