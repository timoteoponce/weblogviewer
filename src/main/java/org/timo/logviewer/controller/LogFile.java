package org.timo.logviewer.controller;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogFile implements Comparable<LogFile> {

  private static final Collection<String> KNOWN_TEXT_EXTENSIONS =
      Collections.unmodifiableCollection(Arrays
          .asList(".txt", ".log", ".text", ".xml", ".json", ".out", ".html", ".stdout", ".stderr"));

  private final String id;
  private final String name;

  private final String path;
  private final long size;

  public LogFile(String id, String name, String path, long size) {
    this.id = id;
    this.name = name;
    this.path = path;
    this.size = size;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public long getSize() {
    return size;
  }

  public static LogFile of(File file) {
    return new LogFile(
        checksum(file.getName()), file.getName(), file.getAbsolutePath(), file.length());
  }

  private static String checksum(String strValue) {
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(StandardCharsets.UTF_8.encode(strValue));
      return String.format("%032x", new BigInteger(1, md5.digest()));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public static LogFile of(String filePath) {
    File f = new File(filePath);
    return f.exists() ? of(f) : null;
  }

  public static List<LogFile> of(File[] files) {
    if (files == null) {
      return Collections.emptyList();
    }
    return toList(Stream.of(files).filter(Objects::nonNull).map(LogFile::of));
  }

  public static List<LogFile> of(Collection<String> logFilesList) {
    return toList(logFilesList.stream().map(LogFile::of).filter(Objects::nonNull));
  }

  private static List<LogFile> toList(Stream<LogFile> stream) {
    return stream.collect(Collectors.toList());
  }

  public LogFile withId(String otherFileId) {
    return new LogFile(otherFileId, name, path, size);
  }

  /**
   * Larger than 5MB
   *
   * @return too big
   */
  public boolean isTooBig() {
    return size > 5 * 1024 * 1024;
  }

  @Override
  public int compareTo(LogFile o) {
    return this.getPath().compareTo(o.getPath());
  }

  public boolean isTextFile() {
    return Utils.getExtension(path).map(KNOWN_TEXT_EXTENSIONS::contains).orElse(true);
  }
}
