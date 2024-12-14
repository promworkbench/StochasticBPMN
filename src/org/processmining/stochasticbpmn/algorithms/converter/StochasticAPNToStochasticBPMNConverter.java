package org.processmining.stochasticbpmn.algorithms.converter;

import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.petrinets.stochastic.StochasticAPN;

public interface StochasticAPNToStochasticBPMNConverter {

    static StochasticAPNToStochasticBPMNConverter getDefault() {
        return new StochasticAPNToStochasticBPMNEnhancedConverter();
    }

    StochasticBPMNDiagram convert(StochasticAPN stochasticAPN);
}
