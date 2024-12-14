package org.processmining.stochasticbpmn.utils;

import org.processmining.models.graphbased.directed.DirectedGraphNode;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Activity;
import org.processmining.models.graphbased.directed.bpmn.elements.Event;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.plugins.graphalgorithms.DFS;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticFlow;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGateway;

import java.util.*;

public class StochasticBPMNUtils {

    public static void removeSilentActivities(Map<String, Activity> conversionMap,
                                              StochasticBPMNDiagram diagram) {
        Collection<Activity> allActivities = new HashSet();
        allActivities.addAll(diagram.getActivities());

        for(Activity activity : allActivities) {
            if ("Empty".equals(activity.getLabel())) {
                Collection<BPMNEdge<? extends BPMNNode, ? extends BPMNNode>> inEdges = diagram.getInEdges(activity);
                Collection<BPMNEdge<? extends BPMNNode, ? extends BPMNNode>> outEdges = diagram.getOutEdges(activity);
                if (inEdges.iterator().hasNext() && outEdges.iterator().hasNext()) {
                    BPMNEdge<?, ?> inEdge = inEdges.iterator().next();
                    BPMNNode source = inEdge.getSource();
                    BPMNNode target = (BPMNNode)((BPMNEdge)outEdges.iterator().next()).getTarget();
                    if (source instanceof StochasticGateway) {
                        StochasticGateway xorSplit = (StochasticGateway) source;
                        String uuid = UUID.randomUUID().toString();
                        StochasticFlow f = diagram.addStochasticFlow(xorSplit, target, (String)null);
                        f.getAttributeMap().put("Original id", uuid);
                        xorSplit.getWeightedFlow().assignFlowWeight(xorSplit.getWeight(inEdge), uuid);
                        f.setLabel(f.getLabel());
                        // remove weighted flow
                        xorSplit.getWeightedFlow().remove((String) inEdge.getAttributeMap().get("Original id"));
                    } else {
                        addFlow(diagram, source, target);
                    }
                }

                diagram.removeActivity(activity);
                if (conversionMap != null) {
                    Set<String> idToRemove = new HashSet();

                    for(String id : conversionMap.keySet()) {
                        if (activity == conversionMap.get(id)) {
                            idToRemove.add(id);
                        }
                    }

                    for(String id : idToRemove) {
                        conversionMap.remove(id);
                    }
                }
            }
        }

    }

    private static Flow addFlow(BPMNDiagram diagram, BPMNNode source, BPMNNode target) {
        for(BPMNEdge flow : diagram.getEdges()) {
            if (source == flow.getSource() && target == flow.getTarget()) {
                return null;
            }
        }

        return diagram.addFlow(source, target, "");
    }


    /**
     * Retrieve end event for the BPMN Diagram
     *
     * @param diagram
     * @return
     */
    public static Event retrieveEndEvent(BPMNDiagram diagram) {
        Event endEvent = null;
        for (Event event : diagram.getEvents()) {
            if (event.getEventType().equals(Event.EventType.END)) {
                endEvent = event;
            }
        }

        if(endEvent == null) {
            endEvent = diagram.addEvent("", Event.EventType.END, Event.EventTrigger.NONE, Event.EventUse.THROW, true, null);
        }
        return endEvent;
    }

    /**
     * Retrieve start event for the BPMN Diagram
     *
     * @param diagram
     * @return
     */
    public static Event retrieveStartEvent(BPMNDiagram diagram) {
        Event startEvent = null;
        for (Event event : diagram.getEvents()) {
            if (event.getEventType().equals(Event.EventType.START)) {
                startEvent = event;
            }
        }

        return startEvent;
    }


    public static void handleActivitiesWithoutOutgoingFlows(BPMNDiagram bpmnDiagram) {
        // Handle activities without paths to the end event
        Event startEvent = StochasticBPMNUtils.retrieveStartEvent(bpmnDiagram);
        Event endEvent = StochasticBPMNUtils.retrieveEndEvent(bpmnDiagram);
        DFS dfs = new DFS(bpmnDiagram, startEvent);

        Set<Activity> acivitiesWithoutPathToEndEvent = findActivitiesWithoutPathToEndEvent(bpmnDiagram, dfs);
        Set<Activity> currentActivities = new HashSet<Activity>();
        currentActivities.addAll(acivitiesWithoutPathToEndEvent);

        for (Activity activity1 : acivitiesWithoutPathToEndEvent) {
            for (Activity activity2 : acivitiesWithoutPathToEndEvent) {
                if (dfs.findDescendants(activity2).contains(activity1) && !activity1.equals(activity2)) {
                    if (currentActivities.contains(activity1)) {
                        currentActivities.remove(activity2);
                    }
                }
            }
        }

        for (Activity activity : currentActivities) {
            bpmnDiagram.addFlow(activity, endEvent, "");
        }
    }

    /**
     * Find activities without a path to end event
     *
     * @param bpmnDiagram
     * @return
     */
    public static Set<Activity> findActivitiesWithoutPathToEndEvent(BPMNDiagram bpmnDiagram, DFS dfs) {

        Set<Activity> resultSet = new HashSet<Activity>();
        Event endEvent = retrieveEndEvent(bpmnDiagram);
        // Find activities without paths to end event
        for (Activity activity : bpmnDiagram.getActivities()) {
            Set<DirectedGraphNode> descendants = dfs.findDescendants(activity);

            boolean hasPathToEndEvent = false;
            for(DirectedGraphNode descendant : descendants) {
                if (descendant.equals(endEvent)) {
                    hasPathToEndEvent = true;
                }
            }
            if(!hasPathToEndEvent) {
                resultSet.add(activity);
            }
        }
        return resultSet;
    }
}
