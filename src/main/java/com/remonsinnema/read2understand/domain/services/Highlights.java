package com.remonsinnema.read2understand.domain.services;

import java.util.List;

public record Highlights(String title, String author, List<String> text) {

  public String author() {
    var result = author.startsWith("by ") ? author.substring(3) : author;
    var parts = result.split(",\\s*");
    if (parts.length == 2) {
      result = "%s %s".formatted(parts[1], parts[0]);
    }
    return result;
  }

  public int numWords() {
    return text.stream().mapToInt(line -> line.split("\\w+").length).sum();
  }
}
