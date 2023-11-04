package com.remonsinnema.read2understand.domain.model.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;


/**
 * Information about a book.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookInfo implements TextInfo {

    private String title;
    private Collection<Author> authors;

    public BookInfo(String title, Author author) {
        this(title, List.of(author));
    }

}
