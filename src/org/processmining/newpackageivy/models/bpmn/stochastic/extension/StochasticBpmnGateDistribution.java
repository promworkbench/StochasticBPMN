package org.processmining.newpackageivy.models.bpmn.stochastic.extension;

import org.processmining.newpackageivy.models.bpmn.extensions.BpmnExtensionElement;
import org.processmining.plugins.bpmn.Bpmn;
import org.xmlpull.v1.XmlPullParser;

import java.util.Collection;
import java.util.LinkedList;

public class StochasticBpmnGateDistribution extends BpmnExtensionElement {
    private final Collection<StochasticBpmnGateDistributionElement> distributionElements;

    public StochasticBpmnGateDistribution() {
        super("gateDistribution");
        this.distributionElements = new LinkedList<>();
    }

    @Override
    protected boolean importElements(XmlPullParser xpp, Bpmn bpmn) {
        if (super.importElements(xpp, bpmn)) {
            return true;
        } else if (xpp.getName().equals("gateDistributionElement")) {
            final StochasticBpmnGateDistributionElement element = new StochasticBpmnGateDistributionElement();
            element.importElement(xpp, bpmn);
            this.distributionElements.add(element);
            return true;
        }
        return false;
    }

    public Collection<StochasticBpmnGateDistributionElement> getDistributionElements() {
        return distributionElements;
    }

/*    @Override
    protected void checkValidity(Bpmn bpmn) {
        super.checkValidity(bpmn);

        double totalProbabilityMass = 0.0;

        for (StochasticBpmnGateDistributionElement element : getDistributionElements()) {
            totalProbabilityMass += element.getProbability();
            for (StochasticBpmnGateDistributionElement element2 : getDistributionElements()) {
                if (!element.getPaths().equals(element2.getPaths())) {
                    throw new IllegalStateException("2 Distributions with same paths.");
                }
            }
        }
        if (totalProbabilityMass != 1.0) {
            throw new IllegalStateException("Probability mass is not 1.0");
        }
    }*/
}
