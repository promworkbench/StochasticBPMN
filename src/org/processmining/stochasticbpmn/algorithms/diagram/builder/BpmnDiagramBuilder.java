package org.processmining.stochasticbpmn.algorithms.diagram.builder;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.parameters.BpmnSelectDiagramParameters;

public interface BpmnDiagramBuilder {
    BPMNDiagram build(final Bpmn bpmn, final BpmnSelectDiagramParameters parameters);

    BPMNDiagram build(final Bpmn bpmn);
}
