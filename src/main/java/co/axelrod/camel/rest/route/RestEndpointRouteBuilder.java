package co.axelrod.camel.rest.route;

import co.axelrod.camel.rest.processor.GreetingProcessor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 31.05.2018.
 */
public class RestEndpointRouteBuilder extends RouteBuilder {
    public static final Integer PORT = 9977;
    public static final String CONTEXT_PATH = "/camel";
    public static final String SWAGGER_PATH = "/swagger";
    public static final String CONTENT_TYPE = "text/plain";

    public void configure() {
        restConfiguration()
                .component("spark-rest")
                .scheme("http")
                .port(PORT)
                .contextPath(CONTEXT_PATH)
                .apiContextPath(SWAGGER_PATH)
                .apiProperty("api.title", "Camel REST example with Spark REST")
                .apiProperty("api.version", "1");

        rest("/hi")
                .post()
                .consumes(CONTENT_TYPE)
                .produces(CONTENT_TYPE)
                .to("direct:greeting");

        from("direct:greeting")
                .id("greeting")
                .process(new GreetingProcessor());
    }
}
