package com.remonsinnema.read2understand.domain.model.info;

import com.remonsinnema.read2understand.domain.model.info.Author;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Information about a text.
 */
public interface TextInfo {

    // Title

    String getTitle();

    void setTitle(String title);


    // Authors

    Collection<Author> getAuthors();

    void setAuthors(Collection<Author> authors);

    default void addAuthor(Author author) {
        var authors = new ArrayList<>(getAuthors());
        authors.add(author);
        setAuthors(authors);
    }


}
