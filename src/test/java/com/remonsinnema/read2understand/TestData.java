package com.remonsinnema.read2understand;

import com.remonsinnema.read2understand.domain.model.info.Author;
import com.remonsinnema.read2understand.domain.model.info.BookInfo;
import lombok.experimental.UtilityClass;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

import java.util.Collection;

import static java.lang.String.join;
import static java.lang.System.lineSeparator;


@UtilityClass
public class TestData {

    public static String someTitle() {
        return titles().sample();
    }

    private static Arbitrary<String> titles() {
        return words().list().ofMinSize(1).ofMaxSize(5).map(l -> join(" ", l));
    }

    private static Arbitrary<String> words() {
        return Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10);
    }

    public static Author someAuthor() {
        return authors().sample();
    }

    private static Arbitrary<Author> authors() {
        return words().list().ofMinSize(2).ofMaxSize(3)
                .map(n -> join(" ", n))
                .map(Author::new);
    }

    public static Collection<Author> someAuthors() {
        return authors().list().ofMinSize(2).ofMaxSize(4).sample();
    }

    public static BookInfo someBookinfo() {
        return bookInfos().sample();
    }

    private static Arbitrary<BookInfo> bookInfos() {
        return Combinators.combine(
                titles(),
                authors().list()
        ).as(BookInfo::new);
    }

    public String someText() {
        return texts().sample();
    }

    private Arbitrary<String> texts() {
        return paragraphs().list().ofMinSize(2).ofMaxSize(5)
                .map(p -> join(lineSeparator(), p));
    }

    private Arbitrary<String> paragraphs() {
        return sentences().list().ofMinSize(1).ofMaxSize(7)
                .map(s -> join(" ", s));
    }

    private Arbitrary<String> sentences() {
        return words().list().ofMinSize(4).ofMaxSize(25)
                .map(w -> join(" ", w))
                .map("%s."::formatted);
    }

}
