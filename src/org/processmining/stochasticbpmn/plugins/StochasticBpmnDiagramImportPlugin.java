package org.processmining.stochasticbpmn.plugins;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

import java.io.InputStream;
import java.util.Objects;

@Plugin(
        name = "Import Stochastic BPMN Diagram model from extended BPMN 2.0 file",
        level = PluginLevel.NightlyBuild,
        parameterLabels = {"Filename"},
        returnLabels = {"Stochastic BPMN Diagram"},
        returnTypes = {StochasticBPMNDiagram.class})
@UIImportPlugin(
        description = "Stochastic BPMN 2.0 files (Diagram)",
        extensions = {"bpmn", "xml"},
        pack = "StochasticBPMNDiagram")
public class StochasticBpmnDiagramImportPlugin extends AbstractImportPlugin {
    private final StochasticBPMNDiagramReader diagramReader;

    public StochasticBpmnDiagramImportPlugin() {
        this.diagramReader = StochasticBPMNDiagramReader.fromInputStream();
    }

    public StochasticBPMNDiagram importFromStream(PluginContext context, InputStream input, String filename,
                                                  long fileSizeInBytes) throws Exception {
        StochasticBPMNDiagram diagram = diagramReader.read(input, filename);
        if (Objects.isNull(diagram)) {
            return null;
        }
        context.getFutureResult(0).setLabel(filename);
        return diagram;
    }
}
