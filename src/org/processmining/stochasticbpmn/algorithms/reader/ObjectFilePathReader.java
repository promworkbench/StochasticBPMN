package org.processmining.stochasticbpmn.algorithms.reader;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ObjectFilePathReader<OUTPUT> implements ObjectReader<String, OUTPUT> {
    private final ObjectReader<InputStream, OUTPUT> reader;

    public ObjectFilePathReader(ObjectReader<InputStream, OUTPUT> reader) {
        this.reader = reader;
    }

    @Override
    public OUTPUT read(String filename) throws Exception {
        final File file = new File(filename);
        return reader.read(Files.newInputStream(file.toPath()));
    }
}
