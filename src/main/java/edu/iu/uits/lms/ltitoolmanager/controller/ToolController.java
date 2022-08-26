package edu.iu.uits.lms.ltitoolmanager.controller;

import edu.iu.uits.lms.canvas.model.ExternalTool;
import edu.iu.uits.lms.canvas.services.ExternalToolsService;
import edu.iu.uits.lms.lti.LTIConstants;
import edu.iu.uits.lms.lti.controller.OidcTokenAwareController;
import edu.iu.uits.lms.lti.service.OidcTokenUtils;
import edu.iu.uits.lms.ltitoolmanager.config.ToolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ox.ctl.lti13.security.oauth2.client.lti.authentication.OidcAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/app")
@Slf4j
public class ToolController extends OidcTokenAwareController {

   @Autowired
   private ToolConfig toolConfig;

   @Autowired
   private ExternalToolsService externalToolsService;

   private final String editButtonlaunchUrl = "https://www.edu-apps.org/redirect";

   @RequestMapping("/launch")
   public String launch(Model model, SecurityContextHolderAwareRequestWrapper request) {
      log.info("In launch");
      OidcAuthenticationToken token = getTokenWithoutContext();

      OidcTokenUtils oidcTokenUtils = new OidcTokenUtils(token);

      String courseId = oidcTokenUtils.getCourseId();

      return loading(courseId, model);
   }

   private String loading(String courseId, Model model) {
      log.info("In loading");
      model.addAttribute("courseId", courseId);
      model.addAttribute("hideFooter", true);
      return "loading";
   }

   @RequestMapping("/index/{courseId}")
   @Secured(LTIConstants.INSTRUCTOR_AUTHORITY)
   public ModelAndView index(@PathVariable("courseId") String courseId, Model model, HttpServletRequest request) {
      log.debug("in /index");
      getValidatedToken(courseId);

      List<ExternalTool> externalToolsList = externalToolsService.getExternalTools(courseId);
      model.addAttribute("externalToolsList", externalToolsList);
      model.addAttribute("editButtonlaunchUrl", editButtonlaunchUrl);

      return new ModelAndView("index");
   }

   @RequestMapping(value = "/{courseId}/delete/{id}", method = RequestMethod.POST)
   @Secured(LTIConstants.INSTRUCTOR_AUTHORITY)
   public ModelAndView deleteTool(@PathVariable("courseId") String courseId, @PathVariable("id") String toolId, Model model, HttpServletRequest request) {
      log.debug("in /deleteTool");
      getValidatedToken(courseId);

      // delete the external tool
      ExternalTool externalTool = externalToolsService.deleteExternalTool(courseId, toolId);

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
      getValidatedToken(courseId);

      if (toolName == null || "".equals(toolName) || redirectUrl == null || "".equals(redirectUrl)) {
         model.addAttribute("error", "Tool not saved. Tool Name or Redirect URL was empty.");
         return index(courseId, model, request);
      }

      boolean isNewTab = true;

      if (redirectUrl.contains("instructure.com")) {
         isNewTab = false;
      }

      // edit the external tool
      ExternalTool externalTool = externalToolsService.editExternalTool(courseId, toolId, toolName, redirectUrl, isNewTab, isCourseNav);

      if (externalTool != null) {
         model.addAttribute("success", "Updated tool: " + externalTool.getName());
      } else {
         // TODO not sure what a legit Canvas failure looks like, so using a generic message
         model.addAttribute("error", "There was an issue updating or deleting the tool. Please try again.");
      }

      return index(courseId, model, request);
   }
}
