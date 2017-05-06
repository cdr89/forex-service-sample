package it.caldesi.sample.exchange.data.repository;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import it.caldesi.sample.exchange.data.domain.ExchangeRate;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ExchangeRateRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ExchangeRateRepository exchangeRateRepository;

	@Test
	public void testFindByName() {
		Double excRate = 1.0;
		entityManager.persist(new ExchangeRate("EUR", excRate, new Date()));

		ExchangeRate rate = exchangeRateRepository.findByCurrency("EUR");
		assertEquals(excRate, rate.getRate());
	}

}
