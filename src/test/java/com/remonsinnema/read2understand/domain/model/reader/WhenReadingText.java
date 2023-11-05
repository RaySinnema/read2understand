package com.remonsinnema.read2understand.domain.model.reader;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import static com.remonsinnema.read2understand.TestData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class WhenReadingText {

    @Test
    void shouldReadInMemoryText() throws IOException {
        var text = someText();
        var bookInfo = someBookinfo();

        var read = new InMemoryText(bookInfo, text);

        assertThat(read.getInfo(), is(bookInfo));
        assertContent(read, is(text));
    }

    private static void assertContent(Text actual, Matcher<String> expected) throws IOException {
        try (var content = new ByteArrayOutputStream()) {
            actual.getContent().transferTo(content);
            assertThat(content.toString(), expected);
        }
    }

    @Test
    void shouldReadTextUsingApplicableReader() throws IOException {
        var source = someUri();
        var text = new InMemoryText(someBookinfo(), someText());
        var reader1 = mock(TextReader.class);
        when(reader1.canExtractTextFrom(source)).thenReturn(false);
        var reader2 = mock(TextReader.class);
        when(reader2.canExtractTextFrom(source)).thenReturn(true);
        when(reader2.extractTextFrom(source)).thenReturn(text);

        var reader = new CompositeReader(reader1, reader2);

        assertThat(reader.canExtractTextFrom(source), is(true));
        assertThat(reader.extractTextFrom(source), is(text));
    }

    @Test
    void shouldRejectUnknownSource() {
        var source = someUri();
        var reader1 = mock(TextReader.class);
        when(reader1.canExtractTextFrom(any())).thenReturn(false);

        var reader = new CompositeReader(reader1);

        assertThat(reader.canExtractTextFrom(source), is(false));
        assertThrows(IllegalArgumentException.class, () ->
                reader.extractTextFrom(source));
    }

    @Test
    void shouldReadTextFromWeb() throws IOException {
        var source = URI.create("https://httpbin.org/html");

        var reader = new UrlTextReader();

        assertThat(reader.canExtractTextFrom(source), is(true));
        var read = reader.extractTextFrom(source);
        assertThat(read.getInfo().getAuthors(), hasSize(0));
        assertContent(read, not(containsString("<html>")));
    }

}
