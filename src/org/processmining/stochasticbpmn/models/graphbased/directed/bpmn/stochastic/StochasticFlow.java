package org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.stochasticbpmn.models.stochastic.Probability;
import org.processmining.stochasticbpmn.models.stochastic.StochasticObject;

import java.math.BigDecimal;

public class StochasticFlow extends Flow implements StochasticObject {
    public StochasticFlow(BPMNNode source, BPMNNode target, String label) {
        super(source, target, label);
    }

    private StochasticGateway getStochasticGateway() {
        if (!(this.source instanceof StochasticGateway)) {
            throw new IllegalStateException("This edge is not outgoing edge from a gateway. Therefore it has no probability.");
        }
        return (StochasticGateway) this.source;
    }

    public Probability getProbability() {
        final StochasticGateway gateway = getStochasticGateway();
        return gateway.getProbability(this);
    }

    public BigDecimal getWeight() {
        final StochasticGateway gateway = getStochasticGateway();
        return gateway.getWeight(this);
    }

    @Override
    public String getLabel() {
        return getWeight() + ": " + super.getLabel();
    }
}
