package org.processmining.stochasticbpmn.models.bpmn.extensions;


import org.apache.batik.extension.ExtensionElement;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.BpmnElement;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.extension.StochasticBpmnGatewayWeights;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticFlow;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGateway;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGatewayWeightedFlow;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BpmnExtensionElements extends BpmnElement {
    private final List<BpmnExtensionElement> extensionElements;

    private final Map<String, Class<? extends BpmnExtensionElement>> supportedExtensionElements;
    public BpmnExtensionElements(final Map<String, Class<? extends BpmnExtensionElement>> supportedExtensionElements) {
        super("extensionElements");
        this.supportedExtensionElements = supportedExtensionElements;
        this.extensionElements = new LinkedList<>();
    }

    @Override
    protected boolean importElements(XmlPullParser xpp, Bpmn bpmn) {
        if (super.importElements(xpp, bpmn)) {
            return true;
        } else {
            for (Map.Entry<String, Class<? extends BpmnExtensionElement>> supportedElement : this.supportedExtensionElements.entrySet()) {

                if (xpp.getName().equals(supportedElement.getKey())) {
                    try {
                        BpmnExtensionElement extensionElement = supportedElement.getValue().getConstructor().newInstance();
                        extensionElement.importElement(xpp, bpmn);
                        extensionElements.add(extensionElement);
                        return true;
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        bpmn.log(this.tag, xpp.getLineNumber(), e.getMessage());
                    }
                }
            }
            return false;
        }
    }

    public void addExtensionElement(BpmnExtensionElement extensionElement) {
        this.extensionElements.add(extensionElement);
    }

    public String exportElements() {
        StringBuilder s = new StringBuilder();
        if (!extensionElements.isEmpty()) {
            for (BpmnExtensionElement element : extensionElements) {
                s.append(element.exportElement());
            }
        }
        return s.toString();
    }


    public List<BpmnExtensionElement> getExtensionElements() {
        return extensionElements;
    }
}
