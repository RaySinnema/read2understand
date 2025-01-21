package com.remonsinnema.read2understand.domain.services;

import java.util.Collection;

public interface Summarizer {

  String summarize(Collection<String> text);
}
