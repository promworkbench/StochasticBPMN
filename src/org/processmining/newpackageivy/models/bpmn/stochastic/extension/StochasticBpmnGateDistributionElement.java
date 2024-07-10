package org.processmining.newpackageivy.models.bpmn.stochastic.extension;

import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.BpmnElement;
import org.xmlpull.v1.XmlPullParser;

import java.util.Collection;
import java.util.HashSet;

public class StochasticBpmnGateDistributionElement extends BpmnElement {
    private double probability;

    private Collection<StochasticBpmnGatePath> paths;

    public StochasticBpmnGateDistributionElement() {
        super("gateDistributionElement");
        this.paths = new HashSet<>();
        this.probability = 0.0;
    }

    @Override
    protected boolean importElements(XmlPullParser xpp, Bpmn bpmn) {
        if (super.importElements(xpp, bpmn)) {
            return true;
        } else if (xpp.getName().equals("outgoing")) {
            StochasticBpmnGatePath outgoing = new StochasticBpmnGatePath();
            outgoing.importElement(xpp, bpmn);
            this.paths.add(outgoing);
            return true;
        }
        return false;
    }

    protected void importAttributes(XmlPullParser xpp, Bpmn bpmn) {
        super.importAttributes(xpp, bpmn);
        String value = xpp.getAttributeValue((String)null, "probabilityMass");
        if (value != null) {
            this.probability = Double.parseDouble(value);
        }
    }

    public double getProbability() {
        return probability;
    }

    public Collection<StochasticBpmnGatePath> getPaths() {
        return paths;
    }
}
