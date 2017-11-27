package com.panopset.re4ms.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;

import com.google.common.base.Joiner;
import com.panopset.re4ms.RankDisplay;

public enum Rank implements ControlKeys {

   ROBOT("index"), 
   ORGANIZER("nicknames"),
   SKULL("invites"), 
   BOHEMIAN("members"), 
   ILLUMINATI("re4msill");

  Rank(String rankHomePage) {
    page = rankHomePage;
    forward = Joiner.on("/").join("forward:", page);
  }

  public String getNameKey() {
    return this.name();
  }

  public String getHelpKey() {
    return helpKey;
  }

  public String getBtnKey() {
    return btnKey;
  }

  private final String helpKey = Joiner.on('_').join(name(), "HELP");
  private final String btnKey = Joiner.on('_').join(name(), "BTN");
  private final String page;
  private final String forward;

  public String getPage(Model model) {
    setupNextPage(model);
    return page;
  }

  public String getForward(Model model) {
    setupNextPage(model);
    return forward;
  }

  private void setupNextPage(Model model) {
    if (ROBOT.equals(this)) {
      return;
    }
    model.addAttribute(MODEL_KEY_STATUS, Nls.get(name()));
    String btnText = Nls.get(getBtnKey());
    String helpText = Nls.get(getHelpKey());
    model.addAttribute(MODEL_KEY_HELP, helpText);
    model.addAttribute(MODEL_KEY_BTN_TEXT, btnText);
    List<RankDisplay> rankDisplayList = new ArrayList<RankDisplay>();
    for (Rank rank : Rank.values()) {
      if (!rank.equals(Rank.ROBOT)) {
        rankDisplayList.add(new RankDisplay(rank, rank.equals(this)));
      }
    }
    model.addAttribute(MODEL_KEY_RNKDSPLST, rankDisplayList);
  }
  
  public static Rank findRank(String rank) {
    for (Rank r : Rank.values()) {
      if (r.name().equals(rank)) {
        return r;
      }
    }
    return Rank.ROBOT;
  }
}
