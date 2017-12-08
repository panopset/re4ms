package com.panopset.re4ms;

import com.panopset.re4ms.interfaces.NlsHelper;
import com.panopset.re4ms.interfaces.Rank;

public class RankDisplay {
  public final Rank rank;
  public final boolean ic;
  public RankDisplay(final Rank currentRank, final boolean isCurrent) {
    rank = currentRank;
    ic = isCurrent;
  }
  public Boolean isCurrent() {
    return ic;
  }
  public String getName() {
    return NlsHelper.get(rank.getNameKey());
  }
  public String getHelp() {
    return NlsHelper.get(rank.getHelpKey());
  }
  public String getNameTDclass() {
    if (ic) {
      return "cls_rank_current";
    }
    return "cls_rank";
  }
  public String getHelpTDclass() {
    if (ic) {
      return "cls_rank_help_current";
    }
    return "cls_rank_help";
  }
}
