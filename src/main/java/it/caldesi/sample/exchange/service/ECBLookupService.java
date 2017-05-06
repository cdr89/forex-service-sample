package it.caldesi.sample.exchange.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.caldesi.sample.exchange.data.domain.ExchangeRate;
import it.caldesi.sample.exchange.data.xml.Cube;
import it.caldesi.sample.exchange.data.xml.Envelope;
import it.caldesi.sample.exchange.data.xml.TimeCube;

@Service
public class ECBLookupService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${ecb.webservice.url}")
	private String url;

	private final RestTemplate restTemplate;

	public ECBLookupService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Async
	public Collection<ExchangeRate> getUpdatedRates() throws Exception {
		log.info("Start getting updated exchage rates");

		log.debug("getting Envelope");
		Envelope envelope = getEnvelope();

		log.debug("transform Envelope into collection of ExchangeRate");
		Collection<ExchangeRate> rates = transformEnvelopeToExchangeRates(envelope);

		return rates;
	}

	private Collection<ExchangeRate> transformEnvelopeToExchangeRates(Envelope envelope) throws Exception {
		LinkedList<ExchangeRate> rates = new LinkedList<>();

		List<TimeCube> timeCubes = envelope.getTimeCubeList().getTimeCube();
		timeCubes.stream().forEach(timeCube -> transformTimeCube(timeCube, rates));

		return rates;
	}

	private void transformTimeCube(TimeCube timeCube, LinkedList<ExchangeRate> rates) {
		Date date = parseDate(timeCube);
		timeCube.getCubes().stream().forEach(cube -> transformCube(cube, date, rates));
	}

	private Date parseDate(TimeCube timeCube) {
		String timeString = timeCube.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = simpleDateFormat.parse(timeString);
		} catch (ParseException e) {
			log.error("Cannot parse date: " + timeString, e);
			return null;
		}
		return date;
	}

	private void transformCube(Cube cube, Date date, LinkedList<ExchangeRate> rates) {
		ExchangeRate exchangeRate = new ExchangeRate(cube.getCurrency(), cube.getRate(), date);
		rates.add(exchangeRate);
	}

	private Envelope getEnvelope() {
		Envelope envelope = restTemplate.getForObject(url, Envelope.class);

		return envelope;
	}

}
