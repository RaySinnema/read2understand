package com.remonsinnema.read2understand.outbound.markdown;

import com.remonsinnema.read2understand.domain.services.MarkdownToHtml;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommonMarkdownToHtml implements MarkdownToHtml {

  private final Parser parser = Parser.builder().build();
  private final HtmlRenderer renderer = HtmlRenderer.builder().build();

  @Override
  public String apply(String markdown) {
    try {
      var document = parser.parse(markdown);
      return renderer.render(document);
    } catch (Exception e) {
      log.error("Failed to convert markdown to HTML", e);
      return markdown;
    }
  }
}
