package org.processmining.stochasticbpmn.models.petrinets.stochastic;

import org.processmining.models.graphbased.directed.petrinet.StochasticNet;
import org.processmining.models.semantics.petrinet.Marking;

public class StochasticAPN {
    private final StochasticNet stochasticNet;
    private final Marking initialMarking;
    private final Marking finalMarking;

    public StochasticAPN(StochasticNet stochasticNet, Marking initialMarking, Marking finalMarking) {
        this.stochasticNet = stochasticNet;
        this.initialMarking = initialMarking;
        this.finalMarking = finalMarking;
    }

    public StochasticNet getStochasticNet() {
        return stochasticNet;
    }

    public Marking getInitialMarking() {
        return initialMarking;
    }

    public Marking getFinalMarking() {
        return finalMarking;
    }
}
