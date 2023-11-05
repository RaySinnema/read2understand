package com.remonsinnema.read2understand.domain.model.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.Collection;

import static java.util.Collections.emptyList;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleInfo implements TextInfo {

    private String title;
    private Collection<Author> authors;

    public ArticleInfo(URI source) {
        this(source.toString(), emptyList());
    }

}
