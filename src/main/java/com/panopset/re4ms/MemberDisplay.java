package com.panopset.re4ms;

import java.io.StringWriter;
import com.panopset.compat.util.Stringop;
import com.panopset.re4ms.hibernate.Member;

public class MemberDisplay {
  public final Member mbr;
  private final String base;

  public MemberDisplay(final String baseURL, final Member member) {
    mbr = member;
    base = baseURL;
  }

  public String getDisplayName() {
    String rtn = mbr.getDisplayName();
    if (Stringop.isPopulated(rtn)) {
      return rtn;
    }
    return "";
  }

  public String getLink() {
    StringWriter sw = new StringWriter();
    sw.append(base);
    sw.append("?uuid=");
    sw.append(mbr.getRe4msId());
    sw.append("&id=");
    sw.append(mbr.getMemberId());
    return sw.toString();
  }

  public String getNameTDclass() {
    if (mbr.hasDownloaded()) {
      return "cls_mbr_downloaded";
    }
    return "cls_mbr";
  }

  public String getHelpTDclass() {
    if (mbr.hasDownloaded()) {
      return "cls_mbr_link_downloaded";
    }
    return "cls_mbr_link";
  }

  public String getDownloaded() {
    return mbr.hasDownloaded().toString().toLowerCase();
  }
}
