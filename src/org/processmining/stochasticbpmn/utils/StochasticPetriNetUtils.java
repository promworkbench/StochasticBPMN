package org.processmining.stochasticbpmn.utils;

import org.processmining.models.graphbased.directed.bpmn.elements.Activity;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.models.graphbased.directed.petrinet.*;
import org.processmining.models.graphbased.directed.petrinet.elements.*;
import org.processmining.models.graphbased.directed.petrinet.impl.StochasticNetImpl;
import org.processmining.models.semantics.petrinet.Marking;

import java.util.*;

public class StochasticPetriNetUtils {
    /**
     * Converting an arbitrary Petri net to a Petri net with a single source
     * place
     *
     * @param stochasticNet
     * @param marking
     */
    public static Object[] convertToPetrinetWithOneSourcePlace(StochasticNet stochasticNet, Marking marking) {
        Place initialPlace = stochasticNet.addPlace("");
        Transition initialTransition = stochasticNet
                .addTimedTransition("", 1, StochasticNet.DistributionType.UNIFORM, 0.0, 200.0);
        initialTransition.setInvisible(true);
        stochasticNet.addArc(initialPlace, initialTransition);
        for (Place place : marking.toList()) {
            stochasticNet.addArc(initialTransition, place);
        }
        return new Object[] {initialPlace, initialTransition};
    }


    /**
     * Handle transitions without incoming sequence flows
     *
     * @param petriNet
     */
    @SuppressWarnings("rawtypes")
    public static void handleTransitionsWithoutIncomingFlows(PetrinetGraph petriNet, Transition initialTransition) {

        // Handle transitions without incoming edges
        for (Transition transition : petriNet.getTransitions()) {
            if ((petriNet.getInEdges(transition) == null) || (petriNet.getInEdges(transition).size() == 0)) {
                Place newPlace = petriNet.addPlace("");
                petriNet.addArc(initialTransition, newPlace);
                petriNet.addArc(newPlace, transition);
                petriNet.addArc(transition, newPlace);
            }
        }
    }



    /**
     * Remove places without incoming sequence flows and corresponding output
     * transitions
     *
     * @param petriNet
     */
    public static void removeDeadPlaces(PetrinetGraph petriNet, Place initialPlace) {
        boolean hasDeadPlaces;
        Set<Place> toRemove = new HashSet<Place>();
        do {
            hasDeadPlaces = false;
            for (Place place : petriNet.getPlaces()) {
                if (place != initialPlace) {
                    if ((petriNet.getInEdges(place) == null) || (petriNet.getInEdges(place).isEmpty())) {
                        Collection<Transition> outTransitions = collectOutTransitions(place, petriNet);
                        for (Transition transition : outTransitions) {
                            petriNet.removeTransition(transition);
                        }
                        toRemove.add(place);
                        hasDeadPlaces = true;
                    }
                }
            }
            for(Place place : toRemove) {
                petriNet.removePlace(place);
            }
        } while (hasDeadPlaces);
    }


    public static Place retrieveSourcePlace(PetrinetGraph petrinetGraph) {
        for(Place place : petrinetGraph.getPlaces()) {
            if ((petrinetGraph.getInEdges(place) == null) || (petrinetGraph.getInEdges(place).size() == 0)) {
                return place;
            }
        }
        return null;
    }


    /**
     * Rebuilding conversion map to restore connections with the initial Petri
     * net
     *
     * @param conversionMap
     * @param transitionsMap
     * @return
     */
    public static Map<String, Activity> rebuildTransitionConversionMap(Map<String, Activity> conversionMap,
                                                                 Map<Transition, Transition> transitionsMap) {
        Map<String, Activity> newConversionMap = new HashMap<String, Activity>();
        for (Transition transition : transitionsMap.keySet()) {
            newConversionMap.put(transition.getId().toString(),
                    conversionMap.get(transitionsMap.get(transition).getId().toString()));
        }
        return newConversionMap;
    }

    /**
     * Rebuilding place conversion map to restore connections with the initial Petri
     * net
     *
     * @param conversionMap
     * @param placesMap
     * @return
     */
    public static Map<Place, Flow> rebuildPlaceConversionMap(Map<Place, Flow> conversionMap,
                                                       Map<Place, Place> placesMap) {
        Map<Place, Flow> newConversionMap = new HashMap<Place, Flow>();
        for (Place place : placesMap.keySet()) {
            newConversionMap.put(place, conversionMap.get(placesMap.get(place)));
        }
        return newConversionMap;
    }

    /**
     * Convert to resembling free-choice net
     *
     * @param petrinetGraph
     */
    public static void convertToResemblingFreeChoice(StochasticNet petrinetGraph) {
        // Set of common places which should be splited
        Set<Place> nonFreePlaces = new HashSet<Place>();
        //For each pair of transitions
        for (Transition t1 : petrinetGraph.getTransitions()) {
            for (Transition t2 : petrinetGraph.getTransitions()) {
                Set<Place> inPlaces1 = collectInPlaces(t1, petrinetGraph);
                Set<Place> inPlaces2 = collectInPlaces(t2, petrinetGraph);
                Set<Place> commonPlaces = new HashSet<Place>();
                boolean hasCommonPlace = false;
                boolean hasDiffPlaces = false;
                for (Place p1 : inPlaces1) {
                    for (Place p2 : inPlaces2) {
                        if (p1.equals(p2)) {
                            hasCommonPlace = true;
                            commonPlaces.add(p1);
                        } else {
                            hasDiffPlaces = true;
                        }
                    }
                }
                if (hasCommonPlace && hasDiffPlaces) {
                    nonFreePlaces.addAll(commonPlaces);
                }
            }
        }
        splitNonFreePlaces(petrinetGraph, nonFreePlaces);
    }

    /**
     * Split non-free places
     *
     * @param petrinetGraph
     * @param nonFreePlaces
     */
    public static void splitNonFreePlaces(StochasticNet petrinetGraph, Set<Place> nonFreePlaces) {
        for (Place place : nonFreePlaces) {
            for (PetrinetEdge<?, ?> outArc : petrinetGraph.getOutEdges(place)) {
                TimedTransition outTransition = (TimedTransition) outArc.getTarget();
                petrinetGraph.removeEdge(outArc);
                Place newPlace = petrinetGraph.addPlace("");
                TimedTransition newTransition = petrinetGraph.addTimedTransition("", outTransition.getWeight(),
                        outTransition.getDistributionType(), outTransition.getDistributionParameters());
                newTransition.setInvisible(true);
                petrinetGraph.addArc(newPlace, outTransition);
                petrinetGraph.addArc(newTransition, newPlace);
                petrinetGraph.addArc(place, newTransition);
            }
        }
    }


    /**
     * Delete reset arcs
     *
     * @param petrinetGraph
     * @return
     */
    public static Map<PetrinetNode, Set<PetrinetNode>> deleteResetArcs(PetrinetGraph petrinetGraph) {

        Map<PetrinetNode, Set<PetrinetNode>> deletedEdges = new HashMap<PetrinetNode, Set<PetrinetNode>>();
        for (Place place : petrinetGraph.getPlaces()) {
            Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> outEdges = petrinetGraph
                    .getOutEdges(place);
            for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : outEdges) {
                if (edge instanceof ResetArc) {
                    petrinetGraph.removeEdge(edge);
                    Set<PetrinetNode> targetNodes;
                    if (deletedEdges.get(edge) == null) {
                        targetNodes = new HashSet<PetrinetNode>();
                        deletedEdges.put(place, targetNodes);
                    } else {
                        targetNodes = deletedEdges.get(edge);
                    }
                    targetNodes.add(edge.getTarget());
                    deletedEdges.put(place, targetNodes);
                }
            }
        }

        return deletedEdges;
    }

    /**
     * Restore reset arcs
     *
     * @param petrinetGraph
     * @return
     */
    public static void restoreResetArcs(Map<PetrinetNode, Set<PetrinetNode>> resetArcs, PetrinetGraph petrinetGraph) {

        for (PetrinetNode place : resetArcs.keySet()) {
            Set<PetrinetNode> transitions = resetArcs.get(place);
            for (PetrinetNode transition : transitions) {
                if (petrinetGraph instanceof ResetInhibitorNet) {
                    ((ResetInhibitorNet) petrinetGraph).addResetArc((Place) place, (Transition) transition);
                }
                if (petrinetGraph instanceof ResetNet) {
                    System.out.println("Reset arc is added");
                    ((ResetNet) petrinetGraph).addResetArc((Place) place, (Transition) transition);
                }
            }
        }
    }


    /**
     * Collect out transitions for a place in the Petri net
     *
     * @param place
     * @param petrinetGraph
     * @return
     */
    public static Set<Transition> collectOutTransitions(Place place, PetrinetGraph petrinetGraph) {
        Set<Transition> outTransitions = new HashSet<Transition>();
        Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> outEdges = petrinetGraph
                .getOutEdges(place);
        for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> outEdge : outEdges) {
            if (!(outEdge instanceof ResetArc)) {
                outTransitions.add((Transition) outEdge.getTarget());
            }
        }
        return outTransitions;
    }

    /**
     * Collect in places for a transition in the Petri net
     *
     * @param transition
     * @param petrinetGraph
     * @return
     */
    public static Set<Place> collectInPlaces(Transition transition, PetrinetGraph petrinetGraph) {
        Set<Place> inPlaces = new HashSet<Place>();
        Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> inEdges = petrinetGraph
                .getInEdges(transition);
        for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> inEdge : inEdges) {
            inPlaces.add((Place) inEdge.getSource());
        }
        return inPlaces;
    }

    public static Object[] cloneToPetrinet(StochasticNet stochasticNet, Marking initialMarking, Marking finalMarking) {
        StochasticNet clonePetriNet = new StochasticNetImpl(stochasticNet.getLabel());
        Map<Transition, Transition> transitionsMap = new HashMap<Transition, Transition>();
        Map<Place, Place> placesMap = new HashMap<Place, Place>();
        Marking newInitialMarking = new Marking();
        Marking newFinalMarking = new Marking();

        for (Transition t : stochasticNet.getTransitions()) {
            TimedTransition transition = (TimedTransition) t;
            Transition newTransition = clonePetriNet.addTimedTransition(transition.getLabel(), transition.getWeight(),
                    transition.getDistributionType(), transition.getDistributionParameters());
            newTransition.setInvisible(transition.isInvisible());
            transitionsMap.put(transition, newTransition);
        }
        for (Place place : stochasticNet.getPlaces()) {
            placesMap.put(place, clonePetriNet.addPlace(place.getLabel()));
        }
        for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : stochasticNet.getEdges()) {
            if (edge instanceof Arc) {
                if ((edge.getSource() instanceof Place) && (edge.getTarget() instanceof Transition)) {
                    clonePetriNet.addArc(placesMap.get(edge.getSource()), transitionsMap.get(edge.getTarget()));
                }
                if ((edge.getSource() instanceof Transition) && (edge.getTarget() instanceof Place)) {
                    clonePetriNet.addArc(transitionsMap.get(edge.getSource()), placesMap.get(edge.getTarget()));
                }
            }
        }

        // Construct marking for the clone Petri net
        if (initialMarking != null) {
            for (Place markedPlace : initialMarking.toList()) {
                newInitialMarking.add(placesMap.get(markedPlace));
            }
        }

        if (finalMarking != null) {
            for (Place markedPlace : finalMarking.toList()) {
                newFinalMarking.add(placesMap.get(markedPlace));
            }
        }

        return new Object[] { clonePetriNet, transitionsMap, placesMap, newInitialMarking, newFinalMarking };
    }
}
