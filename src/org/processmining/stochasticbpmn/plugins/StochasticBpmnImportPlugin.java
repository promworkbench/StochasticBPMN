package org.processmining.stochasticbpmn.plugins;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.stochasticbpmn.algorithms.reader.StochasticBPMNInputStreamReader;
import org.processmining.stochasticbpmn.algorithms.reader.StochasticBPMNReader;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;

import java.io.InputStream;

@Plugin(
        name = "Import Stochastic BPMN model from extended BPMN 2.0 file",
        level = PluginLevel.NightlyBuild,
        parameterLabels = {"Filename"},
        returnLabels = {"Stochastic BPMN"},
        returnTypes = {StochasticBpmn.class})
@UIImportPlugin(
        description = "Stochastic BPMN 2.0 files",
        extensions = {"bpmn", "xml"},
        pack = "StochasticBPMN")
public class StochasticBpmnImportPlugin extends AbstractImportPlugin {
    private final StochasticBPMNReader modelLoader;

    public StochasticBpmnImportPlugin() {
        this.modelLoader = new StochasticBPMNInputStreamReader();
    }

    public StochasticBpmn importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes) throws Exception {
        final StochasticBpmn bpmn = modelLoader.read(input);

        if (bpmn.hasErrors()) {
            context.getProvidedObjectManager().createProvidedObject("Log of BPMN import", bpmn.getLog(), XLog.class, context);
            return null;
        } else {
            context.getFutureResult(0).setLabel(filename);
            return bpmn;
        }
    }
}
