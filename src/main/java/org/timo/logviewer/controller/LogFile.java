package org.timo.logviewer.controller;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogFile {

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
		return new LogFile(UUID.randomUUID().toString(), file.getName(), file.getAbsolutePath(), file.length());
	}

	public static LogFile of(String filePath) {
		File f = new File(filePath);
		return f.exists() ? of(f) : null;
	}

	public static List<LogFile> of(File[] files) {
		if (files == null)
			return Collections.emptyList();
		return toList(Stream.of(files).filter(Objects::nonNull).map(LogFile::of));
	}

	public static List<LogFile> of(Collection<String> logFilesList) {
		return toList(logFilesList.stream().map(LogFile::of).filter(Objects::nonNull));
	}

	private static List<LogFile> toList(Stream<LogFile> stream) {
		return stream.sorted((a, b) -> a.getPath().compareTo(b.getPath())).collect(Collectors.toList());
	}

  public LogFile withId(String otherFileId) {    
    return new LogFile(otherFileId, name, path, size);
  }

}
