package org.processmining.stochasticbpmn.algorithms.reader;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ObjectFileReader<OUTPUT> implements ObjectReader<File, OUTPUT> {

    private final ObjectReader<InputStream, OUTPUT> reader;

    public ObjectFileReader(final ObjectReader<InputStream, OUTPUT> reader) {
        this.reader = reader;
    }

    @Override
    public OUTPUT read(File file) throws IOException, XmlPullParserException {
        return reader.read(Files.newInputStream(file.toPath()));
    }
}
