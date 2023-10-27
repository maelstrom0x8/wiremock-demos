package com.aeflheim.wiremockspring.common;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.BindMode;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@Testcontainers
public abstract class AbstractWireMockTest {

    @Container
    protected static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock")
        .withExposedPorts(8080);

    static {
        wiremockServer.addFileSystemBind("./src/test/resources", "/home/wiremock", BindMode.READ_ONLY);
        wiremockServer.start();
    }

    @BeforeAll
    static void configure() {
        configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());
        RestAssured.baseURI = wiremockServer.getBaseUrl();
    }
}
