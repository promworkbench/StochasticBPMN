package org.processmining.stochasticbpmn.algorithms.diagram.reader;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.BpmnDiagramBuilder;
import org.processmining.stochasticbpmn.algorithms.reader.BpmnReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectFilePathReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectFileReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;

import java.io.File;
import java.io.InputStream;

public class BpmnDiagramReader implements ObjectReader<InputStream, BPMNDiagram> {
    private final BpmnReader bpmnReader;
    private final BpmnDiagramBuilder diagramBuilder;

    public BpmnDiagramReader(BpmnReader bpmnReader, BpmnDiagramBuilder diagramBuilder) {
        this.bpmnReader = bpmnReader;
        this.diagramBuilder = diagramBuilder;
    }

    public static BpmnDiagramReader fromInputStream() {
        return new BpmnDiagramReader(BpmnReader.fromInputStream(), BpmnDiagramBuilder.getInstance());
    }

    public static ObjectReader<File, BPMNDiagram> fromFile() {
        return new ObjectFileReader<>(fromInputStream());
    }

    public static ObjectReader<String, BPMNDiagram> fromFileName() {
        return new ObjectFilePathReader<>(fromFile());
    }

    @Override
    public BPMNDiagram read(InputStream inputStream) throws Exception {
        return read(inputStream, "");
    }

    @Override
    public BPMNDiagram read(InputStream inputStream, String label) throws Exception {
        Bpmn sbpmn = bpmnReader.read(inputStream);
        return diagramBuilder.build(sbpmn, label);
    }
}
