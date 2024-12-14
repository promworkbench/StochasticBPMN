package org.processmining.stochasticbpmn.plugins;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.Progress;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.connections.petrinets.behavioral.FinalMarkingConnection;
import org.processmining.models.connections.petrinets.behavioral.InitialMarkingConnection;
import org.processmining.models.connections.petrinets.structural.FreeChoiceInfoConnection;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.StochasticNet;
import org.processmining.models.graphbased.directed.petrinet.analysis.NetAnalysisInformation;
import org.processmining.models.graphbased.directed.petrinet.analysis.NetAnalysisInformation.UnDetBool;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.stochasticbpmn.algorithms.converter.StochasticAPNToStochasticBPMNConverter;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.petrinets.stochastic.StochasticAPN;
import org.processmining.stochasticbpmn.utils.StochasticPetriNetUtils;

@Plugin(name = "Convert Stochastic Petri net to Stochastic BPMN model", level = PluginLevel.NightlyBuild,
        parameterLabels = {"Stochastic Petri Net"}, returnLabels = {"BPMN Diagram " /*, "Transition Conversion map",
        "Place Conversion Map"*/ }, returnTypes = { StochasticBPMNDiagram.class /*{ StochasticBPMNDiagram.class, Map
        .class,
        Map.class */})
public class StochasticPetriNetToStochasticBpmnConverterPlugin {

    private Place initialPlace;
    private Transition initialTransition;

    @SuppressWarnings("unchecked")
    @UITopiaVariant(affiliation = "RWTH Aacehn", author = "Aleksandar Kuzmanoski", email = "aleksandar" +
            ".kuzmanoski@rwth-aachen.de")
    @PluginVariant(variantLabel = "Convert Stochastic Petri net to Stochastic BPMN", requiredParameterLabels = { 0 })
    public StochasticBPMNDiagram convert(PluginContext context, StochasticNet stochasticNet) {

        Progress progress = context.getProgress();
        progress.setCaption("Converting Petri net To BPMN diagram");

        Marking initialMarking = retrieveInitialMarking(context, stochasticNet);

        Marking finalMarking = retrieveFinalMarking(context, stochasticNet);

        return StochasticAPNToStochasticBPMNConverter.getDefault().convert(
                new StochasticAPN(stochasticNet, initialMarking, finalMarking));
//        return convert(stochasticNet, initialMarking, finalMarking);
    }


    /**
     * Retrieve initial marking for a Petri net graph
     *
     * @param context
     * @param petrinetGraph
     * @return
     */
    private Marking retrieveInitialMarking(PluginContext context, PetrinetGraph petrinetGraph) {
        Marking marking = new Marking();
        try {
            InitialMarkingConnection initialMarkingConnection = context.getConnectionManager().getFirstConnection(
                    InitialMarkingConnection.class, context, petrinetGraph);
            marking = initialMarkingConnection.getObjectWithRole(InitialMarkingConnection.MARKING);
            if ((marking != null) && (marking.size() == 0)) {
                Place sourcePlace = StochasticPetriNetUtils.retrieveSourcePlace(petrinetGraph);
                if (sourcePlace != null) {
                    marking.add(sourcePlace);
                }
            }
        } catch (ConnectionCannotBeObtained e) {
            Place sourcePlace = StochasticPetriNetUtils.retrieveSourcePlace(petrinetGraph);
            marking.add(sourcePlace);
        }
        return marking;
    }

    /**
     * Retrieve final marking for a Petri net graph
     *
     * @param context
     * @param petrinetGraph
     * @return
     */
    private Marking retrieveFinalMarking(PluginContext context, PetrinetGraph petrinetGraph) {
        Marking marking = new Marking();
        try {
            FinalMarkingConnection finalMarkingConnection = context.getConnectionManager().getFirstConnection(
                    FinalMarkingConnection.class, context, petrinetGraph);
            marking = finalMarkingConnection.getObjectWithRole(FinalMarkingConnection.MARKING);
        } catch (ConnectionCannotBeObtained e) {
            return null;
        }
        return marking;
    }

    /**
     * Check whether Petri net is a Free-Choice
     *
     * @param context
     * @param petrinetGraph
     * @return
     */
    private boolean petriNetIsFreeChoice(PluginContext context, PetrinetGraph petrinetGraph) {
        NetAnalysisInformation.FREECHOICE fCRes = null;
        try {
            fCRes = context.tryToFindOrConstructFirstObject(NetAnalysisInformation.FREECHOICE.class,
                    FreeChoiceInfoConnection.class, "Free Choice information of " + petrinetGraph.getLabel(),
                    petrinetGraph);
        } catch (ConnectionCannotBeObtained e) {
            context.log("Can't obtain connection for " + petrinetGraph.getLabel());
            e.printStackTrace();
        }
        return fCRes.getValue().equals(UnDetBool.TRUE);
    }


}
