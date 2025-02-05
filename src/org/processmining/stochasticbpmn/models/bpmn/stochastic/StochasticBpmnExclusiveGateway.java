package org.processmining.stochasticbpmn.models.bpmn.stochastic;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.models.graphbased.directed.bpmn.elements.SubProcess;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;
import org.processmining.plugins.bpmn.*;
import org.processmining.stochasticbpmn.models.bpmn.extensions.BpmnExtensionElement;
import org.processmining.stochasticbpmn.models.bpmn.extensions.BpmnExtensionElements;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.extension.StochasticBpmnGatewayOutgoing;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.extension.StochasticBpmnGatewayWeightedElement;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.extension.StochasticBpmnGatewayWeights;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGateway;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGatewayWeightedFlow;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;
import java.util.*;
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
            final BpmnExtensionElements extensionElements = new BpmnExtensionElements(Collections.singletonMap("gatewayWeights", StochasticBpmnGatewayWeights.class));
            extensionElements.importElement(xpp, bpmn);
            this.extensionElements = extensionElements;
            return true;
        }
        return false;
    }

    protected String exportElements() {
        StringBuilder s = new StringBuilder();
        if(!this.extensionElements.getExtensionElements().isEmpty()) {
            s.append(this.extensionElements.exportElements());
        }
        for (BpmnIncoming incoming : incomings) {
            s.append(incoming.exportElement());
        }
        return s.toString();
    }

    @Override
    protected void checkValidity(Bpmn bpmn) {
        super.checkValidity(bpmn);
        if (this.outgoings.size() >= 2) {
            final Optional<StochasticBpmnGatewayWeights> weightsOpt = getWeights();
            if (!weightsOpt.isPresent()) {
                bpmn.log(this.tag, -1, "The diverging gateway: " + this.id + " should have one and only one extension set of weights.");
            }
            final StochasticBpmnGatewayWeights weights = weightsOpt.get();

            Set<String> outgoingEdges = outgoings.stream().map(BpmnOutgoing::getText).collect(Collectors.toSet());

            for (StochasticBpmnGatewayWeightedElement element : weights.getWeightedElements()) {
                if (element.getOutgoing().size() != 1) {
                    // Special case for the exclusive gateway.
                    bpmn.log(this.tag, -1, "For exclusive gate: " + this.id + " each weighted element should contain exactly one path.");
                }
                for (StochasticBpmnGatewayOutgoing path : element.getOutgoing()) {
                    if (!outgoingEdges.contains(path.getText())) {
                        bpmn.log(this.tag, -1, "For exclusive gate: " + this.id + " the weighted elements contain a path: " + path.getText() + " not listed in the outgoing edges.");
                    }
                }
            }
        }
    }

    public void unmarshall(BPMNDiagram diagram, Map<String, BPMNNode> id2node, Swimlane lane) {
        final Optional<StochasticBpmnGatewayWeights> weightsOpt = getWeights();
        if (weightsOpt.isPresent()) {
            final StochasticBPMNDiagram sDiagram = (StochasticBPMNDiagram) diagram;
            final StochasticGateway gateway = sDiagram.addStochasticGateway(this.name, Gateway.GatewayType.DATABASED, lane, createWeightedFlow(weightsOpt.get()));
            gateway.getAttributeMap().put("Original id", this.id);
            id2node.put(this.id, gateway);
        } else {
            super.unmarshall(diagram, id2node, lane);
        }

    }

    public void unmarshall(BPMNDiagram diagram, Collection<String> elements, Map<String, BPMNNode> id2node, Swimlane lane) {
        if (elements.contains(this.id)) {
            this.unmarshall(diagram, id2node, lane);
        }

    }

    public void unmarshall(BPMNDiagram diagram, Map<String, BPMNNode> id2node, SubProcess subProcess) {
        final Optional<StochasticBpmnGatewayWeights> weightsOpt = getWeights();
        if (weightsOpt.isPresent()) {
            final StochasticBPMNDiagram sDiagram = (StochasticBPMNDiagram) diagram;
            final StochasticGateway gateway = sDiagram.addStochasticGateway(this.name, Gateway.GatewayType.DATABASED, subProcess, createWeightedFlow(weightsOpt.get()));
            gateway.getAttributeMap().put("Original id", this.id);
            id2node.put(this.id, gateway);
        } else {
            super.unmarshall(diagram, id2node, subProcess);
        }

    }

    public void unmarshall(BPMNDiagram diagram, Collection<String> elements, Map<String, BPMNNode> id2node, SubProcess subProcess) {
        if (elements.contains(this.id)) {
            this.unmarshall(diagram, id2node, subProcess);
        }
    }

    public void marshall(BPMNDiagram diagram, Gateway gateway){
        super.marshall(gateway);
        int incoming = 0;
        int outgoing = 0;

        for(BPMNEdge<? extends BPMNNode, ? extends BPMNNode> e: diagram.getEdges()) {
            if(e.getTarget().equals(gateway)) {
                BpmnIncoming in = new BpmnIncoming("incoming");
                in.setText(e.getEdgeID().toString().replace(" ", "_"));
                incomings.add(in);
                incoming++;
            }
            if (e.getSource().equals(gateway)) {
                BpmnOutgoing out = new BpmnOutgoing("outgoing");
                out.setText(e.getEdgeID().toString().replace(" ", "_"));
                outgoings.add(out);
                outgoing++;
            }
        }
        String direction;
        if (incoming > 1 && outgoing > 1) {
            direction = "Mixed";
        } else if (incoming == 1 && outgoing > 1) {
            direction = "Diverging";
        } else if (incoming > 1 && outgoing == 1) {
            direction = "Converging";
        } else {
            direction = "Unspecified";
        }
        setPrivateField("gatewayDirection", direction, BpmnAbstractGateway.class);
        if (gateway.getDefaultFlow() != null) {
            String defaultFlowValue = gateway.getDefaultFlow().getEdgeID().toString().replace(" ", "_");
            setPrivateField("defaultFlow", defaultFlowValue, BpmnAbstractGateway.class);
        }

        Map<String, Class<? extends BpmnExtensionElement>> supportedExtensionElements = new HashMap<>();
        supportedExtensionElements.put("StochasticBpmnGatewayWeights", StochasticBpmnGatewayWeights.class);
        BpmnExtensionElements extensionElements = new BpmnExtensionElements(Collections.singletonMap("sbpmn:gatewayWeights", StochasticBpmnGatewayWeights.class));
        extensionElements.marshall();
        this.extensionElements = extensionElements;
    }

    private Optional<StochasticBpmnGatewayWeights> getWeights() {
        if (extensionElements == null) {
            return Optional.empty();
        }
        for (BpmnExtensionElement extensionElement : extensionElements.getExtensionElements()) {
            if (extensionElement instanceof StochasticBpmnGatewayWeights) {
                return Optional.of((StochasticBpmnGatewayWeights) extensionElement);
            }
        }
        return Optional.empty();
    }

    private StochasticGatewayWeightedFlow createWeightedFlow(final StochasticBpmnGatewayWeights distribution) {
        final StochasticGatewayWeightedFlow weightedFlow = new StochasticGatewayWeightedFlow();
        for (StochasticBpmnGatewayWeightedElement wElement : distribution.getWeightedElements()) {
            String[] outgoingEdges = wElement.getOutgoing().stream().map(Object::toString).toArray(String[]::new);
            weightedFlow.assignFlowWeight(wElement.getWeight(), outgoingEdges);
        }
        return weightedFlow;
    }

    // Utility method to set private fields via reflection
    private void setPrivateField(String fieldName, Object value, Class<?> targetClass) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
