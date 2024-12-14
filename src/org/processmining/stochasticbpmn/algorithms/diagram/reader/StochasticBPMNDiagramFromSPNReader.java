package org.processmining.stochasticbpmn.algorithms.diagram.reader;

import org.processmining.stochasticbpmn.algorithms.converter.StochasticAPNToStochasticBPMNConverter;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectFilePathReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectFileReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.algorithms.reader.StochasticAPNReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.petrinets.stochastic.StochasticAPN;

import java.io.File;
import java.io.InputStream;

public class StochasticBPMNDiagramFromSPNReader implements ObjectReader<InputStream, StochasticBPMNDiagram> {
    private final StochasticAPNReader spnReader;
    private final StochasticAPNToStochasticBPMNConverter spn2sbpmnConverter;

    public StochasticBPMNDiagramFromSPNReader(StochasticAPNReader spnReader,
                                              StochasticAPNToStochasticBPMNConverter spn2sbpmnConverter) {
        this.spnReader = spnReader;
        this.spn2sbpmnConverter = spn2sbpmnConverter;
    }

    public static StochasticBPMNDiagramFromSPNReader fromInputStream() {
        return new StochasticBPMNDiagramFromSPNReader(StochasticAPNReader.getDefaultReader(),
                StochasticAPNToStochasticBPMNConverter.getDefault());
    }

    public static ObjectReader<File, StochasticBPMNDiagram> fromFile() {
        return new ObjectFileReader<>(fromInputStream());
    }

    public static ObjectReader<String, StochasticBPMNDiagram> fromFileName() {
        return new ObjectFilePathReader<>(fromFile());
    }

    @Override
    public StochasticBPMNDiagram read(InputStream inputStream) throws Exception {
        return null;
    }

    @Override
    public StochasticBPMNDiagram read(InputStream inputStream, String label) throws Exception {
        StochasticAPN stochasticAPN = spnReader.read(inputStream);
        return spn2sbpmnConverter.convert(stochasticAPN);
    }
}
