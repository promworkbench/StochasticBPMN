package org.processmining.stochasticbpmn.models.bpmn.stochastic.extension;

import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.BpmnElement;
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

    protected void importAttributes(XmlPullParser xpp, Bpmn bpmn) {
        super.importAttributes(xpp, bpmn);
        String value = xpp.getAttributeValue(null, "weight");
        if (value != null) {
            this.weight = new BigDecimal(value);
        }
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public Collection<StochasticBpmnGatewayOutgoing> getOutgoing() {
        return outgoingFlow;
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
