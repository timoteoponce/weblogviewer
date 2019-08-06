package org.timo.logviewer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

  private final AppConfiguration config;

  private final LogFileRepository repo;

  @Autowired
  public MainController(AppConfiguration config, LogFileRepository repo) {
    this.config = config;
    this.repo = repo;
  }

  @GetMapping("/")
  public String sayHello(Model model) {
    model.addAttribute("configLogFiles", config.getLogFilesList());
    model.addAttribute("configLogPath", config.getLogPath());
    model.addAttribute("files", repo.findAll());
    return "main";
  }

  @GetMapping("/download/{fileId}")
  public ResponseEntity<?> download(@PathVariable(required = true, name = "fileId") String fileId) {
    return repo.getById(fileId)
        .map(
            l -> {
              File file = new File(l.getPath());
              try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamResource resource = new InputStreamResource(fis);
                return ResponseEntity.ok()
                    .header(
                        HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                    .contentLength(file.length())
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(resource);
              } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
              }
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/watch/{fileId}")
  public String watch(@PathVariable(required = true, name = "fileId") String fileId, Model model) {
    repo.getById(fileId).ifPresent(f -> model.addAttribute("file", f));
    System.out.println(">> " + model);
    return "watcher";
  }
}
