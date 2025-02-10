package org.processmining.stochasticbpmn.models.bpmn.stochastic.extension;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.BpmnElement;
import org.processmining.plugins.bpmn.BpmnIncoming;
import org.processmining.plugins.bpmn.BpmnOutgoing;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGateway;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGatewayWeightedFlow;
import org.xmlpull.v1.XmlPullParser;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class StochasticBpmnGatewayWeightedElement extends BpmnElement {
    private BigDecimal weight;

    private final Collection<StochasticBpmnGatewayOutgoing> outgoingFlow;

    public StochasticBpmnGatewayWeightedElement() {
        super("gatewayWeightedElement");
        this.outgoingFlow = new HashSet<>();
        this.weight = BigDecimal.ZERO;
    }

    @Override
    protected boolean importElements(XmlPullParser xpp, Bpmn bpmn) {
        if (super.importElements(xpp, bpmn)) {
            return true;
        } else if (xpp.getName().equals("outgoing")) {
            StochasticBpmnGatewayOutgoing outgoing = new StochasticBpmnGatewayOutgoing();
            outgoing.importElement(xpp, bpmn);
            this.outgoingFlow.add(outgoing);
            return true;
        }
        return false;
    }

    public String exportElements() {
        StringBuilder s = new StringBuilder();
        if(!outgoingFlow.isEmpty()) {
            for(StochasticBpmnGatewayOutgoing outgoing : outgoingFlow) {
                s.append(outgoing.exportElement());
            }
        }
        return s.toString();
    }

    protected void importAttributes(XmlPullParser xpp, Bpmn bpmn) {
        super.importAttributes(xpp, bpmn);
        String value = xpp.getAttributeValue(null, "weight");
        if (value != null) {
            this.weight = new BigDecimal(value);
        }
    }

    protected String exportAttributes() {
        StringBuilder s = new StringBuilder();
        if (this.weight != null) {
            s.append(this.exportAttribute("weight", this.weight.toString()));
        }
        return s.toString();
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void marshall(BPMNDiagram diagram, Gateway gateway) {
        for(BPMNEdge<? extends BPMNNode, ? extends BPMNNode> e: diagram.getEdges()) {
            if (e.getSource().equals(gateway)) {
//                StochasticGatewayWeightedFlow weightedFlow = gateway.getWeightedFlow();
                StochasticBpmnGatewayOutgoing out = new StochasticBpmnGatewayOutgoing();
                out.setText(e.getEdgeID().toString().replace(" ", "_"));
                outgoingFlow.add(out);
//                weight = weightedFlow.getFlowWeight(getEdgeId(e));
            }
        }
    }

    public Collection<StochasticBpmnGatewayOutgoing> getOutgoing() {
        return outgoingFlow;
    }

    private String getEdgeId(BPMNEdge<? extends BPMNNode, ? extends BPMNNode> edge) {
        return edge.getAttributeMap().get("Original id").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StochasticBpmnGatewayWeightedElement)) return false;
        StochasticBpmnGatewayWeightedElement element = (StochasticBpmnGatewayWeightedElement) o;
        return Objects.equals(outgoingFlow, element.outgoingFlow);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(outgoingFlow);
    }
}
