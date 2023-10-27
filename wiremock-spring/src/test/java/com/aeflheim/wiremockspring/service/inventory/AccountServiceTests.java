package com.aeflheim.wiremockspring.service.inventory;

import com.aeflheim.wiremockspring.common.AbstractWireMockTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AccountServiceTests extends AbstractWireMockTest {

    @Test
    @DisplayName("Fetching connected accounts for an unauthorized customer")
    void fetchConnectedAccountsUnauthorized() {
        given()
            .pathParam("customerId", "acct_1032D8")
            .when().get("/api/v1/customers/{customerId}")
            .then().statusCode(401)
            .body("message", equalTo("Authentication failed. Please provide valid credentials."));
    }

    @Test
    @DisplayName("Fetch connected accounts for authorized customer")
    void fetchConnectedAccounts() {
        Response response = given()
            .headers("Authorization", "12912")
            .pathParam("customerId", "acct_1032D8")
            .when().get("/api/v1/customers/{customerId}");

        var result = response.as(new TypeRef<Map<String, List<Map<String, Object>>>>() {});

        response.then().statusCode(200);
        assertThat(result.get("accounts")).hasSize(2);
        assertThat(result.get("accounts").get(0).get("id")).isEqualTo("2fc1aa");
        assertThat(result.get("accounts").get(1).get("currency")).isEqualTo("ngn");
    }

}
