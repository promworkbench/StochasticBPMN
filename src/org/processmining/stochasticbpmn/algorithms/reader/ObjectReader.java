package org.processmining.stochasticbpmn.algorithms.reader;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public interface ObjectReader<INPUT, OUTPUT> {
    OUTPUT read(final INPUT input) throws Exception;
}
