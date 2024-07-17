package org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagramImpl;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.models.graphbased.directed.bpmn.elements.SubProcess;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;

public class StochasticBPMNDiagramImpl extends BPMNDiagramImpl implements StochasticBPMNDiagram {
    public StochasticBPMNDiagramImpl(String label) {
        super(label);
    }


    @Override
    public StochasticGateway addStochasticGateway(String label, Gateway.GatewayType gatewayType, StochasticGatewayWeightedFlow weightedFlow) {
        StochasticGateway g = new StochasticGateway(this, label, gatewayType, weightedFlow);
        this.gateways.add(g);
        this.graphElementAdded(g);
        return g;
    }

    @Override
    public StochasticGateway addStochasticGateway(String label, Gateway.GatewayType gatewayType, SubProcess parentSubProcess, StochasticGatewayWeightedFlow weightedFlow) {
        StochasticGateway g = new StochasticGateway(this, label, gatewayType, parentSubProcess, weightedFlow);
        this.gateways.add(g);
        this.graphElementAdded(g);
        return g;
    }

    @Override
    public StochasticGateway addStochasticGateway(String label, Gateway.GatewayType gatewayType, Swimlane lane, StochasticGatewayWeightedFlow weightedFlow) {
        StochasticGateway g = new StochasticGateway(this, label, gatewayType, lane, weightedFlow);
        this.gateways.add(g);
        this.graphElementAdded(g);
        return g;
    }

    @Override
    public StochasticFlow addStochasticFlow(BPMNNode source, BPMNNode target, String label) {
        StochasticFlow f = new StochasticFlow(source, target, label);
        this.flows.add(f);
        this.graphElementAdded(f);
        return f;
    }
}
