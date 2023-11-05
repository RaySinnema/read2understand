package com.remonsinnema.read2understand.domain.model.info;

import org.junit.jupiter.api.Test;

import static com.remonsinnema.read2understand.TestData.someAuthor;
import static com.remonsinnema.read2understand.TestData.someTitle;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


class WhenMaintainingInfoAboutTexts {

    @Test
    void shouldMaintainBookInfo() {
        var title = someTitle();
        var author = someAuthor();

        var text = new BookInfo(title, author);

        assertThat(text.getTitle(), is(title));
        assertThat(text.getAuthors().size(), is(1));
        assertThat(text.getAuthors().contains(author), is(true));
    }

}
