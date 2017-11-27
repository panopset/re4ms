package com.panopset.re4ms.interfaces;

import com.panopset.Bundleop;

public enum Nls {

  INSTANCE;

  public static String get(final String key) {
    return INSTANCE.getTranslation(key);
  }

  private String getTranslation(final String key) {
    String rtn = api.get(key);
    if (rtn == null) {
      return "";
    }
    return rtn;
  }
  
  private final BundleAPI api = new BundleAPI();

  private class BundleAPI extends Bundleop {
    public BundleAPI() {
      super("com.panopset.bundles.re4ms");
    }
  }
}
