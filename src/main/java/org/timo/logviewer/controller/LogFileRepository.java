package org.timo.logviewer.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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

  private final Map<String, String> filesMap = new ConcurrentHashMap<>();

  private final AppConfiguration config;

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  public LogFileRepository(AppConfiguration config) {
    this.config = config;
  }

  public List<LogFile> findAll() {
    log.info("Using properties: " + config.toString());
    List<LogFile> result;
    if (config.getLogPath() == null) {
      result = LogFile.of(config.getLogFilesList());
    } else {
      result =
          LogFile.of(
              new File(config.getLogPath())
                  .listFiles(
                      (FilenameFilter) (dir, name) -> name.endsWith(config.getFileExtension())));
    }
    addToCache(result);
    return result;
  }

  private void addToCache(List<LogFile> result) {
    result.forEach(l -> filesMap.put(l.getId(), l.getPath()));
  }

  public Optional<LogFile> getById(String fileId) {
    if (filesMap.containsKey(fileId)) {
      return Optional.of(LogFile.of(new File(filesMap.get(fileId))));
    }
    return Optional.empty();
  }
}
