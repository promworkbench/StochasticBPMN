package org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;

public class StochasticFlow extends Flow {
    public StochasticFlow(BPMNNode source, BPMNNode target, String label) {
        super(source, target, label);
//        setLabel(getLabel());
    }

    private StochasticGateway getStochasticGateway() {
        if (!(this.source instanceof StochasticGateway)) {
            throw new IllegalStateException("This edge is not outgoing edge from a gateway. Therefore it has no probability.");
        }
        return (StochasticGateway) this.source;
    }

    public double getProbability() {
        final StochasticGateway gateway = getStochasticGateway();
        return gateway.getProbability(this.getAttributeMap().get("Original id").toString());
    }


    public double getWeight() {
        final StochasticGateway gateway = getStochasticGateway();
        return gateway.getWeight(this.getAttributeMap().get("Original id").toString());
    }

    @Override
    public String getLabel() {
        return getWeight() + ": " + super.getLabel();
    }
}
