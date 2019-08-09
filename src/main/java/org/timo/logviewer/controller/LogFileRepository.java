package org.timo.logviewer.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LogFileRepository {

  private final Map<String, LogFile> filesMap = new ConcurrentHashMap<>();

  private final AppConfiguration config;

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  public LogFileRepository(AppConfiguration config) {
    this.config = config;
  }

  public List<LogFile> findAll() {
    refreshCache();
    return refreshCache().values().stream().sorted().collect(Collectors.toList());
  }

  public Optional<LogFile> getById(String fileId) {
    LogFile logFile = refreshCache().get(fileId);
    if (logFile != null) {
      return Optional.of(logFile);
    }
    return Optional.empty();
  }

  private Map<String, LogFile> refreshCache() {
    log.info("Using properties: " + config.toString());
    if (filesMap.isEmpty()) {
      loadAll().forEach(l -> filesMap.put(l.getId(), l));
    }
    return filesMap;
  }

  private List<LogFile> loadAll() {
    final List<LogFile> result = new ArrayList<>(0);
    final String tomcatHome = System.getProperty("catalina.home");
    if (tomcatHome != null) {
      result.addAll(LogFile.of(loadFromFolder(tomcatHome + "/logs/")));
    }
    Collection<String> logFilesList = config.getLogFilesList();
    if (!logFilesList.isEmpty()) {
      result.addAll(LogFile.of(logFilesList));
    }
    if (config.getLogPath() != null) {
      result.addAll(LogFile.of(loadFromFolder(config.getLogPath())));
    }
    return result;
  }

  private File[] loadFromFolder(String path) {
    final List<String> extensions = Arrays.asList(config.getFileExtension());
    return new File(path)
        .listFiles((dir, name) -> extensions.contains(Utils.getExtension(name).orElse("")));
  }
}
