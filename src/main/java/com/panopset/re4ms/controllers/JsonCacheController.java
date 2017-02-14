package com.panopset.re4ms.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.panopset.Logop;
import com.panopset.Stringop;
import com.panopset.re4ms.hibernate.Re4mSet;
import com.panopset.re4ms.hibernate.Re4msGroup;
import com.panopset.re4ms.interfaces.ControlKeys;
import com.panopset.re4ms.util.Re4msCache;
import com.panopset.re4ms.util.VerifyRecaptcha;

public abstract class JsonCacheController implements ControlKeys {
  Boolean i_robot = true;
  String error = "";
  
  Re4msGroup findRe4ms(final String uuid) {
    Re4msGroup re4ms = Re4msCache.getRooms().get(uuid);
    if (re4ms == null) {
      try {
        re4ms = Re4mSet.find(uuid);
        if (re4ms == null) {
          return null;
        }
      } catch (Exception ex) {
        Logop.error(ex);
        return null;
      }
    }
    return re4ms;
  }

  protected boolean isRobot(Model model, HttpServletRequest request) {
    verifyHumanityOrRobotity(model, request);
    return i_robot;
  }
  
  private boolean isHumanAfterAll(Model model, HttpServletRequest request) {
    String recaptchaPublic = System.getProperty(RE4MS_RECPUBLIC);
    String gRecaptchaResponse = "";
    if (Stringop.isPopulated(recaptchaPublic)) {
      model.addAttribute(RE4MS_RECPUBLIC, recaptchaPublic);
      gRecaptchaResponse = request.getParameter("g-recaptcha-response");
      Boolean verify = false;
      try {
        verify = VerifyRecaptcha.verify(gRecaptchaResponse);
      } catch (IOException e) {
        return false;
      }
      if (!verify) {
        return false;
      }
    }
    model.addAttribute(RE4MS_RECPUBLIC, "");
    return true;
  }

  private void verifyHumanityOrRobotity(Model model, HttpServletRequest request) {
    i_robot = (Boolean) request.getSession().getAttribute(I_ROBOT);
    if (i_robot == null || i_robot) {
      i_robot = !isHumanAfterAll(model, request);
      request.getSession().setAttribute(I_ROBOT, i_robot);
    }
  }
}
