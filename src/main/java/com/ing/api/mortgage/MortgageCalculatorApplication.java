package com.ing.api.mortgage;

import com.ing.api.mortgage.model.MortgageRate;
import com.ing.api.mortgage.repository.MortgageRateRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@SpringBootApplication
public class MortgageCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MortgageCalculatorApplication.class, args);
	}

	/**
	 * Initialize few interest rates on startup.
	 */
	@Bean
	ApplicationRunner init(MortgageRateRepository mortgageRateRepository) {
		return args -> {
			Instant now = Instant.now();
			mortgageRateRepository.saveAll(List.of(
					new MortgageRate(5, new BigDecimal("3.25"), now),
					new MortgageRate(10, new BigDecimal("3.75"), now),
					new MortgageRate(15, new BigDecimal("4.10"), now),
					new MortgageRate(20, new BigDecimal("4.50"), now),
					new MortgageRate(30, new BigDecimal("5.00"), now)
			));
		};
	}
}
