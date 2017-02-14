package com.panopset.re4ms.hibernate;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Set;
import java.util.TreeSet;

public class Re4msGroup implements Serializable {
  
  private Set<Member> members;

  public transient Long cacheDate;
 
  public Member findMemberByNickname(final String nickname) {
    for (Member mbr : getMembers()) {
      if (mbr.getDisplayName().equals(nickname)) {
        return mbr;
      }
    }
    return null;
  }

  private String uuid;

  public synchronized String getUuid() {
    if (uuid == null) {
      uuid = "";
    }
    return uuid;
  }

  public synchronized void setUuid(String re4msuuid) {
    this.uuid = re4msuuid;
  }

  public synchronized void setDisplayNames(String values) {
    int i = 0;
    for (String mbr : values.split(",")) {
      Member member = new Member();
      member.setRe4msId(this.getUuid());
      member.setMemberId("" + i++);
      member.setDisplayName(mbr);
      getMembers().add(member);
    }
    Re4mSet.save(this);
  }

  public synchronized Long getCacheDate() {
    return cacheDate;
  }

  public synchronized void setCacheDate(Long re4mscacheDate) {
    this.cacheDate = re4mscacheDate;
  }

  public synchronized Set<Member> getMembers() {
    if (members == null) {
      members = new TreeSet<Member>();
    }
    return members;
  }

  public synchronized void setMembers(Set<Member> re4msmembers) {
    this.members = re4msmembers;
  }

  public boolean hasMembers() {
    return getMembers() != null && getMembers().size() > 0;
  }

  private static final long serialVersionUID = 1L;

  public String getNicknames() {
    StringWriter sw = new StringWriter();
    boolean firstTime = true;
    for (Member mbr : getMembers()) {
      if (firstTime) {
        firstTime = false;
      } else {
        sw.append(",");
      }
      sw.append(mbr.getDisplayName());
    }
    return sw.toString();
  }

  private String passphrase;
  public String getPassphrase() {
    return passphrase;
  }
  
  public void setPassphrase(final String value) {
    passphrase = value;
  }

  public boolean areDownloadsComplete() {
    for (Member mbr : getMembers()) {
      if (!mbr.hasDownloaded()) {
        return false;
      }
    }
    Re4mSet.delete(this);
    return true;
  }
}
