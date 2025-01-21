package com.remonsinnema.read2understand.domain.services;

import java.io.File;
import java.util.List;

public interface NotesExtractor {

  List<String> extractFrom(File pdf);
}
