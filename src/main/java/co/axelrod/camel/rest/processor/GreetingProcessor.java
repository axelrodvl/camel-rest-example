package co.axelrod.camel.rest.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 01.06.2018.
 */
public class GreetingProcessor implements Processor {
    public void process(Exchange exchange) {
        String requestBody = exchange.getIn().getBody(String.class);
        exchange.getIn().setBody("Hello! Your request: " + requestBody);
    }
}
