package org.processmining.stochasticbpmn.models.bpmn.stochastic.extension;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.stochasticbpmn.models.bpmn.extensions.BpmnExtensionElement;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.*;
import org.xmlpull.v1.XmlPullParser;

import java.util.Collection;
import java.util.HashSet;

public class StochasticBpmnGatewayWeights extends BpmnExtensionElement {
    private final Collection<StochasticBpmnGatewayWeightedElement> weightedElements;

    public StochasticBpmnGatewayWeights() {
        super("gatewayWeights");
        this.weightedElements = new HashSet<>();
    }

    @Override
    protected boolean importElements(XmlPullParser xpp, Bpmn bpmn) {
        if (super.importElements(xpp, bpmn)) {
            return true;
        } else if (xpp.getName().equals("gatewayWeightedElement")) {
            final StochasticBpmnGatewayWeightedElement element = new StochasticBpmnGatewayWeightedElement();
            element.importElement(xpp, bpmn);
            this.weightedElements.add(element);
            return true;
        }
        return false;
    }

    public String exportElements() {
        StringBuilder s = new StringBuilder();
        if(!this.weightedElements.isEmpty()) {
            for(StochasticBpmnGatewayWeightedElement element : weightedElements) {
                s.append(element.exportElement());
            }
        }
        return s.toString();
    }

    public void marshall(BPMNDiagram diagram, StochasticGatewayWeightedFlow weightedFlow) {
        for (StochasticGatewayFlowSet weightedFlowSet : weightedFlow.getWeightedFlowSets()) {
            StochasticBpmnGatewayWeightedElement element = new StochasticBpmnGatewayWeightedElement();
            element.marshall(diagram, weightedFlowSet, weightedFlow.getFlowWeight(weightedFlowSet));
            this.weightedElements.add(element);
        }

    }

    public Collection<StochasticBpmnGatewayWeightedElement> getWeightedElements() {
        return weightedElements;
    }
}
