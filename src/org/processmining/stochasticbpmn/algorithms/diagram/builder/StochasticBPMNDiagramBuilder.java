package org.processmining.stochasticbpmn.algorithms.diagram.builder;

import org.processmining.plugins.bpmn.parameters.BpmnSelectDiagramParameters;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

public interface StochasticBPMNDiagramBuilder {
    StochasticBPMNDiagram build(final StochasticBpmn bpmn, final BpmnSelectDiagramParameters parameters);

    StochasticBPMNDiagram build(final StochasticBpmn bpmn);
}
