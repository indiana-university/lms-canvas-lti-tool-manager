package edu.iu.uits.lms.ltitoolmanager.services.swagger;

import edu.iu.uits.lms.lti.service.LmsDefaultGrantedAuthoritiesMapper;
import edu.iu.uits.lms.lti.swagger.AbstractSwaggerCustomTest;
import edu.iu.uits.lms.lti.swagger.AbstractSwaggerDisabledTest;
import edu.iu.uits.lms.lti.swagger.AbstractSwaggerEmbeddedToolTest;
import edu.iu.uits.lms.lti.swagger.AbstractSwaggerUiCustomTest;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.NestedTestConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.test.context.NestedTestConfiguration.EnclosingConfiguration.INHERIT;


@NestedTestConfiguration(INHERIT)
public class SwaggerSuiteTest {

    @MockitoBean
    private BufferingApplicationStartup bufferingApplicationStartup;

    @MockitoBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockitoBean
    private LmsDefaultGrantedAuthoritiesMapper lmsDefaultGrantedAuthoritiesMapper;

    @MockitoBean
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Nested
    @SpringBootTest(classes = {LtiToolManagerSwaggerConfig.class})
    public class SwaggerCustomTest extends AbstractSwaggerCustomTest {

    }

    @Nested
    @SpringBootTest(classes = {LtiToolManagerSwaggerConfig.class})
    public class SwaggerDisabledTest extends AbstractSwaggerDisabledTest {

    }

    @Nested
    @SpringBootTest(classes = {LtiToolManagerSwaggerConfig.class})
    public class SwaggerEmbeddedToolTest extends AbstractSwaggerEmbeddedToolTest {

    }

    @Nested
    @SpringBootTest(classes = {LtiToolManagerSwaggerConfig.class})
    public class SwaggerUiCustomTest extends AbstractSwaggerUiCustomTest {

    }
}