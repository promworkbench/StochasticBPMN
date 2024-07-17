package org.processmining.newpackageivy.models.bpmn.stochastic.extension;

import org.processmining.plugins.bpmn.BpmnText;

import java.util.Objects;

public class StochasticBpmnGatewayOutgoing extends BpmnText {
    public StochasticBpmnGatewayOutgoing() {
        super("outgoing");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StochasticBpmnGatewayOutgoing)) {
            return false;
        }
        StochasticBpmnGatewayOutgoing choice = (StochasticBpmnGatewayOutgoing) o;
        return Objects.equals(text, choice.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
