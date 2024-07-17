package org.processmining.newpackageivy.models.bpmn.stochastic;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.newpackageivy.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.newpackageivy.models.graphbased.directed.bpmn.stochastic.StochasticFlow;
import org.processmining.newpackageivy.models.graphbased.directed.bpmn.stochastic.StochasticGateway;
import org.processmining.plugins.bpmn.BpmnSequenceFlow;

import java.util.Collection;
import java.util.Map;

public class StochasticBpmnSequenceFlow extends BpmnSequenceFlow {
    public StochasticBpmnSequenceFlow() {
        super("sequenceFlow");
    }

    public Flow unmarshall(BPMNDiagram diagram, Map<String, BPMNNode> id2node) {
        final BPMNNode sourceNode = id2node.get(this.sourceRef);
        if (sourceNode instanceof StochasticGateway) {
            final StochasticBPMNDiagram sDiagram = (StochasticBPMNDiagram) diagram;
            final StochasticFlow flow = sDiagram.addStochasticFlow(sourceNode, id2node.get(this.targetRef), this.name);
            flow.getAttributeMap().put("Original id", this.id);
            flow.setConditionExpression(this.getConditionExpression());
            id2node.put(this.id, flow.getTarget());
            return flow;
        } else {
            return super.unmarshall(diagram, id2node);
        }
    }

    public Flow unmarshall(BPMNDiagram diagram, Collection<String> elements, Map<String, BPMNNode> id2node) {
        if (elements.contains(this.sourceRef) && elements.contains(this.targetRef)) {
            if (id2node.containsKey(this.sourceRef) && id2node.containsKey(this.targetRef)) {
                return this.unmarshall(diagram, id2node);
            }

            if (!id2node.containsKey(this.sourceRef)) {
                System.out.println("org.processmining.plugins.bpmn.BpmnSequenceFlow.unmarshall(BPMNDiagram, Collection<String>, Map<String, BPMNNode>)(BPMNDiagram, Collection<String>, Map<String, BPMNNode>) element with sourceRef=" + this.sourceRef + " not existing in id2node");
            }

            if (!id2node.containsKey(this.targetRef)) {
                System.out.println("org.processmining.plugins.bpmn.BpmnSequenceFlow.unmarshall(BPMNDiagram, Collection<String>, Map<String, BPMNNode>)(BPMNDiagram, Collection<String>, Map<String, BPMNNode>) element with targetRef=" + this.targetRef + " not existing in id2node");
            }
        }

        return null;
    }
}
