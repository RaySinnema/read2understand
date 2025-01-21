package com.remonsinnema.read2understand.application.services;

import com.remonsinnema.read2understand.domain.services.MarkdownToHtml;
import com.remonsinnema.read2understand.domain.services.NotesExtractor;
import com.remonsinnema.read2understand.domain.services.Summarizer;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadImpl implements Upload {

  private final Summarizer summarizer;
  private final NotesExtractor notesExtractor;
  private final MarkdownToHtml markdownToHtml;

  @Override
  public String pdf(File pdf) {
    log.info("Summarizing {}", pdf.getName());
    return markdownToHtml.apply(summarizer.summarize(notesExtractor.extractFrom(pdf)));
  }
}
