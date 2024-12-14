package org.processmining.stochasticbpmn.algorithms.reader;

import org.processmining.stochasticbpmn.models.petrinets.stochastic.StochasticAPN;

import java.io.InputStream;

public interface StochasticAPNReader extends ObjectReader<InputStream, StochasticAPN> {

    static StochasticAPNReader getDefaultReader() {
        return new StochasticAPNInputStreamReader();
    }
}
