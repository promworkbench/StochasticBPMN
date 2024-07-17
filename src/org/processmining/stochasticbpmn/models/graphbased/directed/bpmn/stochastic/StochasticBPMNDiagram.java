package org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.models.graphbased.directed.bpmn.elements.SubProcess;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;

public interface StochasticBPMNDiagram extends BPMNDiagram {
    StochasticGateway addStochasticGateway(String var1, Gateway.GatewayType var2, StochasticGatewayWeightedFlow weightedFlow);

    StochasticGateway addStochasticGateway(String var1, Gateway.GatewayType var2, SubProcess var3, StochasticGatewayWeightedFlow weightedFlow);

    StochasticGateway addStochasticGateway(String var1, Gateway.GatewayType var2, Swimlane var3, StochasticGatewayWeightedFlow weightedFlow);

    StochasticFlow addStochasticFlow(BPMNNode var1, BPMNNode var2, String var3);
}
