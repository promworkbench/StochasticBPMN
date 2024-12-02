package org.processmining.stochasticbpmn.algorithms.reader;

import org.processmining.plugins.bpmn.Bpmn;

import java.io.File;
import java.io.InputStream;

public interface BpmnReader extends ObjectReader<InputStream, Bpmn> {
    static BpmnReader fromInputStream() {
        return new BpmnInputStreamReader();
    }

    static ObjectReader<File, Bpmn> fromFile() {
        return new ObjectFileReader<>(fromInputStream());
    }

    static ObjectReader<String, Bpmn> fromFileName() {
        return new ObjectFilePathReader<>(fromFile());
    }

    Bpmn read(final InputStream input) throws Exception;
}
