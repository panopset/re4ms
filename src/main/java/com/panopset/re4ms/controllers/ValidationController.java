package com.panopset.re4ms.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.panopset.Fileop;
import com.panopset.Logop;

@Controller
@RequestMapping("/vd9tgzle.htm")
public class ValidationController {
  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public void getFile(HttpServletResponse response) {
    try {
      File fileToDownload = getFile();
      InputStream inputStream = new FileInputStream(fileToDownload);
      response.setContentType("application/force-download");
      response.setHeader("Content-Disposition",
          "attachment; filename=" + fileToDownload.getName());
      IOUtils.copy(inputStream, response.getOutputStream());
      response.flushBuffer();
      inputStream.close();
    } catch (Exception ex) {
      Logop.error(ex);
    }
  }
  
  private File file;
  
  private File getFile() {
    if (file == null) {
      file = new File("vd9tgzle.htm");
      if (!file.exists()) {
        Fileop.saveStringToFile("aoEgyHiH3hAJTeCNK0aE", file);
      }
    }
    return file;
  }
}
