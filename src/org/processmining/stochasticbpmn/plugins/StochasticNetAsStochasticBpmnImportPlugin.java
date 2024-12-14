package org.processmining.stochasticbpmn.plugins;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.StochasticNet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.stochasticbpmn.Main;
import org.processmining.stochasticbpmn.algorithms.converter.StochasticAPNToStochasticBPMNConverter;
import org.processmining.stochasticbpmn.algorithms.reader.StochasticAPNReader;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.petrinets.stochastic.StochasticAPN;

import java.io.InputStream;

@Plugin(
        name = "Import Stochastic net as a Stochastic BPMN siagram",
        parameterLabels = {"Filename"},
        returnLabels = {"Stochastic BPMN Diagram"},
        returnTypes = {StochasticBPMNDiagram.class})
@UIImportPlugin(
        description = "Stochastic net files",
        extensions = {"pnml"})
public class StochasticNetAsStochasticBpmnImportPlugin {

    protected StochasticBPMNDiagram importFromStream(PluginContext context, InputStream input, String filename,
                                      long fileSizeInBytes) throws Exception {
        StochasticAPN stochasticAPN = StochasticAPNReader.getDefaultReader().read(input);
        StochasticBPMNDiagram diagram = StochasticAPNToStochasticBPMNConverter.getDefault().convert(stochasticAPN);
        context.getFutureResult(0).setLabel(filename);
        return diagram;
    }
}
