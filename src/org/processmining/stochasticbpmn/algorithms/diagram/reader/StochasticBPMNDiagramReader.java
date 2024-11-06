package org.processmining.stochasticbpmn.algorithms.diagram.reader;

import org.processmining.stochasticbpmn.algorithms.diagram.builder.StochasticBPMNDiagramBuilder;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.algorithms.reader.StochasticBPMNReader;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class StochasticBPMNDiagramReader implements ObjectReader<InputStream, StochasticBPMNDiagram> {
    private final StochasticBPMNReader sbpmnReader;
    private final StochasticBPMNDiagramBuilder diagramBuilder;

    public StochasticBPMNDiagramReader(StochasticBPMNReader sbpmnReader, StochasticBPMNDiagramBuilder diagramBuilder) {
        this.sbpmnReader = sbpmnReader;
        this.diagramBuilder = diagramBuilder;
    }

    @Override
    public StochasticBPMNDiagram read(InputStream inputStream) throws IOException, XmlPullParserException {
        StochasticBpmn sbpmn = sbpmnReader.read(inputStream);
        return diagramBuilder.build(sbpmn);
    }
}
