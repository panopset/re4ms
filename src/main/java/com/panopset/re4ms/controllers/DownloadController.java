package com.panopset.re4ms.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.panopset.Logop;
import com.panopset.re4ms.Onetimers;

@Controller
@RequestMapping("/download/{uuid}")
public class DownloadController extends JsonCacheController {

  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public void getFile(@PathVariable("uuid") String uuid,
      HttpServletResponse response) {
    try {
      File fileToDownload = new Onetimers(findRe4ms(uuid)).getOnetimeFile();
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
}
