package org.goldenroute.portfolio.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.io.EofException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController
{
    private static final Logger logger = Logger.getLogger(HomeController.class);

    @RequestMapping("/")
    public String home()
    {
        return "home";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download() throws IOException
    {
        try
        {
            ClassPathResource packageFile = new ClassPathResource("download/app-release.apk");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(packageFile.contentLength());
            headers.setContentDispositionFormData("attachment", packageFile.getFilename());
            headers.setCacheControl(CacheControl.noCache().mustRevalidate().toString());
            InputStreamResource resource = new InputStreamResource(packageFile.getInputStream());
            return ResponseEntity.ok().headers(headers).body(resource);
        }
        catch (EofException ex)
        {
            logger.error(ex);
        }

        return null;
    }
}
