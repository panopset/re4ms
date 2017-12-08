package com.panopset.re4ms.hibernate;

import java.io.File;
import java.util.UUID;
import com.google.common.base.Joiner;
import com.panopset.compat.util.Alert;
import com.panopset.compat.util.Stringop;
import com.panopset.io.Fileop;
import com.panopset.io.Jsonop;
import com.panopset.re4ms.interfaces.Re4msSystemProperties;

public enum Re4mSet implements Re4msSystemProperties {

  INSTANCE;

  public static Re4msGroup createGroup() {
    return INSTANCE.newRe4msGroup();
  }

  public static void save(Re4msGroup re4ms) {
    Jsonop.save(INSTANCE.getJson4(re4ms.getUuid()), re4ms);
  }

  private static final File DEFAULT_DIR = new File(".");

  private transient File dir;

  private File getDir() {
    if (dir == null) {
      File dft = new File(Joiner.on(Stringop.FILE_SEP).join(Stringop.USER_HOME, "re4mss"));
      String path = System.getProperty(RE4MS_DIR);
      if (Stringop.isPopulated(path)) {
        dir = new File(path);
      } else {
        dir = dft;
      }
      if (!dir.exists() && !dir.mkdirs()) {
        Alert.yellow(dir, "Could not create, switching to default.");
        dir = dft;
      }
      String realPath = Fileop.getCanonicalPath(dir);
      Alert.green("****************************************************");
      Alert.green("Temporary files directory is:");
      Alert.green(realPath);
      Alert.green("****************************************************");
      if (!dir.exists()) {
        Alert.red(Joiner.on(" ").join(realPath, "does not exist."));
      }
      if (!dir.canWrite()) {
        Alert.red(Joiner.on(" ").join(realPath, "is write protected."));
        return DEFAULT_DIR;
      }
    }
    return dir;
  }

  public static Re4msGroup find(final String re4msuuid) {
    File json = INSTANCE.getJson4(re4msuuid);
    if (!json.exists()) {
      return null;
    }
    Re4msGroup re4ms = (Re4msGroup) Jsonop.load(json);
    return re4ms;
  }

  private File getJson4(String re4msuuid) {
    return new File(
        Joiner.on(Stringop.FILE_SEP).join(Fileop.getCanonicalPath(getDir()), Joiner.on('.').join(re4msuuid, "json")));
  }

  private synchronized Re4msGroup newRe4msGroup() {
    Re4msGroup rtn = new Re4msGroup();
    String newUUID = UUID.randomUUID().toString();
    while (getJson4(newUUID).exists()) {
      newUUID = UUID.randomUUID().toString();
    }
    rtn.setUuid(newUUID);
    return rtn;
  }

  public static void delete(Re4msGroup re4ms) {
    File f = INSTANCE.getJson4(re4ms.getUuid());
    if (f.exists()) {
      f.delete();
    }
  }
}
