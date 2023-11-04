package com.remonsinnema.read2understand.domain.model.reader;

import java.net.URI;


@FunctionalInterface
public interface TextReader {

    Text extractText(URI source);

}
