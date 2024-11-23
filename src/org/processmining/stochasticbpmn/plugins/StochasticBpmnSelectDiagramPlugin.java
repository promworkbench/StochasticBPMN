package org.processmining.stochasticbpmn.plugins;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.*;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.StochasticBPMNDiagramBuilder;
import org.processmining.stochasticbpmn.algorithms.diagram.builder.StochasticBPMNDiagramBuilderImpl;
import org.processmining.stochasticbpmn.help.YourHelp;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

@Plugin(name = "Select Stochastic BPMN Diagram", parameterLabels = {"Stochastic BPMN"}, returnLabels = {"Stochastic BPMN Diagram"}, returnTypes = {StochasticBPMNDiagram.class}, help = YourHelp.TEXT, level = PluginLevel.NightlyBuild, keywords = {"Stochastic BPMN Diagram", "Diagram", "Stochastic", "BPMN"}, quality = PluginQuality.VeryPoor, categories = PluginCategory.Enhancement)
public class StochasticBpmnSelectDiagramPlugin {
    private final StochasticBPMNDiagramBuilder stochasticBPMNDiagramBuilder;

    public StochasticBpmnSelectDiagramPlugin() {
        this.stochasticBPMNDiagramBuilder = new StochasticBPMNDiagramBuilderImpl();
    }

    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context The context to run in.
     * @param sBpmn   a StochasticBpmn object.
     * @return The output.
     */
    @UITopiaVariant(affiliation = "RWTH Aachen University", author = "Aleksandar Kuzmanoski, Sandhya Velagapudi", email = "aleksandar.kuzmanoski@rwth-aachen.de, sandhya.velagapudi@rwth-aachen.de", pack = "StochasticBPMN", uiLabel = "Stochastic BPMN, select default diagram", uiHelp = "It selects the first encountered diagram in the bpmn xml schema and produces StochasticBPMNDiagram class.")
    @PluginVariant(variantLabel = "Generate Stochastic BPMN Diagram from Stochastic BPMN", requiredParameterLabels = {0}, help = "It selects the first encountered diagram in the bpmn xml schema and produces Stochastic BPMN Diagram class.")
    public StochasticBPMNDiagram selectDefault(PluginContext context, StochasticBpmn sBpmn) {
        StochasticBPMNDiagram diagram = stochasticBPMNDiagramBuilder.build(sBpmn);
        return diagram;
    }
}
