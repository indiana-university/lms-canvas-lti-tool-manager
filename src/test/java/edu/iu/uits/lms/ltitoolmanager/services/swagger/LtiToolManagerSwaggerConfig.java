package edu.iu.uits.lms.ltitoolmanager.services.swagger;

import edu.iu.uits.lms.lti.config.LtiClientTestConfig;
import edu.iu.uits.lms.lti.config.SwaggerConfig;
import edu.iu.uits.lms.lti.swagger.SwaggerTestingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Import({
            SwaggerConfig.class,
            LtiClientTestConfig.class
})
public class LtiToolManagerSwaggerConfig {
    @Bean
    public SwaggerTestingBean swaggerTestingBean() {
        SwaggerTestingBean stb = new SwaggerTestingBean();

        List<String> expandedList = new ArrayList<>();

        stb.setEmbeddedSwaggerToolPaths(expandedList);
        return stb;
    }
}