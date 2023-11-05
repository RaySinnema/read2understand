package com.remonsinnema.read2understand.domain.model.reader;

import java.io.IOException;
import java.net.URI;


public interface TextReader {

    boolean canExtractTextFrom(URI source);

    Text extractTextFrom(URI source) throws IOException;

}
