package com.remonsinnema.read2understand.domain.model;

import com.remonsinnema.read2understand.domain.model.info.BookInfo;
import com.remonsinnema.read2understand.domain.model.reader.InMemoryText;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.remonsinnema.read2understand.TestData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


class WhenReading {

    @Test
    void shouldMaintainBookInfo() {
        var title = someTitle();
        var author = someAuthor();

        var text = new BookInfo(title, author);

        assertThat(text.getTitle(), is(title));
        assertThat(text.getAuthors().size(), is(1));
        assertThat(text.getAuthors().contains(author), is(true));
    }

    @Test
    void shouldReadInMemoryText() throws IOException {
        var text = someText();
        var bookInfo = someBookinfo();

        var read = new InMemoryText(bookInfo, text);

        assertThat(read.getInfo(), is(bookInfo));
        try (var content = new ByteArrayOutputStream()) {
            read.getContent().transferTo(content);
            assertThat(content.toString(), is(text));
        }
    }

}
