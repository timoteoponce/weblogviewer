package org.timo.logviewer.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AppConfiguration {

  @Value("${logviewer.path:#{systemProperties['logviewer.path']}}")
  private String logPath;

  @Value("${logviewer.files:#{systemProperties['logviewer.files']}}")
  private String[] logFiles;

  @Value("${logviewer.extension:#{systemProperties['logviewer.extension'] ?: '.log'}}")
  private String fileExtension;

  public String getLogPath() {
    return logPath;
  }

  public void setLogPath(String logPath) {
    this.logPath = logPath;
  }

  public String[] getLogFiles() {
    return logFiles;
  }

  public void setLogFiles(String[] logFiles) {
    this.logFiles = logFiles;
  }

  public String getFileExtension() {
    return fileExtension;
  }

  public void setFileExtension(String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public Collection<String> getLogFilesList() {
    return logFiles == null ? Collections.emptyList() : Arrays.asList(logFiles);
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("AppConfiguration [logPath=")
        .append(logPath)
        .append(", logFiles=")
        .append(Arrays.toString(logFiles))
        .append(", fileExtension=")
        .append(fileExtension)
        .append("]")
        .toString();
  }
}
