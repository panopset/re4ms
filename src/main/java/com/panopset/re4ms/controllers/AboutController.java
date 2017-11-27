package com.panopset.re4ms.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.panopset.re4ms.interfaces.Re4msSystemProperties;

@Controller
@RequestMapping("/about")
public class AboutController extends JsonCacheController
implements Re4msSystemProperties {

  @RequestMapping(method = RequestMethod.GET)
  public String handleRequest(Model model, HttpServletRequest request) {
    return "about";
  }
}
