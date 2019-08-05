package org.timo.logviewer.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	private final AppConfiguration config;

	@Autowired
	public MainController(AppConfiguration config) {
		this.config = config;
	}

	@GetMapping("/")
	public String sayHello(Model model) {
		model.addAttribute("configLogFiles", config.getLogFilesList());
		model.addAttribute("configLogPath", config.getLogPath());
		model.addAttribute("files", filesList());
		return "main";
	}

	private Collection<LogFile> filesList() {
		if (config.getLogPath() == null) {
			return LogFile.of(config.getLogFilesList());
		} else {
			return LogFile.of(new File(config.getLogPath())
					.listFiles((FilenameFilter) (dir, name) -> name.endsWith(config.getFileExtension())));
		}
	}

}
