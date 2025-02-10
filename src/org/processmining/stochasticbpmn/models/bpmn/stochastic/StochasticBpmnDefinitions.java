package org.processmining.stochasticbpmn.models.bpmn.stochastic;

import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.DefaultGraphCell;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.DirectedGraphNode;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.elements.*;
import org.processmining.models.jgraph.ProMGraphModel;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.models.jgraph.elements.ProMGraphCell;
import org.processmining.models.jgraph.elements.ProMGraphEdge;
import org.processmining.models.jgraph.elements.ProMGraphPort;
import org.processmining.models.jgraph.views.JGraphPortView;
import org.processmining.models.jgraph.visualization.ProMJGraphPanel;
import org.processmining.plugins.bpmn.*;
import org.processmining.plugins.bpmn.diagram.*;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class StochasticBpmnDefinitions extends BpmnDefinitions {

    public StochasticBpmnDefinitions(String tag, StochasticBpmnDefinitionsBuilder builder) {
        super(tag);
        resources = builder.resources;
        processes = builder.processes;
        collaborations = builder.collaborations;
        messages = new HashSet();
        diagrams = builder.diagrams;
    }


    public static class StochasticBpmnDefinitionsBuilder {
        protected Collection<BpmnResource> resources;
        protected Collection<BpmnProcess> processes;
        protected Collection<BpmnCollaboration> collaborations;
        protected Collection<BpmnDiagram> diagrams;

        public StochasticBpmnDefinitionsBuilder(PluginContext context, StochasticBPMNDiagram diagram) {
            resources = new HashSet<BpmnResource>();
            processes = new HashSet<BpmnProcess>();
            collaborations = new HashSet<BpmnCollaboration>();
            diagrams = new HashSet<BpmnDiagram>();
            
            buildFromDiagram(context, diagram);
        }

        private void buildFromDiagram(PluginContext context, StochasticBPMNDiagram diagram) {
            BpmnCollaboration bpmnCollaboration = new BpmnCollaboration("collaboration");
            bpmnCollaboration.setId("col_" + bpmnCollaboration.hashCode());
            
            for (Swimlane pool : diagram.getPools()) {
                BpmnParticipant bpmnParticipant = new BpmnParticipant("participant");
                bpmnParticipant.id = pool.getId().toString().replace(' ', '_');
                bpmnParticipant.name = pool.getLabel();
                if (!pool.getChildren().isEmpty()) {
                    StochasticBpmnProcess sbpmnProcess = new StochasticBpmnProcess();
                    sbpmnProcess.marshall(diagram, pool);
                    sbpmnProcess.setId("proc_" + sbpmnProcess.hashCode());
                    processes.add(sbpmnProcess);
                    bpmnParticipant.setProcessRef(sbpmnProcess.getId());
                }
                bpmnCollaboration.addParticipant(bpmnParticipant);
            }

            // Discover "internal" process
            StochasticBpmnProcess intBpmnProcess = new StochasticBpmnProcess();
            intBpmnProcess.setId("proc_" + intBpmnProcess.hashCode());
            // If there are elements without parent pool, add process
            if (intBpmnProcess.marshall(diagram, null)) {
                processes.add(intBpmnProcess);
            }

            for (MessageFlow messageFlow : diagram.getMessageFlows()) {
                BpmnMessageFlow bpmnMessageFlow = new BpmnMessageFlow("messageFlow");
                bpmnMessageFlow.marshall(messageFlow);
                bpmnCollaboration.addMessageFlow(bpmnMessageFlow);
            }

            for (TextAnnotation textAnnotation : diagram.getTextAnnotations(null)) {
                BpmnTextAnnotation bpmnTextAnnotation = new BpmnTextAnnotation("textAnnotation");
                bpmnTextAnnotation.marshall(textAnnotation);
                bpmnCollaboration.addTextAnnotation(bpmnTextAnnotation);;
            }

            for (Association association : diagram.getAssociations(null)) {
                BpmnAssociation bpmnAssociation = new BpmnAssociation("association");
                bpmnAssociation.marshall(association);
                bpmnCollaboration.addAssociation(bpmnAssociation);
            }

            for(Swimlane swimlane : diagram.getSwimlanes()) {
                if(swimlane.getPartitionElement() != null) {
                    BpmnResource resource = new BpmnResource("resource");
                    resource.marshall(swimlane);
                    resources.add(resource);
                }
            }

            BpmnDiagram bpmnDiagram = new BpmnDiagram("bpmndi:BPMNDiagram");
            bpmnDiagram.setId("id_" + diagram.hashCode());
            BpmnDiPlane plane = new BpmnDiPlane("bpmndi:BPMNPlane");
            if (diagram.getPools().size() > 0) {
                collaborations.add(bpmnCollaboration);
                plane.setBpmnElement(bpmnCollaboration.id);
            } else {
                plane.setBpmnElement(intBpmnProcess.id);
            }

            bpmnDiagram.addPlane(plane);
            if (context != null) {
                fillGraphicsInfo(context, diagram, bpmnDiagram, plane);
            }

            diagrams.add(bpmnDiagram);
        }

        private static synchronized void fillGraphicsInfo(PluginContext context, BPMNDiagram diagram, BpmnDiagram bpmnDiagram, BpmnDiPlane plane) {
            ProMJGraphPanel graphPanel = ProMJGraphVisualizer.instance().visualizeGraph(context, diagram);
            ProMGraphModel graphModel = graphPanel.getGraph().getModel();

            for (Object o : graphModel.getRoots()) {
                if (o instanceof ProMGraphCell) {
                    ProMGraphCell graphCell = (ProMGraphCell) o;
                    addCellGraphicsInfo(graphCell, plane);
                }
                if (o instanceof ProMGraphPort) {
                    ProMGraphPort graphPort = (ProMGraphPort) o;
                    if(graphPort.getBoundingNode() != null) {
                        addCellGraphicsInfo(graphPort, plane);
                    }
                }
                if (o instanceof ProMGraphEdge) {
                    ProMGraphEdge graphEdge = (ProMGraphEdge) o;
                    addEdgeGraphInfo(graphEdge, plane);
                }
            }
        }

        private static void addCellGraphicsInfo(DefaultGraphCell graphCell, BpmnDiPlane plane) {
            DirectedGraphNode graphNode = null;
            if (graphCell instanceof ProMGraphCell) {
                graphNode = ((ProMGraphCell)graphCell).getNode();
            } else if (graphCell instanceof ProMGraphPort) {
                graphNode = ((ProMGraphPort)graphCell).getBoundingNode();
            }

            String bpmnElement = ((DirectedGraphNode)graphNode).getId().toString().replace(' ', '_');
            boolean isExpanded = false;
            boolean isHorizontal = false;
            if (graphNode instanceof SubProcess) {
                SubProcess subProcess = (SubProcess)graphNode;
                if (!subProcess.isBCollapsed()) {
                    isExpanded = true;
                }
            }

            if (graphNode instanceof Swimlane) {
                isExpanded = true;
                isHorizontal = true;
            }

            AbstractCellView view = null;
            if (graphCell instanceof ProMGraphCell) {
                view = ((ProMGraphCell)graphCell).getView();
            } else if (graphCell instanceof ProMGraphPort) {
                view = ((ProMGraphPort)graphCell).getView();
            }
            Rectangle2D rectangle = view.getBounds();
            
            double x = rectangle.getX();
            double y = rectangle.getY();
            double width = rectangle.getWidth();
            double height = rectangle.getHeight();
            
            BpmnDcBounds bounds = new BpmnDcBounds("dc:Bounds", x, y, width, height);
            BpmnDiShape shape = new BpmnDiShape("bpmndi:BPMNShape", bpmnElement, bounds, isExpanded, isHorizontal);
            plane.addShape(shape);
            addChildGrapInfo(graphCell, plane);
        }

        private static void addEdgeGraphInfo(ProMGraphEdge graphEdge, BpmnDiPlane plane) {
            BPMNEdge bpmnEdge = (BPMNEdge)graphEdge.getEdge();
            String bpmnElement = bpmnEdge.getEdgeID().toString().replace(' ', '_');
            
            BpmnDiEdge edge = new BpmnDiEdge("bpmndi:BPMNEdge", bpmnElement);
            for (Object point : graphEdge.getView().getPoints()) {
                Point2D point2D;
                if(point instanceof JGraphPortView) {
                    JGraphPortView portView = (JGraphPortView) point;
                    point2D = portView.getLocation();
                } else if(point instanceof Point2D) {
                    point2D = (Point2D)point;
                } else {
                    continue;
                }
                double x = point2D.getX();
                double y = point2D.getY();
                BpmnDiWaypoint waypoint = new BpmnDiWaypoint("di:waypoint", x, y);
                edge.addWaypoint(waypoint);
            }
            plane.addEdge(edge);
        }

        private static void addChildGrapInfo(DefaultGraphCell graphCell, BpmnDiPlane plane) {
            for (Object o : graphCell.getChildren()) {
                if (o instanceof ProMGraphCell) {
                    ProMGraphCell childGraphCell = (ProMGraphCell) o;
                    addCellGraphicsInfo(childGraphCell, plane);
                }
                if (o instanceof ProMGraphPort) {
                    ProMGraphPort childGraphPort = (ProMGraphPort) o;
                    if(childGraphPort.getBoundingNode() != null) {
                        addCellGraphicsInfo(childGraphPort, plane);
                    }
                }
                if (o instanceof ProMGraphEdge) {
                    ProMGraphEdge childGraphEdge = (ProMGraphEdge) o;
                    addEdgeGraphInfo(childGraphEdge, plane);
                }
            }
        }
    }
}
