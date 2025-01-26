package com.remonsinnema.read2understand.outbound.llm;

import static java.lang.System.lineSeparator;
import static java.time.LocalTime.now;
import static java.util.stream.Collectors.joining;

import com.remonsinnema.read2understand.domain.services.Highlights;
import com.remonsinnema.read2understand.domain.services.Summarizer;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LlmSummarizer implements Summarizer {

  private static final String SYSTEM_PROMPT =
      """
      Please analyze these book highlights and create a detailed, actionable synthesis for regular re-reading. The synthesis should aim for approximately 1000 words to ensure that sufficient detail is retained and that the most important concepts are fully covered.

      1. **Organize the content under clear headers** representing the main themes from the book.
      2. For each theme:
          - **Define key terms and models** concisely, preserving their original phrasing if possible.
          - **Group related insights and actionable takeaways** under each theme. Present them clearly and avoid redundancy.
          - **Present models or processes** in a summarized form, making sure they are described without repetition.
      3. **Do not include any generic commentary** about the task or the author (e.g., "The text appears to be..."). Avoid any vague summaries like "The text covers..." or "Overall, the text provides..."
      4. **Always refer to the book by its title** when summarizing or concluding. Replace phrases like "the text" or "the guide" with the book title
      5. **Focus on actionable details** and practical insights, and eliminate any superfluous or generic statements.
      6. **Ensure sufficient detail** in each section to approach a final word count of around 1000 words.

      ### Formatting Guidelines:
      - **Use at least 8–10 sections** to cover the key themes in sufficient depth.
      - **No conclusions or commentary** at the beginning or end, just a clean, detailed summary.

      The text that follows includes the title of the book on the first line, the author on the second, and the highlights thereafter.

      Here is the text to summarize:

      """;

  private final ChatLanguageModel llm;

  @Override
  public String summarize(Highlights highlights) {
    log.info("Summarizing {} words of highlights", highlights.numWords());
    var prompt = userPromptFor(highlights);
    var messages = List.of(new SystemMessage(SYSTEM_PROMPT), new UserMessage(prompt));
    var start = now();
    try {
      var response = llm.generate(messages);
      return postProcessResponse(highlights, response.content().text());
    } finally {
      log.info("Got response or timeout in {}", Duration.between(start, now()));
    }
  }

  private String userPromptFor(Highlights highlights) {
    return Stream.concat(titleAndAuthor(highlights), highlights.text().stream())
        .collect(joining(lineSeparator()));
  }

  private Stream<String> titleAndAuthor(Highlights highlights) {
    return Stream.of("%s%n%s%n".formatted(highlights.title(), highlights.author()));
  }

  private String postProcessResponse(Highlights highlights, String response) {
    return "# %s - %s%n%n%s".formatted(highlights.title(), highlights.author(), response);
  }
}
