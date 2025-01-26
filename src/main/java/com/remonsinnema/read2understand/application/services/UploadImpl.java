package com.remonsinnema.read2understand.application.services;

import com.remonsinnema.read2understand.domain.services.HighlightsExtractor;
import com.remonsinnema.read2understand.domain.services.MarkdownToHtml;
import com.remonsinnema.read2understand.domain.services.Summarizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadImpl implements Upload {

  private final Summarizer summarizer;
  private final HighlightsExtractor notesExtractor;
  private final MarkdownToHtml markdownToHtml;

  @Override
  public String pdf(File pdf) {
    log.info("Summarizing {}", pdf.getName());
    var result = markdownToHtml.apply(summarizer.summarize(notesExtractor.extractFrom(pdf)));
    writeToHtml(pdf, result);
    return result;
  }

  private void writeToHtml(File pdf, String html) {
    try (var output = new PrintWriter(toHtmlFile(pdf))) {
      output.println("<html><body>");
      output.println(html);
      output.println("</body></html>");
    } catch (FileNotFoundException e) {
      log.error("Failed to write {} to HTML", pdf.getAbsolutePath(), e);
    }
  }

  private File toHtmlFile(File pdf) {
    return new File(pdf.getParent(), withHtmlExtension(pdf));
  }

  private String withHtmlExtension(File source) {
    return source.getName().substring(0, 1 + source.getName().lastIndexOf('.')) + "html";
  }
}
