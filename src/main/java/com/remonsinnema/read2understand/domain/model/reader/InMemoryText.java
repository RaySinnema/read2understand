package com.remonsinnema.read2understand.domain.model.reader;

import com.remonsinnema.read2understand.domain.model.info.TextInfo;
import lombok.Value;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


@Value
public class InMemoryText implements Text {

    TextInfo info;
    String text;

    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

}
