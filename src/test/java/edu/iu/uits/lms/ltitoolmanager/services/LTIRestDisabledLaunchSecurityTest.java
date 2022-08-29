package edu.iu.uits.lms.ltitoolmanager.services;

import edu.iu.uits.lms.canvas.config.CanvasClientTestConfig;
import edu.iu.uits.lms.lti.AbstractLTIRestDisabledLaunchSecurityTest;
import edu.iu.uits.lms.lti.config.LtiClientTestConfig;
import edu.iu.uits.lms.ltitoolmanager.config.ToolConfig;
import org.springframework.context.annotation.Import;

@Import({ToolConfig.class, CanvasClientTestConfig.class, LtiClientTestConfig.class})
public class LTIRestDisabledLaunchSecurityTest extends AbstractLTIRestDisabledLaunchSecurityTest {

}
