package it.caldesi.sample.exchange.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.caldesi.sample.exchange.data.domain.ExchangeRate;
import it.caldesi.sample.exchange.service.ExchangeRateService;

@RestController
@RequestMapping("/api/exchangeRate")
public class ExchangeRateRestController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExchangeRateService exchangeRateService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<List<ExchangeRate>> listAllRates() {
		log.debug("Listing all rates");
		List<ExchangeRate> rates = exchangeRateService.getAllRates();

		if (rates == null || rates.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<ExchangeRate>>(rates, HttpStatus.OK);
	}

}
