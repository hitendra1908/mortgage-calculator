package com.ing.api.mortgage;

import com.ing.api.mortgage.dto.MortgageCheckRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
class MortgageIntegrationTest {

	@LocalServerPort
	private Integer port;

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
			"postgres:15-alpine"
	);

	@BeforeEach
	public void setUp() {
		RestAssured.baseURI = "http://localhost:" + port + "/api";
	}

	@Test
	void testGetAllMortgageRates() {
		given()
				.contentType(ContentType.JSON)
				.when()
				.get("/interest-rates")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body(
						".", hasSize(5),
						"[0].maturityPeriod", equalTo(5)
				);

	}

	@Test
	void testCheckMortgage() {
		final MortgageCheckRequest request = MortgageCheckRequest.builder()
				.income(BigDecimal.valueOf(5000))
				.loanValue(BigDecimal.valueOf(20000))
				.homeValue(BigDecimal.valueOf(20000))
				.maturityPeriod(10)
				.build();

		given()
				.contentType(ContentType.JSON)
				.body(request)
				.when()
				.post("/mortgage-check")
				.then()
				.statusCode(200)
				.body(containsString("\"monthlyCost\":212.13"), containsString("\"feasible\":true"));
	}

}
