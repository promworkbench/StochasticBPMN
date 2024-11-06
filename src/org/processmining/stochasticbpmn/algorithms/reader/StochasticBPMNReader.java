package org.processmining.stochasticbpmn.algorithms.reader;

import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public interface StochasticBPMNReader extends ObjectReader<InputStream, StochasticBpmn> {
    StochasticBpmn read(final InputStream input) throws XmlPullParserException, IOException;
}
