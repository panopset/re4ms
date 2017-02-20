package com.panopset.re4ms.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.panopset.Stringop;
import com.panopset.re4ms.interfaces.Nls;
import com.panopset.re4ms.interfaces.Rank;
import com.panopset.re4ms.interfaces.Re4msSystemProperties;
import com.panopset.security.TextScrambler;

@Controller
@SessionAttributes("isHuman")
@RequestMapping(value={"/","index.html"})
public class IndexController extends JsonCacheController
    implements Re4msSystemProperties {

  @RequestMapping(method = RequestMethod.GET) 
  public String handleRequest(Model model, HttpServletRequest request,
      @RequestParam(value = "error", required = false) String error) {
    model.addAttribute("koi", "" + TextScrambler.DEFAULT_KEY_OBTENTION_ITERATIONS);
    model.addAttribute("error", error);
    if (isRobot(model, request)) {
      return Rank.ROBOT.getPage(model);
    }
    return Rank.ROBOT.getPage(model);
  }

  @RequestMapping(params = { "msg", "pvk", "koi", "sharekeys"}, 
  method = RequestMethod.POST)
  public String handleShareKeysRequest(Model model, HttpServletRequest request,
      @RequestParam("msg") String msg, 
      @RequestParam("pvk") String pvk, 
      @RequestParam("koi") String koi, 
      @RequestParam("sharekeys") String sharekeys
    ) {
    model.addAttribute("pvk", pvk);
    model.addAttribute("msg", msg);
    model.addAttribute("koi", check(koi, "" + TextScrambler.DEFAULT_KEY_OBTENTION_ITERATIONS));
    if (isRobot(model, request)) {
      model.addAttribute("error", Nls.get("ROBOTS_PROMPT"));
      return Rank.ROBOT.getPage(model);
    }
    return Rank.ORGANIZER.getForward(model);
  }

  private String check(String val, String dft) {
    return Stringop.isPopulated(val) ? val : dft;
  }

  @RequestMapping(params = { "msg", "pvk", "koi", "btnencrypt"}, 
  method = RequestMethod.POST)
  public String handleEncryptionRequest(Model model, HttpServletRequest request,
      @RequestParam("msg") String msg, 
      @RequestParam("pvk") String pvk, 
      @RequestParam("koi") String koi,
      @RequestParam("btnencrypt") String btnencrypt
    ) {
    model.addAttribute("msg", msg);
    model.addAttribute("pvk", pvk);
    model.addAttribute("koi", check(koi, "" + TextScrambler.DEFAULT_KEY_OBTENTION_ITERATIONS));
    if (isValid(request, model, msg, pvk, koi)) {
      try {
        model.addAttribute("msg", new TextScrambler().withKeyObtentionIters(getKoiInt(model)).encrypt(pvk, msg));
      } catch (Exception ex) {
        model.addAttribute("msg", msg);
        model.addAttribute("error", ex.getMessage());
      }
    }
    return Rank.ROBOT.getPage(model);
  }

  @RequestMapping(params = { "msg", "pvk", "koi", "btndecrypt"}, method = RequestMethod.POST)
  public String handleDecryptionRequest(Model model, HttpServletRequest request,
      @RequestParam("msg") String msg, 
      @RequestParam("pvk") String pvk, 
      @RequestParam("koi") String koi,
      @RequestParam("btndecrypt") String btnencrypt
    ) {
    model.addAttribute("msg", msg);
    model.addAttribute("pvk", pvk);
    model.addAttribute("koi", check(koi, "" + TextScrambler.DEFAULT_KEY_OBTENTION_ITERATIONS));
    if (isValid(request, model, msg, pvk, koi)) {
      try {
        model.addAttribute("msg", new TextScrambler().withKeyObtentionIters(getKoiInt(model)).decrypt(pvk, msg));
      } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException eonpe) {
        model.addAttribute("error", "Operation not possible, is the password correct?  Did the number of iterations change?");
      } catch (Exception ex) {
        model.addAttribute("msg", msg);
        model.addAttribute("error", ex.getMessage());
      }
    }
    return Rank.ROBOT.getPage(model);
  }

  private boolean isValid(HttpServletRequest request, Model model, String msg, String pvk, String koi) {
    if (isRobot(model, request)) {
      model.addAttribute("error", Nls.get("ROBOTS_PROMPT"));
      model.addAttribute("msg", msg);
      return false;
    }
    if (pvk == null || pvk.length() < 4) {
      model.addAttribute("error", Nls.get("PASSPHRASE_4CHAR"));
      model.addAttribute("msg", msg);
      return false;
    }
    if (!Stringop.isPopulated(msg)) {
      model.addAttribute("error", Nls.get("ENCTXT_PROMPT"));
      model.addAttribute("msg", msg);
      return false;
    }
    if (!Stringop.isPopulated(pvk)) {
      model.addAttribute("error", Nls.get("KEYS_PROMPT"));
      model.addAttribute("msg", msg);
      return false;
    }
    if (!Stringop.isPopulated(koi)) {
      model.addAttribute("error", Nls.get("KOI_PROMPT"));
      return false;
    }
    try {
      int koiInt = Integer.parseInt(koi);
      if (koiInt > 1000000) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException nfe) {
      model.addAttribute("error",Nls.get("KOI_PROMPT"));
      return false;
    }
    return true;
  }
  
  int getKoiInt(Model model) {
    return Integer.parseInt(model.asMap().get("koi").toString());
  }
}
