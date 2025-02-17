package org.processmining.stochasticbpmn.models.bpmn.extensions;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.plugins.bpmn.BpmnElement;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticFlow;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGateway;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGatewayWeightedFlow;

public abstract class BpmnExtensionElement extends BpmnElement {

    public BpmnExtensionElement(String tag) {
        super(tag);
    }

//    public abstract void marshall(BPMNDiagram diagram, StochasticGatewayWeightedFlow weightedFlow);
}