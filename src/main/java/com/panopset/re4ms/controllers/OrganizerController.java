package com.panopset.re4ms.controllers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.panopset.compat.util.Stringop;
import com.panopset.re4ms.hibernate.Re4mSet;
import com.panopset.re4ms.hibernate.Re4msGroup;
import com.panopset.re4ms.interfaces.Nls;
import com.panopset.re4ms.interfaces.Rank;

@Controller
@RequestMapping("/nicknames")
public class OrganizerController extends JsonCacheController {

  @RequestMapping(method = RequestMethod.POST)
  public String handleRequest(Model model, HttpServletRequest request) {
    if (isRobot(model, request)) {
      model.addAttribute("error", Nls.get("ROBOTS_PROMPT"));
      return "index";
    }
    Re4msGroup brandNewRe4ms = Re4mSet.createGroup();
    Re4mSet.save(brandNewRe4ms);
    request.getSession().setAttribute(RE4MS, brandNewRe4ms);
    model.addAttribute(RE4MS, brandNewRe4ms);
    return Rank.ORGANIZER.getPage(model);
  }

  @RequestMapping(params = {"nicknames", "pvk"}, method = RequestMethod.GET)
  public String handleNicknamesSubmitRequest(Model model, HttpServletRequest request,
      @RequestParam("nicknames") String nicknames, @RequestParam("pvk") String pvk) {
    model.addAttribute("pvk", pvk);
    if (isRobot(model, request)) {
      model.addAttribute("error", Nls.get("ROBOTS_PROMPT"));
      return "index";
    }
    if (pvk == null || pvk.length() < 4) {
      model.addAttribute("error", Nls.get("PASSPHRASE_4CHAR"));
      return Rank.ORGANIZER.getPage(model);
    }
    Re4msGroup re4ms = (Re4msGroup) request.getSession().getAttribute(RE4MS);
    if (re4ms == null) {
      return Rank.ROBOT.getForward(model);
    }
    model.addAttribute(RE4MS, re4ms);
    re4ms.setPassphrase(pvk);
    if (Stringop.isPopulated(nicknames)) {
      re4ms.setDisplayNames(nicknames);
      return Rank.SKULL.getForward(model);
    } else {
      model.addAttribute("error", Nls.get(Rank.ORGANIZER.getHelpKey()));
      return Rank.ORGANIZER.getPage(model);
    }
  }
}
