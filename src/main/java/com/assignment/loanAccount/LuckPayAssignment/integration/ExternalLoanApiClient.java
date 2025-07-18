package com.assignment.loanAccount.LuckPayAssignment.integration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Component
public class ExternalLoanApiClient {

    private RestTemplate restTemplate;

    public ExternalLoanApiClient() {
        this.restTemplate = createRestTemplateWithDisabledSSL();
    }

    private RestTemplate createRestTemplateWithDisabledSSL() {
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

            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            // Create hostname verifier that accepts all hostnames
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Set default SSL context and hostname verifier
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            return new RestTemplate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create RestTemplate with disabled SSL", e);
        }
    }

    public LoanApiResponse fetchLoanDetails(String loanAccountNumber) {
        String url = "https://demo9993930.mockable.io/loanaccount/" + loanAccountNumber;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, LoanApiResponse.class);
    }
}
