package com.panopset.re4ms.controllers;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.panopset.re4ms.hibernate.Re4msGroup;
import com.panopset.re4ms.interfaces.Nls;
import com.panopset.re4ms.interfaces.Rank;

@Controller
@RequestMapping("/invites")
public class SkullBonesController extends JsonCacheController {
  @RequestMapping(method = RequestMethod.GET)
  public String handleRequest(Model model, HttpServletRequest request) {
    if (isRobot(model, request)) {
      model.addAttribute("error", Nls.get("ROBOTS_PROMPT"));
      return "index";
    }
    Re4msGroup re4ms = (Re4msGroup) request.getSession().getAttribute("re4ms");
    if (re4ms == null) {
      model.addAttribute("error",
          "Session data lost somehow, did you clear your cache?  Please try again.");
      return Rank.ROBOT.getPage(model);
    }
    model.addAttribute("thelink", getTheLink(request, re4ms));
    return Rank.SKULL.getPage(model);
  }

  private String getTheLink(HttpServletRequest request, Re4msGroup re4ms) {
    StringWriter sw = new StringWriter();
    sw.append(
        request.getRequestURL().toString().replace("/invites", "/member"));
    sw.append("?uuid=");
    sw.append(re4ms.getUuid());
    return sw.toString();
  }
}
