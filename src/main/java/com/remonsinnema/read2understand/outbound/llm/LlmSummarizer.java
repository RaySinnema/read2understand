package com.remonsinnema.read2understand.outbound.llm;

import static java.lang.System.lineSeparator;
import static java.time.LocalTime.now;
import static java.util.stream.Collectors.joining;

import com.remonsinnema.read2understand.domain.services.Summarizer;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LlmSummarizer implements Summarizer {

  private static final String START_PROMPT =
      """
      Please analyze these book highlights and create a knowledge synthesis that:
      1. Identifies and groups the main concepts and themes. Use those groups as headers in the response.
      2. Preserves important terminology, frameworks, and models.
      3. Highlights practical insights and actionable takeaways.
      4. Maintains connections between related ideas.

      Don't include any text in the response but the actual synthesis.

      Here are the highlights to analyze:

      """;

  private final ChatLanguageModel llm;

  @Override
  public String summarize(Collection<String> text) {
    log.info("Summarizing {} lines of text", text.size());
    var prompt = text.stream().collect(joining(lineSeparator()));
    log.info("Sending prompt of {} characters", prompt.length());
    var messages = List.of(new SystemMessage(START_PROMPT), new UserMessage(prompt));
    var start = now();
    try {
      var response = llm.generate(messages);
      log.info("AI response: {}", response);
      return response.content().text();
    } finally {
      log.info("Got response or timeout in {}", Duration.between(start, now()));
    }
  }
}
