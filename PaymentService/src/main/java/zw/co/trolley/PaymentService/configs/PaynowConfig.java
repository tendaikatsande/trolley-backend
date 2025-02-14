package zw.co.trolley.PaymentService.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import zw.co.paynow.core.Paynow;

@Configuration
public class PaynowConfig {

    @Value("${paynow.integrationId}")
    private String integrationId;
    @Value("${paynow.integrationKey}")
    private String integrationKey;

    @Bean
    public Paynow paynow() {

        // log keys
        System.out.println("Integration ID: " + integrationId);
        System.out.println("Integration Key: " + integrationKey);
        Paynow paynow = new Paynow(integrationId, integrationKey);
        paynow.setResultUrl("http://example.com/gateways/paynow/update");
        paynow.setReturnUrl("http://example.com/return?gateway=paynow&merchantReference=1234");
        return paynow;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
