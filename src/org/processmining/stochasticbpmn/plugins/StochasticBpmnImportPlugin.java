package org.processmining.newpackageivy.plugins;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.newpackageivy.models.StochasticBpmn;

import java.io.InputStream;

@Plugin(name = "Import Stochastic BPMN model from extended BPMN 2.0 file", level = PluginLevel.NightlyBuild, parameterLabels = {"Filename"}, returnLabels = {"Stochastic BPMN"}, returnTypes = {StochasticBpmn.class})
@UIImportPlugin(description = "BPMN 2.0 files", extensions = {"bpmn", "xml"})
public class StochasticBpmnImportPlugin {
    public StochasticBpmnImportPlugin() {
    }

    public StochasticBpmn importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes) {
        throw new UnsupportedOperationException();
    }
}
