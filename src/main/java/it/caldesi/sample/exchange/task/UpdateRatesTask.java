package it.caldesi.sample.exchange.task;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.caldesi.sample.exchange.data.domain.ExchangeRate;
import it.caldesi.sample.exchange.service.ECBLookupService;
import it.caldesi.sample.exchange.service.ExchangeRateService;

@Component
public class UpdateRatesTask {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ECBLookupService ecbLookupService;

	@Autowired
	private ExchangeRateService exchangeRateService;

	@Scheduled(fixedRateString = "${update.task.rate}")
	public void updateExchangeRates() {
		log.info("Updating exchange rates");
		try {
			Collection<ExchangeRate> updatedRates = ecbLookupService.getUpdatedRates();
			exchangeRateService.updateRates(updatedRates);
			log.info("Rates updated");
		} catch (Exception e) {
			log.error("Cannot update rates", e);
		}
	}

}
