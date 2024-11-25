package org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic;

import org.processmining.models.graphbased.directed.AbstractDirectedGraph;
import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.models.graphbased.directed.bpmn.elements.SubProcess;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public double getWeight(final Collection<BPMNEdge<? extends BPMNNode, ? extends BPMNNode>> outgoingEdges) {
        return this.weightedFlow.getFlowWeight(outgoingEdges.stream().map(this::getEdgeId).collect(Collectors.toList()));
    }

    public double getWeight(BPMNEdge<? extends BPMNNode, ? extends BPMNNode> outgoingEdge) {
        return weightedFlow.getFlowWeight(getEdgeId(outgoingEdge));
    }

    public double getProbability(BPMNEdge<? extends BPMNNode, ? extends BPMNNode> outgoingEdge) {
        return weightedFlow.getFlowProbability(getEdgeId(outgoingEdge));
    }

    public double getProbability(final Collection<BPMNEdge<? extends BPMNNode, ? extends BPMNNode>> outgoingEdges) {
        return this.weightedFlow.getFlowProbability(outgoingEdges.stream().map(this::getEdgeId).collect(Collectors.toList()));
    }

    private String getEdgeId(BPMNEdge<? extends BPMNNode, ? extends BPMNNode> edge) {
        return edge.getAttributeMap().get("Original id").toString();
    }
}
