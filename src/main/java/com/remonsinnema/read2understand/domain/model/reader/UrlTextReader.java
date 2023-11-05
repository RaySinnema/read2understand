package com.remonsinnema.read2understand.domain.model.reader;

import com.remonsinnema.read2understand.domain.model.info.ArticleInfo;
import com.remonsinnema.read2understand.domain.model.info.Author;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.function.Predicate.not;


public class UrlTextReader implements TextReader {

    @Override
    public boolean canExtractTextFrom(URI source) {
        return source.getScheme().startsWith("http");
    }

    @Override
    public Text extractTextFrom(URI source) throws IOException {
        try (var output = new ByteArrayOutputStream()) {
            try (var input = source.toURL().openStream()) {
                input.transferTo(output);
            } catch (MalformedURLException e) {
                throw new IOException(e);
            }
            return extractTextFrom(source, output.toString(UTF_8));
        }
    }

    private Text extractTextFrom(URI source, String text) {
        if (text.contains("<html>") && text.contains("</html")) {
            try {
                return htmlArticle(text);
            } catch (Exception e) {
                // Ignore and fall back to default
            }
        }
        return new InMemoryText(new ArticleInfo(source), text);
    }

    private static InMemoryText htmlArticle(String html) {
        var document = Jsoup.parse(html);
        var textInfo = new ArticleInfo(titleFrom(document), authorsFrom(document));
        return new InMemoryText(textInfo, document.body().text());
    }

    private static String titleFrom(Document document) {
        var result = document.title();
        if (result.isBlank()) {
            var children = document.body().children().stream().toList();
            var index = 0;
            while (index < children.size() && children.get(index).text().isBlank()) {
                index++;
            }
            if (index < children.size() && "h1".equals(children.get(index).tagName())) {
                result = children.get(index).text();
            }
        }
        return result;
    }

    private static Collection<Author> authorsFrom(Document html) {
        return html.selectXpath("//a[rel='author']")
                .eachText()
                .stream()
                .filter(not(String::isBlank))
                .map(Author::new)
                .toList();
    }

}
