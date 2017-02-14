package com.panopset.re4ms.util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.panopset.re4ms.hibernate.Re4msGroup;

public enum Re4msCache {

  INSTANCE;

  private static final int MAX_CACHE_ENTRIES = 2500;

  public static Map<String, Re4msGroup> getRooms() {
    if (INSTANCE.cache.size() > MAX_CACHE_ENTRIES) {
      long yesterday =  LocalDate.now().toEpochDay() - 1;
      for (Entry<String, Re4msGroup> r7 : INSTANCE.cache.entrySet()) {
        if (r7.getValue().cacheDate < yesterday) {
          INSTANCE.cache.remove(r7.getKey());
        }
      }
      if (INSTANCE.cache.size() > MAX_CACHE_ENTRIES) {
        INSTANCE.cache.clear();
      }
    }
    return INSTANCE.cache;
  }

  public final Map<String, Re4msGroup> cache = new HashMap<String, Re4msGroup>();
}
