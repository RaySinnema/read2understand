package com.remonsinnema.read2understand.outbound.kindle;

import com.remonsinnema.read2understand.domain.services.Highlights;
import com.remonsinnema.read2understand.domain.services.HighlightsExtractor;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

@Service
public class KindleHighlightsExtractor implements HighlightsExtractor {

  private static final Pattern PAGE = Pattern.compile("(Highlight \\(Yellow\\) \\| )?(Page )?\\d+");
  private static final Pattern KINDLE =
      Pattern.compile("(Free Kindle instant preview.*)|(\\d+ Highlights.*)");

  @Override
  public Highlights extractFrom(File pdf) {
    try (var document = Loader.loadPDF(pdf)) {
      var text = new PDFTextStripper().getText(document);
      var result =
          Arrays.stream(text.split("\n"))
              .filter(line -> !KINDLE.matcher(line).matches())
              .filter(line -> !PAGE.matcher(line).matches())
              .toList();
      writeToText(pdf, result);
      return new Highlights(result.getFirst(), result.get(1), result.stream().skip(2).toList());
    } catch (Exception e) {
      throw new RuntimeException("Failed to extract notes from " + pdf, e);
    }
  }

  private void writeToText(File source, List<String> lines) {
    try (var output = new PrintWriter(textFileBasedOn(source))) {
      lines.forEach(output::println);
    } catch (Exception e) {
      throw new RuntimeException("Failed to copy text to file", e);
    }
  }

  private File textFileBasedOn(File source) {
    return new File(source.getParent(), withTxtExtension(source));
  }

  private String withTxtExtension(File source) {
    return source.getName().substring(0, 1 + source.getName().lastIndexOf('.')) + "txt";
  }
}
