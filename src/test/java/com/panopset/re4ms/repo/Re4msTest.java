package com.panopset.re4ms.repo;

import org.junit.Assert;
import org.junit.Test;

import com.panopset.re4ms.hibernate.Member;
import com.panopset.re4ms.hibernate.Re4msGroup;
import com.panopset.re4ms.interfaces.Nls;
import com.panopset.re4ms.hibernate.Re4mSet;

public class Re4msTest {
  
  @Test
  public void testMessage() {
    Assert.assertEquals("Everyone has their one-time-download passphrase.  All traces of your secret society, bent on world domination, are deleted from the server.", 
        Nls.get("UUID_NOT_FOUND_PROMPT"));
  }
  @Test
  public void simpleTest() {
    Re4msGroup re4ms = Re4mSet.createGroup();
    String id = re4ms.getUuid();
    Re4mSet.save(re4ms);
    re4ms = Re4mSet.find(id);
    Assert.assertEquals(id, re4ms.getUuid());
    Re4mSet.delete(re4ms);
    re4ms = Re4mSet.find(id);
    Assert.assertNull(re4ms);
  }
  
  @Test
  public void nicknamesTest() {
    Re4msGroup re4ms = Re4mSet.createGroup();
    String id = re4ms.getUuid();
    re4ms.setDisplayNames(TEST_NICKNAME);
    Re4mSet.save(re4ms);
    re4ms = Re4mSet.find(id);
    Assert.assertEquals(id, re4ms.getUuid());
    Member mbr = re4ms.findMemberByNickname(TEST_NICKNAME);
    Assert.assertEquals(TEST_NICKNAME, mbr.getDisplayName());
    Re4mSet.delete(re4ms);
  }

  @Test
  public void test() throws Exception {
    Re4msGroup re4ms = Re4mSet.createGroup();
    String id = re4ms.getUuid();
    re4ms.setDisplayNames(TEST_NICKNAME);
    Member mbr = re4ms.findMemberByNickname(TEST_NICKNAME);
    mbr.setStatus(1L);
    Assert.assertTrue(re4ms.getMembers().size() == 1);
    Re4mSet.save(re4ms);
    Re4msGroup r7 = Re4mSet.find(id);
    Assert.assertEquals(id, re4ms.getUuid());
    for (Member member : r7.getMembers()) {
      Assert.assertTrue(1L == member.getStatus());
      Assert.assertEquals(TEST_NICKNAME, member.getDisplayName());
    }
    re4ms = Re4mSet.find(re4ms.getUuid());
    Assert.assertTrue(re4ms.hasMembers());
    Re4mSet.delete(re4ms);
  }

  @Test
  public void testEmptyMembers() throws Exception {
    Re4msGroup re4ms = Re4mSet.createGroup();
    Re4mSet.save(re4ms);
    re4ms = Re4mSet.find(re4ms.getUuid());
    Assert.assertFalse(re4ms.hasMembers());
    Re4mSet.delete(re4ms);
  }

  private static final String TEST_NICKNAME = "testNickname";
}
