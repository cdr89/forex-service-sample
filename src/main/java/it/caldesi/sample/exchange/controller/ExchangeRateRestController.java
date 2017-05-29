package it.caldesi.sample.exchange.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.caldesi.sample.exchange.data.domain.ExchangeRate;
import it.caldesi.sample.exchange.service.ExchangeRateService;

@RestController
@RequestMapping("/api/exchangeRate")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExchangeRateRestController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExchangeRateService exchangeRateService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<List<ExchangeRate>> listAllRates() {
		log.debug("Listing all rates");
		List<ExchangeRate> rates = exchangeRateService.getAllRates();

		if (rates == null || rates.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<ExchangeRate>>(rates, HttpStatus.OK);
	}

	@RequestMapping(value = "/all/{date}", method = RequestMethod.GET)
	public ResponseEntity<List<ExchangeRate>> getAllByDate(
			@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		log.debug("Get all by date" + date);
		List<ExchangeRate> rates = exchangeRateService.getAllRatesByDate(date);

		if (rates == null || rates.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<ExchangeRate>>(rates, HttpStatus.OK);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<ExchangeRate>> getAllByCurrency(@RequestParam("cur") String currency) {
		log.debug("Get all by currency" + currency);
		List<ExchangeRate> rates = exchangeRateService.getAllRatesByCurrency(currency);

		if (rates == null || rates.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<ExchangeRate>>(rates, HttpStatus.OK);
	}

	@RequestMapping(value = "/{date}", method = RequestMethod.GET)
	public ResponseEntity<ExchangeRate> getByDateAndCurrency(
			@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			@RequestParam("cur") String currency) {
		log.debug("Get by date: " + date + " and currency: " + currency);
		ExchangeRate rate = exchangeRateService.getByDateAndCurrency(date, currency);

		if (rate == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<ExchangeRate>(rate, HttpStatus.OK);
	}

	// CONVERSION
	@RequestMapping(value = "/convert", method = RequestMethod.GET)
	public ResponseEntity<Double> convertByDateAndCurrency(
			@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			@RequestParam("fromCurrency") String fromCurrency,
			@RequestParam("toCurrency") String toCurrency,
			@RequestParam("amount") Double amount) {
		log.debug("Convert by date: {}, from {}, to {}, amount:{}", date, fromCurrency, toCurrency, amount);

		Double value = exchangeRateService.convert(date, fromCurrency, toCurrency, amount);
		
		if (value == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Double>(value, HttpStatus.OK);
	}

}
