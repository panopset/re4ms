package com.panopset.re4ms;

import java.io.File;
import java.io.StringWriter;
import com.google.common.base.Joiner;
import com.panopset.compat.util.Stringop;
import com.panopset.io.Fileop;
import com.panopset.re4ms.hibernate.Re4msGroup;

/**
 * Key one time download manager for Re4ms groups.
 * 
 * @author Karl Dinwiddie
 *
 */
public class Onetimers {
  private final Re4msGroup re4ms;

  public Onetimers(Re4msGroup value) throws Exception {
    re4ms = value;
    checkFile();
  }

  private void checkFile() throws Exception {
    if (getOnetimeFile().exists()) {
      return;
    }
    generateKPF(getOnetimeFile(), re4ms.getPassphrase());
  }

  private void generateKPF(final File file, String pvk) throws Exception {
    Fileop.mkdirs(file.getParentFile());
    StringWriter sw = new StringWriter();
    sw.append(pvk);
    Fileop.saveStringToFile(sw.toString(), file);
  }

  private File fl;

  public File getOnetimeFile() {
    if (fl == null) {
      fl = new File(Joiner.on(File.separator).join(new File(Stringop.USER_HOME),
          Joiner.on(".").join(re4ms.getUuid(), "txt")));
    }
    return fl;
  }
}
