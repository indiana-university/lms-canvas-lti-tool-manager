package edu.iu.uits.lms.ltitoolmanager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "lti-tool-manager")
@Getter
@Setter
public class ToolConfig {

   private String version;
   private String env;
}
