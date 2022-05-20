package com.sms.bomdependencyidentifier.api;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sms.bomdependencyidentifier.GithubService;

@Slf4j
@RestController
public class Controller {

  @Autowired
  GithubService githubService;

  @GetMapping("/test")
  public  List<Dependency> main() throws ExecutionException, InterruptedException, IOException, XmlPullParserException {
    List<Dependency> dependencies = githubService.doJob();
    return dependencies;
  }

}
