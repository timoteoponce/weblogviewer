package org.timo.logviewer.controller;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfiguration {

	@Value("${log.path:}")
	private String logPath;

	@Value("${log.files:}")
	private String[] logFiles;

	@Value("${log.folder:log}")
	private String fileExtension;

	@Value("${log.folder:100}")
	private int bufferedLines;

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

	public int getBufferedLines() {
		return bufferedLines;
	}

	public void setBufferedLines(int bufferedLines) {
		this.bufferedLines = bufferedLines;
	}

	public Collection<String> getLogFilesList() {
		return logFiles == null ? null : Arrays.asList(logFiles);
	}

}
