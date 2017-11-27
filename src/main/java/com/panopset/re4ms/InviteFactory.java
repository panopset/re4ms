package com.panopset.re4ms;

import java.io.StringWriter;

import com.panopset.re4ms.hibernate.Re4msGroup;
import com.panopset.re4ms.interfaces.Nls;

public class InviteFactory {
  
  private final Re4msGroup re4ms;
  
  public InviteFactory(Re4msGroup re4msGroup) {
    re4ms = re4msGroup;
  }

  public String getSendInvite(String memberUrl) {
    StringWriter sw = new StringWriter();
    sw.append("mailto:");
    sw.append(re4ms.getNicknames());
    sw.append("?subject=");
    sw.append(Nls.get("INVITE_SUBJ"));
    sw.append("&body=");
    sw.append(getBodyOfInvite(memberUrl));
    return sw.toString();
  }

  private String getBodyOfInvite(String memberUrl) {
    StringWriter sw = new StringWriter();
    sw.append(memberUrl);
    sw.append("?uuid=");
    sw.append(re4ms.getUuid());
    return sw.toString();
  }
}
