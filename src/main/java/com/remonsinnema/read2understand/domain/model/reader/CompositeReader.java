package com.remonsinnema.read2understand.domain.model.reader;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;


@RequiredArgsConstructor
public class CompositeReader implements TextReader {

    private final Collection<TextReader> readers;

    public CompositeReader(TextReader... readers) {
        this(Arrays.asList(readers));
    }

    @Override
    public boolean canExtractTextFrom(URI source) {
        return readers.stream().anyMatch(r -> r.canExtractTextFrom(source));
    }

    @Override
    public Text extractTextFrom(URI source) throws IOException {
        return readers.stream()
                .filter(r -> r.canExtractTextFrom(source))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't extract text from " + source))
                .extractTextFrom(source);
    }

}
