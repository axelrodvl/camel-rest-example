package co.axelrod.camel.rest.route;

import co.axelrod.camel.rest.route.util.HttpUtils;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 01.06.2018.
 */
@RunWith(CamelSpringRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/camel-context.xml"})
@UseAdviceWith()
public class RestEndpointRouteBuilderTest extends CamelTestSupport {
    private static final String TEST_BODY = "test";

    private NotifyBuilder notifyBuilder;

    @Autowired
    private CamelContext context;

    @Produce(uri = "direct:greeting")
    private ProducerTemplate producerTemplate;

    @EndpointInject(uri = "mock:routeEnd")
    private MockEndpoint mock;

    @Before
    public void setUp() throws Exception {
        context.start();

        // Have to wait, because of starting spark-rest component in different thread
        Thread.sleep(1000);
    }

    @Test
    public void greetingRouteTest() throws Exception {
        addMockToEndOfRoute("greeting");

        notifyBuilder = new NotifyBuilder(context)
                .whenDone(1)
                .create();

        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived("Hello! Your request: " + TEST_BODY);

        producerTemplate.sendBody(TEST_BODY);

        notifyBuilder.matches(1, TimeUnit.SECONDS);
        mock.assertIsSatisfied();
    }

    @Test
    public void testEndpoint() throws IOException {
        URL url = new URL("http://localhost:"
                + RestEndpointRouteBuilder.PORT
                + RestEndpointRouteBuilder.CONTEXT_PATH
                + "/hi");

        assertEquals("Endpoint returned invalid response",
                "Hello! Your request: " + TEST_BODY,
                HttpUtils.post(url, RestEndpointRouteBuilder.CONTENT_TYPE, TEST_BODY));
    }

    @Test
    public void testSwagger() throws IOException {
        URL url = new URL("http://localhost:"
                + RestEndpointRouteBuilder.PORT
                + RestEndpointRouteBuilder.CONTEXT_PATH
                + RestEndpointRouteBuilder.SWAGGER_PATH);

        String expectedSwagger = new String(
                Files.readAllBytes(Paths.get("src/test/resources/swagger.json")));

        assertEquals("Endpoint returned invalid Swagger endpoint description",
                expectedSwagger,
                HttpUtils.get(url));
    }

    private void addMockToEndOfRoute(String routeId) throws Exception {
        context.getRouteDefinition(routeId)
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() {
                        weaveAddLast().to(mock);
                    }
                });
    }
}