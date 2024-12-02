package org.processmining.stochasticbpmn.algorithms.reader;

import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface StochasticBPMNReader extends ObjectReader<InputStream, StochasticBpmn> {
    static StochasticBPMNReader fromInputStream() {
        return new StochasticBPMNInputStreamReader();
    }

    static ObjectReader<File, StochasticBpmn> fromFile() {
        return new ObjectFileReader<>(fromInputStream());
    }

    static ObjectReader<String, StochasticBpmn> fromFileName() {
        return new ObjectFilePathReader<>(fromFile());
    }
}
