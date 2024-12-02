package org.processmining.stochasticbpmn.algorithms.diagram.builder;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;
import org.processmining.plugins.bpmn.parameters.BpmnSelectDiagramParameters;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagramImpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StochasticBPMNDiagramBuilderImpl implements StochasticBPMNDiagramBuilder {
    @Override
    public StochasticBPMNDiagram build(StochasticBpmn sBpmn, String label, BpmnSelectDiagramParameters parameters) {
        StochasticBPMNDiagram newDiagram = new StochasticBPMNDiagramImpl(label);
        Map<String, BPMNNode> id2node = new HashMap<>();
        Map<String, Swimlane> id2lane = new HashMap<>();
        if (parameters.getDiagram() == BpmnSelectDiagramParameters.NODIAGRAM) {
            sBpmn.unmarshall(newDiagram, id2node, id2lane);
        } else {
            Collection<String> elements = parameters.getDiagram().getElements();
            sBpmn.unmarshall(newDiagram, elements, id2node, id2lane);
        }

        return newDiagram;
    }

    @Override
    public StochasticBPMNDiagram build(StochasticBpmn sBpmn, String label) {
        BpmnSelectDiagramParameters parameters = new BpmnSelectDiagramParameters();
        if (!sBpmn.getDiagrams().isEmpty()) {
            parameters.setDiagram(sBpmn.getDiagrams().iterator().next());
        } else {
            parameters.setDiagram(BpmnSelectDiagramParameters.NODIAGRAM);
        }

        return this.build(sBpmn, label, parameters);
    }
}
