package org.processmining.newpackageivy.plugins;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.*;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;
import org.processmining.newpackageivy.algorithms.YourAlgorithm;
import org.processmining.newpackageivy.help.YourHelp;
import org.processmining.newpackageivy.models.bpmn.stochastic.StochasticBpmn;
import org.processmining.newpackageivy.models.bpmn.stochastic.diagram.StochasticBPMNDiagram;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.parameters.BpmnSelectDiagramParameters;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Plugin(name = "Select Stochastic BPMN Diagram", parameterLabels = {"Stochastic BPMN"}, returnLabels = {"Stochastic BPMN Diagram"}, returnTypes = {StochasticBPMNDiagram.class}, help = YourHelp.TEXT, level = PluginLevel.NightlyBuild, keywords = {"Stochastic BPMN Diagram", "Diagram", "Stochastic", "BPMN"}, quality = PluginQuality.VeryPoor, categories = PluginCategory.Enhancement)
public class StochasticBpmnSelectDiagramPlugin {

    /**
     * The plug-in variant that runs in any context and requires a parameters.
     *
     * @param context The context to run in.
     * @param sBpmn   a StochasticBpmn object.
     * @return The output.
     */
    @UITopiaVariant(
            affiliation = "RWTH Aachen University",
            author = "Aleksandar Kuzmanoski, Sandhya Velagapudi",
            email = "aleksandar.kuzmanoski@rwth-aachen.de, sandhya.velagapudi@rwth-aachen.de",
            pack = "StochasticBPMN",
            uiLabel = "Stochastic BPMN, select default diagram",
            uiHelp = "It selects the first encountered diagram in the bpmn xml schema and produces StochasticBPMNDiagram class.")
    @PluginVariant(
            variantLabel = "Generate Stochastic BPMN Diagram from Stochastic BPMN",
            requiredParameterLabels = {0},
            help = "It selects the first encountered diagram in the bpmn xml schema and produces Stochastic BPMN Diagram class.")
    public StochasticBPMNDiagram selectDefault(PluginContext context, StochasticBpmn sBpmn) {
        // Apply the algorithm depending on whether a connection already exists.
        BpmnSelectDiagramParameters parameters = new BpmnSelectDiagramParameters();
        if (!sBpmn.getDiagrams().isEmpty()) {
            parameters.setDiagram(sBpmn.getDiagrams().iterator().next());
        } else {
            parameters.setDiagram(BpmnSelectDiagramParameters.NODIAGRAM);
        }

        return this.selectParameters(context, sBpmn, parameters);
    }

    public StochasticBPMNDiagram selectParameters(PluginContext context, Bpmn bpmn, BpmnSelectDiagramParameters parameters) {
        StochasticBPMNDiagram newDiagram = new StochasticBPMNDiagram("");
        Map<String, BPMNNode> id2node = new HashMap<>();
        Map<String, Swimlane> id2lane = new HashMap<>();
        if (parameters.getDiagram() == BpmnSelectDiagramParameters.NODIAGRAM) {
            bpmn.unmarshall(newDiagram, id2node, id2lane);
        } else {
            Collection<String> elements = parameters.getDiagram().getElements();
            bpmn.unmarshall(newDiagram, elements, id2node, id2lane);
        }

        return newDiagram;
    }
}
