package org.processmining.stochasticbpmn.algorithms.diagram.reader;

import org.processmining.stochasticbpmn.algorithms.diagram.builder.StochasticBPMNDiagramBuilder;
import org.processmining.stochasticbpmn.algorithms.reader.*;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

import java.io.File;
import java.io.InputStream;

public class StochasticBPMNDiagramReader implements ObjectReader<InputStream, StochasticBPMNDiagram> {
    private final StochasticBPMNReader sbpmnReader;
    private final StochasticBPMNDiagramBuilder diagramBuilder;

    public static StochasticBPMNDiagramReader fromInputStream() {
        return new StochasticBPMNDiagramReader(StochasticBPMNReader.fromInputStream(),
                StochasticBPMNDiagramBuilder.getInstance());
    }

    public static ObjectReader<File, StochasticBPMNDiagram> fromFile() {
        return new ObjectFileReader<>(fromInputStream());
    }

    public static ObjectReader<String, StochasticBPMNDiagram> fromFileName() {
        return new ObjectFilePathReader<>(fromFile());
    }

    public StochasticBPMNDiagramReader(StochasticBPMNReader sbpmnReader, StochasticBPMNDiagramBuilder diagramBuilder) {
        this.sbpmnReader = sbpmnReader;
        this.diagramBuilder = diagramBuilder;
    }

    @Override
    public StochasticBPMNDiagram read(InputStream inputStream) throws Exception {
        return read(inputStream, "");
    }

    @Override
    public StochasticBPMNDiagram read(InputStream inputStream, String label) throws Exception {
        StochasticBpmn sbpmn = sbpmnReader.read(inputStream);
        return diagramBuilder.build(sbpmn, label);
    }
}
