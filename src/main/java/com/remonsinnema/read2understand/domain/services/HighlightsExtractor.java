package com.remonsinnema.read2understand.domain.services;

import java.io.File;

public interface HighlightsExtractor {

  Highlights extractFrom(File pdf);
}
