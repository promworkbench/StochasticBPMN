package org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic;

import org.processmining.models.graphbased.directed.AbstractDirectedGraph;
import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.models.graphbased.directed.bpmn.elements.SubProcess;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;

public class StochasticGateway extends Gateway {
    private final StochasticGatewayWeightedFlow weightedFlow;

    public StochasticGateway(AbstractDirectedGraph<BPMNNode, BPMNEdge<? extends BPMNNode, ? extends BPMNNode>> bpmndiagram, String label, GatewayType gatewayType, final StochasticGatewayWeightedFlow weightedFlow) {
        super(bpmndiagram, label, gatewayType);
        this.weightedFlow = weightedFlow;
    }

    public StochasticGateway(AbstractDirectedGraph<BPMNNode, BPMNEdge<? extends BPMNNode, ? extends BPMNNode>> bpmndiagram, String label, GatewayType gatewayType, Swimlane parentSwimlane, final StochasticGatewayWeightedFlow weightedFlow) {
        super(bpmndiagram, label, gatewayType, parentSwimlane);
        this.weightedFlow = weightedFlow;
    }

    public StochasticGateway(AbstractDirectedGraph<BPMNNode, BPMNEdge<? extends BPMNNode, ? extends BPMNNode>> bpmndiagram, String label, GatewayType gatewayType, SubProcess parentSubProcess, final StochasticGatewayWeightedFlow weightedFlow) {
        super(bpmndiagram, label, gatewayType, parentSubProcess);
        this.weightedFlow = weightedFlow;
    }

    public double getProbability(final String ...outgoingEdges) {
        return this.weightedFlow.getFlowProbability(outgoingEdges);
    }

    public double getWeight(final String ...outgoingEdges) {
        return this.weightedFlow.getFlowWeight(outgoingEdges);
    }
}
