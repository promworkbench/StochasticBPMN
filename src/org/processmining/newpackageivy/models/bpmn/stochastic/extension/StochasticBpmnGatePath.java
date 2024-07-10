package org.processmining.newpackageivy.models.bpmn.stochastic.extension;

import org.processmining.plugins.bpmn.BpmnText;

import java.util.Objects;

public class StochasticBpmnGatePath extends BpmnText {
    public StochasticBpmnGatePath() {
        super("outgoing");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StochasticBpmnGatePath)) {
            return false;
        }
        StochasticBpmnGatePath choice = (StochasticBpmnGatePath) o;
        return Objects.equals(text, choice.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
