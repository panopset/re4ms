package com.panopset.re4ms.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.panopset.re4ms.interfaces.NlsHelper;

@Controller
@RequestMapping("/re4msill")
public class IlluminatiController extends JsonCacheController {

  @RequestMapping(method = RequestMethod.GET)
  public String handleRequest(Model model, HttpServletRequest request) {
    if (isRobot(model, request)) {
      model.addAttribute("error", NlsHelper.get("ROBOTS_PROMPT"));
      return "index";
    }
    return "forward:/re4msill";
  }
}
