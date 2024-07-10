package org.processmining.newpackageivy.models.bpmn.stochastic;

import org.processmining.newpackageivy.models.bpmn.extensions.BpmnExtensionElement;
import org.processmining.newpackageivy.models.bpmn.extensions.BpmnExtensionElements;
import org.processmining.newpackageivy.models.bpmn.stochastic.extension.StochasticBpmnGateDistribution;
import org.processmining.newpackageivy.models.bpmn.stochastic.extension.StochasticBpmnGateDistributionElement;
import org.processmining.newpackageivy.models.bpmn.stochastic.extension.StochasticBpmnGatePath;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.BpmnExclusiveGateway;
import org.processmining.plugins.bpmn.BpmnOutgoing;
import org.xmlpull.v1.XmlPullParser;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StochasticBpmnExclusiveGateway extends BpmnExclusiveGateway {
    private BpmnExtensionElements extensionElements;

    public StochasticBpmnExclusiveGateway() {
        super("exclusiveGateway");
        this.extensionElements = null;
    }

    @Override
    protected boolean importElements(XmlPullParser xpp, Bpmn bpmn) {
        if (super.importElements(xpp, bpmn)) {
            return true;
        } else if (xpp.getName().equals("extensionElements")) {
            final BpmnExtensionElements extensionElements = new BpmnExtensionElements(Collections.singletonMap("gateDistribution", StochasticBpmnGateDistribution.class));
            extensionElements.importElement(xpp, bpmn);
            this.extensionElements = extensionElements;
            return true;
        }
        return false;

    }

/*    @Override
    protected void checkValidity(Bpmn bpmn) {
                if (extensionElements.getExtensionElements().size() != 1) {
                bpmn.log(this.tag, xpp.getLineNumber(), "The gateway: " + this.id + " should have only one distribution");
            }
            final BpmnExtensionElement extensionElement = extensionElements.getExtensionElements().get(0);
            if (!(extensionElement instanceof StochasticBpmnGateDistribution)) {
                bpmn.log(this.tag, xpp.getLineNumber(), "The gateway: " + this.id + " has extension other than gateDistribution");
                return true;
            }
            probabilityDistribution = (StochasticBpmnGateDistribution) extensionElement;

        super.checkValidity(bpmn);
        if (Objects.isNull(getProbabilityDistribution())) {
            bpmn.log(this.tag, -1, "No probability distribution for the exclusive gate " + this.id);
        }
        Set<String> outgoingEdges = outgoings.stream().map(BpmnOutgoing::getText).collect(Collectors.toSet());

        for (StochasticBpmnGateDistributionElement element : getProbabilityDistribution().getDistributionElements()) {
            for (StochasticBpmnGatePath path : element.getPaths()) {
                if (!outgoingEdges.contains(path.getText())) {
                    bpmn.log(this.tag, -1, "For exclusive gate: " + this.id + " the probability distribution contains path: " + path.getText() + " not listed in the outgoing edges.");
                }
            }
        }
    }*/
}
