package org.timo.logviewer.controller;

import java.util.Optional;

public class Utils {

  public static Optional<String> getExtension(final String filename) {
    return Optional.ofNullable(filename)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(filename.lastIndexOf(".")));
  }
}
