package org.processmining.stochasticbpmn.algorithms.diagram.builder;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagramImpl;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.parameters.BpmnSelectDiagramParameters;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BpmnDiagramBuilderImpl implements BpmnDiagramBuilder {
    @Override
    public BPMNDiagram build(Bpmn bpmn, String label, BpmnSelectDiagramParameters parameters) {
        BPMNDiagram newDiagram = new BPMNDiagramImpl(label);
        Map<String, BPMNNode> id2node = new HashMap<>();
        Map<String, Swimlane> id2lane = new HashMap<>();
        if (parameters.getDiagram() == BpmnSelectDiagramParameters.NODIAGRAM) {
            bpmn.unmarshall(newDiagram, id2node, id2lane);
        } else {
            Collection<String> elements = parameters.getDiagram().getElements();
            bpmn.unmarshall(newDiagram, elements, id2node, id2lane);
        }

        return newDiagram;
    }

    @Override
    public BPMNDiagram build(Bpmn bpmn, String label) {
        BpmnSelectDiagramParameters parameters = new BpmnSelectDiagramParameters();
        if (!bpmn.getDiagrams().isEmpty()) {
            parameters.setDiagram(bpmn.getDiagrams().iterator().next());
        } else {
            parameters.setDiagram(BpmnSelectDiagramParameters.NODIAGRAM);
        }

        return this.build(bpmn, label, parameters);
    }
}
