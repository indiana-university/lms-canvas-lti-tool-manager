package edu.iu.uits.lms.ltitoolmanager.controller;

import canvas.client.generated.api.CoursesApi;
import canvas.client.generated.model.ExternalTool;
import edu.iu.uits.lms.lti.LTIConstants;
import edu.iu.uits.lms.lti.controller.LtiAuthenticationTokenAwareController;
import edu.iu.uits.lms.lti.security.LtiAuthenticationProvider;
import edu.iu.uits.lms.lti.security.LtiAuthenticationToken;
import edu.iu.uits.lms.ltitoolmanager.config.ToolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/app")
@Slf4j
public class ToolController extends LtiAuthenticationTokenAwareController {

   @Autowired
   private ToolConfig toolConfig;

   @Autowired
   private CoursesApi coursesApi;

   private final String editButtonlaunchUrl = "https://www.edu-apps.org/redirect";

   @RequestMapping("/loading/{courseId}")
   public String loading(@PathVariable("courseId") String courseId, Model model) {
      model.addAttribute("courseId", courseId);
      model.addAttribute("hideFooter", true);
      return "loading";
   }

   @RequestMapping("/index/{courseId}")
   @Secured(LTIConstants.INSTRUCTOR_AUTHORITY)
   public ModelAndView index(@PathVariable("courseId") String courseId, Model model, HttpServletRequest request) {
      log.debug("in /index");
      LtiAuthenticationToken token = getValidatedToken(courseId);

      List<ExternalTool> externalToolsList = coursesApi.getExternalTools(courseId);
      model.addAttribute("externalToolsList", externalToolsList);
      model.addAttribute("editButtonlaunchUrl", editButtonlaunchUrl);

      return new ModelAndView("index");
   }

   @RequestMapping(value = "/{courseId}/delete/{id}", method = RequestMethod.POST)
   @Secured(LTIConstants.INSTRUCTOR_AUTHORITY)
   public ModelAndView deleteTool(@PathVariable("courseId") String courseId, @PathVariable("id") String toolId, Model model, HttpServletRequest request) {
      log.debug("in /deleteTool");
      LtiAuthenticationToken token = getValidatedToken(courseId);

      // delete the external tool
      ExternalTool externalTool = coursesApi.deleteExternalTool(courseId, toolId);

      if ("deleted".equals(externalTool.getWorkflowState())) {
         model.addAttribute("success", "Deleted tool: " + externalTool.getName());
      } else {
         // TODO not sure what a legit Canvas failure looks like, so using a generic message
         model.addAttribute("error", "There was an issue updating or deleting the tool. Please try again.");
      }

      return index(courseId, model, request);
   }

   @RequestMapping(value = "/{courseId}/edit/{id}", method = RequestMethod.POST)
   @Secured(LTIConstants.INSTRUCTOR_AUTHORITY)
   public ModelAndView editTool(@PathVariable("courseId") String courseId, @PathVariable("id") String toolId, Model model, HttpServletRequest request,
                                @RequestParam(value="tool-name", required = false) String toolName,
                                @RequestParam(value="redirect-url", required = false) String redirectUrl,
                                @RequestParam(value="course-nav-checkbox", required = false) boolean isCourseNav) {
      log.debug("in /editTool");
      LtiAuthenticationToken token = getValidatedToken(courseId);

      // edit the external tool
      ExternalTool externalTool = coursesApi.editExternalTool(courseId, toolId, toolName, redirectUrl, isCourseNav);

      if (externalTool != null) {
         model.addAttribute("success", "Updated tool: " + externalTool.getName());
      } else {
         // TODO not sure what a legit Canvas failure looks like, so using a generic message
         model.addAttribute("error", "There was an issue updating or deleting the tool. Please try again.");
      }

      return index(courseId, model, request);
   }
}
