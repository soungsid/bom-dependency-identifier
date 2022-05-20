package com.sms.bomdependencyidentifier;

import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.xml.bind.DatatypeConverter;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.spotify.github.v3.clients.GitHubClient;
import com.spotify.github.v3.clients.RepositoryClient;

@Slf4j
@Service
public class GithubService {

  final GitHubClient githubClient = GitHubClient.create(URI.create("https://api.github.com/"), "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");
  @Value("#{'${app.list-repos}'.split(',')}")
  List<String>  listRepo;


  public List<Dependency> doJob() throws ExecutionException, InterruptedException, XmlPullParserException, IOException {

    String owner = "soungsid";
    Model model = getPom(owner, listRepo.get(0));
    List<Dependency> listDependanceCommune = model.getDependencies();
    for (int i = 1; i < listRepo.size(); i++) {

      Model m = getPom(owner, listRepo.get(i));
      listDependanceCommune.retainAll(m.getDependencies());
    }


    return listDependanceCommune;

  }

  public  Model getPom(String owner, String repo)
      throws ExecutionException, InterruptedException, XmlPullParserException, IOException {
    final RepositoryClient repositoryClient = githubClient.createRepositoryClient(owner, repo);
    String encodedContent = repositoryClient.getFileContent("pom.xml").get().content();

    byte[] bytes = DatatypeConverter.parseBase64Binary(encodedContent);
    log.info("{}", new String(bytes));

    MavenXpp3Reader reader = new MavenXpp3Reader();
    InputStream stream = new ByteArrayInputStream(bytes);
    Model model = reader.read(stream);
    return model;
  }

}
