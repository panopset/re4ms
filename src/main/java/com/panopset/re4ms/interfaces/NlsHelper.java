package com.panopset.re4ms.interfaces;

import com.panopset.io.Bundleop;

public enum NlsHelper {

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
