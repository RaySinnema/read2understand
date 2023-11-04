package com.remonsinnema.read2understand.domain.model.reader;

import com.remonsinnema.read2understand.domain.model.info.TextInfo;

import java.io.InputStream;


public interface Text {

    TextInfo getInfo();

    InputStream getContent();

}
