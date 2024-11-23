package org.processmining.stochasticbpmn.algorithms.diagram.reader;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.BpmnDiagramBuilder;
import org.processmining.stochasticbpmn.algorithms.reader.BpmnReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;

import java.io.InputStream;

public class BpmnDiagramReader implements ObjectReader<InputStream, BPMNDiagram> {
    private final BpmnReader bpmnReader;
    private final BpmnDiagramBuilder diagramBuilder;

    public BpmnDiagramReader(BpmnReader bpmnReader, BpmnDiagramBuilder diagramBuilder) {
        this.bpmnReader = bpmnReader;
        this.diagramBuilder = diagramBuilder;
    }

    @Override
    public BPMNDiagram read(InputStream inputStream) throws Exception {
        Bpmn sbpmn = bpmnReader.read(inputStream);
        return diagramBuilder.build(sbpmn);
    }
}
