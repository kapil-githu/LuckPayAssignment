package com.assignment.loanAccount.LuckPayAssignment.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Component
public class ExternalLoanApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ExternalLoanApiClient.class);
    private static final String BASE_URL = "https://demo9993930.mockable.io/loanaccount/";

    private RestTemplate restTemplate;

    public ExternalLoanApiClient() {
        this.restTemplate = createRestTemplateWithDisabledSSL();
        logger.info("ExternalLoanApiClient initialized with SSL disabled for development");
    }

    private RestTemplate createRestTemplateWithDisabledSSL() {
        logger.debug("Creating RestTemplate with disabled SSL validation");

        try {
            // Create a trust manager that accepts all certificates
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] xcs, String string) { }
                        public void checkServerTrusted(X509Certificate[] xcs, String string) { }
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            HostnameVerifier allHostsValid = (hostname, session) -> true;

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            logger.debug("SSL validation disabled successfully");
            return new RestTemplate();

        } catch (Exception e) {
            logger.error("Failed to create RestTemplate with disabled SSL", e);
            throw new RuntimeException("Failed to create RestTemplate with disabled SSL", e);
        }
    }

    public LoanApiResponse fetchLoanDetails(String loanAccountNumber) {
        String url = BASE_URL + loanAccountNumber;
        logger.info("Making external API call to fetch loan details for account: {}", loanAccountNumber);
        logger.debug("External API URL: {}", url);

        long startTime = System.currentTimeMillis();

        try {
            logger.debug("Sending GET request to external API");
            LoanApiResponse response = restTemplate.getForObject(url, LoanApiResponse.class);

            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;

            if (response != null) {
                logger.info("Successfully received response from external API for account: {} in {}ms",
                        loanAccountNumber, responseTime);
                logger.debug("Response contains {} EMI details",
                        response.getEmiDetails() != null ? response.getEmiDetails().size() : 0);

                // Log EMI details at debug level
                if (response.getEmiDetails() != null) {
                    response.getEmiDetails().forEach(emi ->
                            logger.debug("EMI Detail - Month: {}, Amount: {}, Paid: {}, Due: {}",
                                    emi.getMonth(), emi.getEmiAmount(), emi.isPaidStatus(), emi.isDueStatus())
                    );
                }

            } else {
                logger.warn("External API returned null response for account: {} in {}ms",
                        loanAccountNumber, responseTime);
            }

            return response;

        } catch (HttpClientErrorException e) {
            logger.error("Client error while calling external API for account: {}, Status: {}, Response: {}",
                    loanAccountNumber, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("External API client error", e);

        } catch (HttpServerErrorException e) {
            logger.error("Server error while calling external API for account: {}, Status: {}, Response: {}",
                    loanAccountNumber, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("External API server error", e);

        } catch (RestClientException e) {
            logger.error("Network error while calling external API for account: {}",
                    loanAccountNumber, e);
            throw new RuntimeException("External API network error", e);

        } catch (Exception e) {
            logger.error("Unexpected error while calling external API for account: {}",
                    loanAccountNumber, e);
            throw new RuntimeException("Unexpected external API error", e);
        }
    }
}
