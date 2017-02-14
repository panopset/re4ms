package com.panopset.re4ms.interfaces;

/**
 * These system properties must be defined, for a Re4ms deployment. Sample
 * tomcat/bin/setenv.sh is included at the top level of this project.
 * 
 * @author Karl Dinwiddie
 *
 */
public interface Re4msSystemProperties {
  String RE4MS_RECSECRET = "re4ms_recaptcha_secret";
  String RE4MS_RECPUBLIC = "re4ms_recaptcha_public";
  String RE4MS_DIR = "re4ms_dir";
}
