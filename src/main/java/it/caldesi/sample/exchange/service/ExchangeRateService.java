package it.caldesi.sample.exchange.service;

import java.util.Collection;
import java.util.Date;
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

	public List<ExchangeRate> getAllRatesByDate(Date date) {
		return exchangeRateRepository.findAllByDate(date);
	}

	public List<ExchangeRate> getAllRatesByCurrency(String currency) {
		return exchangeRateRepository.findAllByCurrency(currency);
	}

	public ExchangeRate getByDateAndCurrency(Date date, String currency) {
		return exchangeRateRepository.findByDateAndCurrency(date, currency);
	}

	public Double convert(Date date, String fromCurrency, String toCurrency, Double amount) {
		if (date == null) {
			date = new Date();
		}

		double fromExchangeRateVal = 1f;
		double toExchangeRateVal = 1f;

		try {
			fromExchangeRateVal = getExchangeRateValue(date, fromCurrency, fromExchangeRateVal);
			toExchangeRateVal = getExchangeRateValue(date, toCurrency, toExchangeRateVal);

			return (amount * toExchangeRateVal) / fromExchangeRateVal;
		} catch (Exception e) {
			log.error("Error on conversion", e);
			return null;
		}
	}

	private double getExchangeRateValue(Date date, String currency, double exchangeRateVal) {
		if (!"EUR".equalsIgnoreCase(currency)) {
			ExchangeRate exchangeRate = exchangeRateRepository.findByDateAndCurrency(date, currency);
			exchangeRateVal = exchangeRate.getRate();
		}
		
		return exchangeRateVal;
	}

}
