package com.panopset.re4ms.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.common.base.Joiner;
import com.panopset.compat.util.Alert;
import com.panopset.compat.util.Stringop;
import com.panopset.re4ms.MemberDisplay;
import com.panopset.re4ms.Onetimers;
import com.panopset.re4ms.hibernate.Member;
import com.panopset.re4ms.hibernate.Re4mSet;
import com.panopset.re4ms.hibernate.Re4msGroup;
import com.panopset.re4ms.interfaces.NlsHelper;
import com.panopset.re4ms.interfaces.Rank;

@Controller
@RequestMapping("/member")
public class FinalPhaseController extends JsonCacheController {

  @RequestMapping(method = RequestMethod.GET)
  public String handleRequest(Model model, HttpServletRequest request) {
    Re4msGroup re4ms = (Re4msGroup) request.getSession().getAttribute(RE4MS);
    if (re4ms == null) {
      model.addAttribute("error", NlsHelper.get("UUID_NOT_FOUND_PROMPT"));
      return "index";
    }
    request.getSession().setAttribute(RE4MS, re4ms);
    addMemberNlsTexts(request.getRequestURL().toString(), model, re4ms);
    return Rank.BOHEMIAN.getPage(model);
  }
  
  @RequestMapping(params = {"uuid"}, method = RequestMethod.GET)
  public String handleRequestFromNickname(Model model, HttpServletRequest request,
      @RequestParam("uuid") String uuid) {
    if (!Stringop.isPopulated(uuid)) {
      return "redirect:/?error=" + NlsHelper.get("UUID_NOT_THERE_PROMPT");
    }
    Re4msGroup re4ms = findRe4ms(uuid);
    if (re4ms == null) {
      return "redirect:/?error=" + NlsHelper.get("UUID_NOT_FOUND_PROMPT");
    }
    request.getSession().setAttribute(RE4MS, re4ms);
    addMemberNlsTexts(request.getRequestURL().toString(), model, re4ms);
    if (re4ms.areDownloadsComplete()) {
      return Rank.ILLUMINATI.getPage(model);
    }
    return Rank.BOHEMIAN.getPage(model);
  }

  @RequestMapping(params = {"id", "uuid"}, method = RequestMethod.GET)
  public String handleRequestForDownload(Model model, HttpServletRequest request,
      @RequestParam("id") String memberId, @RequestParam("uuid") String uuid) {
    Re4msGroup re4ms = (Re4msGroup) request.getSession().getAttribute(RE4MS);
    if (re4ms == null) {
      model.addAttribute("error", "Missing session data, cookies not enabled?");
      return Rank.BOHEMIAN.getPage(model);
    }
    if (re4ms.areDownloadsComplete()) {
      try {
        new Onetimers(findRe4ms(uuid)).getOnetimeFile().delete();
      } catch (Exception ex) {
        Alert.red(ex);
      }
      return Rank.ILLUMINATI.getPage(model);
    }
    addMemberNlsTexts(request.getRequestURL().toString(), model, re4ms);
    boolean found = false;
    for (Member mbr : re4ms.getMembers()) {
      if (memberId.equalsIgnoreCase(mbr.getMemberId())) {
        found = true;
        if (mbr.hasDownloaded()) {
          model.addAttribute("error", NlsHelper.get(Rank.BOHEMIAN.getHelpKey()));
          return Rank.BOHEMIAN.getPage(model);
        } else {
          mbr.setDownloaded();
        }
      }
    }
    if (found) {
      Re4mSet.save(re4ms);
    } else {
      model.addAttribute("error", "");
      return "index";
    }
    if (!Stringop.isPopulated(memberId)) {
      model.addAttribute("error", NlsHelper.get(Rank.BOHEMIAN.getHelpKey()));
      return Rank.BOHEMIAN.getPage(model);
    }
    model.addAttribute(MODEL_KEY_MEMBER_ID, memberId);
    return Joiner.on("/").join("forward:/download", uuid);
  }
  

  void addMemberNlsTexts(String baseURL, Model model, Re4msGroup re4ms) {
    List<MemberDisplay> memberDisplayList = new ArrayList<MemberDisplay>();
    for (Member mbr : re4ms.getMembers()) {
      memberDisplayList.add(new MemberDisplay(baseURL, mbr));
    }
    model.addAttribute(MODEL_KEY_MBRDSPLST, memberDisplayList);
  }
}
