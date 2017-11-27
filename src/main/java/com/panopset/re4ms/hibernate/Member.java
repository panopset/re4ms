package com.panopset.re4ms.hibernate;

import java.io.Serializable;

import org.apache.commons.validator.util.Flags;

public class Member implements Comparable<Member>, Serializable {

  private static final int FLAG_DOWNLOADED = 1;
  
  private String re4msId;

  private String displayName;
  
  private String id;

  private Long status;

  public synchronized Long getStatus() {
    if (status == null) {
      status = 0L;
    }
    return status;
  }

  public synchronized void setStatus(Long status) {
    this.status = status;
  }

  public synchronized String getRe4msId() {
    return re4msId;
  }

  public synchronized void setRe4msId(String mbrRe4msId) {
    this.re4msId = mbrRe4msId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String value) {
    this.displayName = value;
  }

  public String getMemberId() {
    return id;
  }
  
  public void setMemberId(final String memberId) {
    id = memberId;
  }

  public int compareTo(Member o) {
    if (o == null) {
      return -1;
    }
    if (o.getMemberId() == null || getMemberId() == null) {
      return -1;
    }
    return o.getMemberId().compareTo(getMemberId());
  }

  private static final long serialVersionUID = 1L;

  public Boolean hasDownloaded() {
      return new Flags(getStatus()).isOn(FLAG_DOWNLOADED);
  }

  public void setDownloaded() {
    setStatusFlag(FLAG_DOWNLOADED);
  }

  private void setStatusFlag(int flag) {
    Flags flags = new Flags(getStatus());
    flags.turnOn(flag);
    setStatus(flags.getFlags());
  }
}
