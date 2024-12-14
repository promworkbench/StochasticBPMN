package org.processmining.stochasticbpmn.algorithms.converter;

import org.processmining.models.graphbased.directed.bpmn.elements.Activity;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.models.graphbased.directed.petrinet.StochasticNet;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.petrinets.stochastic.StochasticAPN;
import org.processmining.stochasticbpmn.utils.StochasticBPMNUtils;
import org.processmining.stochasticbpmn.utils.StochasticPetriNetUtils;

import java.util.Map;

/**
 * Converts a StochasticAPN to StochasticBPMN but fire preprocesses the StochasticAPN and also post-processess the
 * resulting StochasticBPMN.
 */
public class StochasticAPNToStochasticBPMNEnhancedConverter implements StochasticAPNToStochasticBPMNConverter {
    @Override
    public StochasticBPMNDiagram convert(StochasticAPN stochasticAPN) {
        // Clone to Petri net with marking
        Object[] cloneResult = StochasticPetriNetUtils.cloneToPetrinet(
                stochasticAPN.getStochasticNet(),
                stochasticAPN.getInitialMarking(),
                stochasticAPN.getFinalMarking());
        StochasticNet clonePetrinet = (StochasticNet) cloneResult[0];
        Map<Transition, Transition> transitionsMap = (Map<Transition, Transition>) cloneResult[1];
        Map<Place, Place> placesMap = (Map<Place, Place>) cloneResult[2];
        Marking cloneInitialMarking = (Marking) cloneResult[3];
        Marking cloneFinalMarking = (Marking) cloneResult[4];

        StochasticPetriNetUtils.convertToResemblingFreeChoice(clonePetrinet);

        // Convert to a Petri net with one source place if needed
        Object[] res = StochasticPetriNetUtils
                .convertToPetrinetWithOneSourcePlace(clonePetrinet, cloneInitialMarking);
        Place initialPlace = (Place) res[0];
        Transition initialTransition = (Transition) res[1];

        // Handle transitions without incoming sequence flows
        StochasticPetriNetUtils.handleTransitionsWithoutIncomingFlows(clonePetrinet, initialTransition);

        // Remove places without incoming sequence flows
        StochasticPetriNetUtils.removeDeadPlaces(clonePetrinet, initialPlace);

        // Convert Petri net to a BPMN diagram
        StochasticPetriNetToStochasticBPMNDirectConverter converter = new StochasticPetriNetToStochasticBPMNDirectConverter(clonePetrinet, initialPlace, cloneFinalMarking);
        StochasticBPMNDiagram bpmnDiagram = converter.convert();
        Map<String, Activity> transitionConversionMap = converter.getTransitionConversionMap();
        Map<Place, Flow> placeConversionMap = converter.getPlaceConversionMap();

        // Simplify BPMN diagram
//        BPMNUtils.simplifyBPMNDiagram(transitionConversionMap, bpmnDiagram);
        StochasticBPMNUtils.removeSilentActivities(transitionConversionMap, bpmnDiagram);

        // Handle activities without outgoing sequence flows
        if ((cloneFinalMarking != null) && (!cloneFinalMarking.isEmpty())) {
            StochasticBPMNUtils.handleActivitiesWithoutOutgoingFlows(bpmnDiagram);
        }

        //Add end event
//        addEndEvent(bpmnDiagram);

//        // Rebuild conversion maps to restore connections with the initial Petri net
//        transitionConversionMap = StochasticPetriNetUtils.rebuildTransitionConversionMap(transitionConversionMap,
//                transitionsMap);
//        placeConversionMap = StochasticPetriNetUtils.rebuildPlaceConversionMap(placeConversionMap, placesMap);

        return bpmnDiagram;
//        return new Object[] { bpmnDiagram, transitionConversionMap, placeConversionMap };
    }
}
