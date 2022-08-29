package edu.iu.uits.lms.ltitoolmanager.services.swagger;

import edu.iu.uits.lms.lti.swagger.AbstractSwaggerCustomTest;
import edu.iu.uits.lms.ltitoolmanager.WebApplication;
import edu.iu.uits.lms.ltitoolmanager.config.SecurityConfig;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = {WebApplication.class, SecurityConfig.class})
public class SwaggerCustomTest extends AbstractSwaggerCustomTest {

   @Override
   protected List<String> getEmbeddedSwaggerToolPaths() {
      return SwaggerTestUtil.getEmbeddedSwaggerToolPaths(super.getEmbeddedSwaggerToolPaths());
   }
}
