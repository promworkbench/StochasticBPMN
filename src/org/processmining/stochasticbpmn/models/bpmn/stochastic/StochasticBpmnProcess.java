package org.processmining.stochasticbpmn.models.bpmn.stochastic;

import org.processmining.models.graphbased.directed.ContainingDirectedGraphNode;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.models.graphbased.directed.bpmn.elements.SubProcess;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;
import org.processmining.plugins.bpmn.*;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGateway;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class StochasticBpmnProcess extends BpmnProcess {
    public StochasticBpmnProcess() {
        super("process");
    }

    protected boolean importElements(XmlPullParser xpp, Bpmn bpmn) {
        if (xpp.getName().equals("exclusiveGateway")) {
            BpmnExclusiveGateway exclusiveGateway = new StochasticBpmnExclusiveGateway();
            exclusiveGateway.importElement(xpp, bpmn);
            try {
                Field field = BpmnProcess.class.getDeclaredField("exclusiveGateways");
                field.setAccessible(true);
                Collection<BpmnExclusiveGateway> exclusiveGateways = (Collection<BpmnExclusiveGateway>) field.get(this);
                exclusiveGateways.add(exclusiveGateway);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                bpmn.log(this.tag, xpp.getLineNumber(), e.getMessage());
            }
            return true;
        } else if (xpp.getName().equals("subProcess")) {
            BpmnSubProcess subPro = new StochasticBpmnSubProcess();
            subPro.importElement(xpp, bpmn);
            try {
                Field field = BpmnProcess.class.getDeclaredField("subProcesses");
                field.setAccessible(true);
                Collection<BpmnSubProcess> subProcesses = (Collection<BpmnSubProcess>) field.get(this);
                subProcesses.add(subPro);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                bpmn.log(this.tag, xpp.getLineNumber(), e.getMessage());
            }
            return true;
        } else if (xpp.getName().equals("sequenceFlow")) {
            final BpmnSequenceFlow sequenceFlow = new StochasticBpmnSequenceFlow();
            sequenceFlow.importElement(xpp, bpmn);
            try {
                Field field = BpmnProcess.class.getDeclaredField("sequenceFlows");
                field.setAccessible(true);
                Collection<BpmnSequenceFlow> sequenceFlows = (Collection<BpmnSequenceFlow>) field.get(this);
                sequenceFlows.add(sequenceFlow);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                bpmn.log(this.tag, xpp.getLineNumber(), e.getMessage());
            }
            return true;
        } else return super.importElements(xpp, bpmn);
    }

    protected String exportElements() {
        StringBuilder s = new StringBuilder();
        if(getLaneSet() != null) {
            s.append(getLaneSet().exportElement());
        }
        for (BpmnStartEvent startEvent : getStartEvents()) {
            s.append(startEvent.exportElement());
        }
        for (BpmnEndEvent endEvent : getEndEvents()) {
            s.append(endEvent.exportElement());
        }
        for (BpmnIntermediateEvent intermediateEvent : getIntermediateEvents()) {
            s.append(intermediateEvent.exportElement());
        }
        for (BpmnTask task : getTasks()) {
            s.append(task.exportElement());
        }
        for (BpmnExclusiveGateway exclusiveGateway : getExclusiveGateways()) {
            s.append(exclusiveGateway.exportElement());
        }
        for (BpmnParallelGateway parallelGateway : getParallelGateways()) {
            s.append(parallelGateway.exportElement());
        }
        for (BpmnInclusiveGateway inclusiveGateway : getInclusiveGateways()) {
            s.append(inclusiveGateway.exportElement());
        }
        for (BpmnSubProcess subPro : getSubProcesses()) {
            s.append(subPro.exportElement());
        }
        for (BpmnSequenceFlow sequenceFlow : getSequenceFlows()) {
            s.append(sequenceFlow.exportElement());
        }
        for (BpmnDataObject dataObject : getDataObjects()) {
            s.append(dataObject.exportElement());
        }
        for (BpmnDataObjectReference dataObjectRef : getDataObjectReferences()) {
            s.append(dataObjectRef.exportElement());
        }
        for (BpmnTextAnnotation textAnnotation : getTextAnnotations()) {
            s.append(textAnnotation.exportElement());
        }
        for (BpmnAssociation association : getAssociations()) {
            s.append(association.exportElement());
        }
        for (BpmnEventBasedGateway eventBasedGateway: getEventBasedGateways()) {
            s.append(eventBasedGateway.exportElement());
        }
        for(BpmnCallActivity callActivity:getCallActivities()){
            s.append(callActivity.exportElement());
        }
        return s.toString();
    }

    public boolean marshall(StochasticBPMNDiagram diagram, Swimlane pool) {
        this.callPrivateMethod("clearAll");

        this.callPrivateMethod("marshallEvents", diagram, pool);
        this.callPrivateMethod("marshallActivities", diagram, pool);
        this.callPrivateMethod("marshallCallActivities", diagram, pool);
        this.marshallStochasticGateways(diagram, pool);
        this.callPrivateMethod("marshallDataObjects", diagram, pool);
        this.callPrivateMethod("marshallSubProcesses", diagram, pool);
        this.callPrivateMethod("marshallControlFlows", diagram, pool);
        this.callPrivateMethod_LaneSet("marshallLaneSet", diagram, pool);
        this.callPrivateMethod("marshallArtifacts", diagram, pool);
        this.callPrivateMethod("marshallAssociations", diagram, pool);

        return !(getStartEvents().isEmpty() && getEndEvents().isEmpty() && getTasks().isEmpty()
                && getExclusiveGateways().isEmpty() && getParallelGateways().isEmpty()
                && getTextAnnotations().isEmpty() && getAssociations().isEmpty() && (getLaneSet() == null));
    }

    private void marshallStochasticGateways(StochasticBPMNDiagram diagram, Swimlane pool) {
        for (Gateway gateway : diagram.getGateways(pool)) {
            if (gateway.getAncestorSubProcess() == null) {
                if (gateway.getGatewayType() == Gateway.GatewayType.DATABASED) {
                    if (diagram.getOutEdges(gateway).size() == 1) {
                        BpmnExclusiveGateway exclusiveGateway = new BpmnExclusiveGateway("exclusiveGateway");
                        exclusiveGateway.marshall(diagram, gateway);
                        this.getExclusiveGateways().add(exclusiveGateway);
                    } else {
                        StochasticBpmnExclusiveGateway exclusiveGateway = new StochasticBpmnExclusiveGateway();
                        if(!(gateway instanceof StochasticGateway)){
                            throw new RuntimeException("Required: Stochastic Gateway. Got Gateway " + gateway.getId());
                        }
                        exclusiveGateway.marshall(diagram, (StochasticGateway) gateway);
                        this.getExclusiveGateways().add(exclusiveGateway);
                    }
                } else if (gateway.getGatewayType() == Gateway.GatewayType.PARALLEL) {
                    BpmnParallelGateway parallelGateway = new BpmnParallelGateway("parallelGateway");
                    parallelGateway.marshall(diagram, gateway);
                    this.getParallelGateways().add(parallelGateway);
                } else if (gateway.getGatewayType() == Gateway.GatewayType.INCLUSIVE) {
                    BpmnInclusiveGateway inclusiveGateway = new BpmnInclusiveGateway("inclusiveGateway");
                    inclusiveGateway.marshall(diagram, gateway);
                    this.getInclusiveGateways().add(inclusiveGateway);
                } else if (gateway.getGatewayType() == Gateway.GatewayType.EVENTBASED) {
                    BpmnEventBasedGateway eventBasedGateway = new BpmnEventBasedGateway("eventBasedGateway");
                    eventBasedGateway.marshall(diagram, gateway);
                    this.getEventBasedGateways().add(eventBasedGateway);
                }
            }
        }

    }

    private Object getPrivateField(String fieldName) {
        try {
            Field field = BpmnProcess.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error accessing private field: " + fieldName, e);
        }
    }

    private void callPrivateMethod(String methodName, BPMNDiagram diagram, Swimlane pool) {
        try {
            Class<BpmnProcess> superClass = BpmnProcess.class;
            Method method = superClass.getDeclaredMethod(methodName, BPMNDiagram.class, Swimlane.class);
            method.setAccessible(true);
            method.invoke(this, diagram, pool);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void callPrivateMethod_LaneSet(String methodName, BPMNDiagram diagram, Swimlane pool) {
        try {
            Class<BpmnProcess> superClass = BpmnProcess.class;
            Method method = superClass.getDeclaredMethod(methodName, BPMNDiagram.class, ContainingDirectedGraphNode.class);
            method.setAccessible(true);
            method.invoke(this, diagram, pool);  // 'this' refers to the current instance of SubClass

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void callPrivateMethod(String methodName) {
        try {
            Class<BpmnProcess> superClass = BpmnProcess.class;
            Method method = superClass.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(this);  // 'this' refers to the current instance of SubClass
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    private Collection<BpmnStartEvent> getStartEvents() {
        return (Collection<BpmnStartEvent>) getPrivateField("startEvents");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnTask> getTasks() {
        return (Collection<BpmnTask>) getPrivateField("tasks");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnSubProcess> getSubProcesses() {
        return (Collection<BpmnSubProcess>) getPrivateField("subprocess");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnExclusiveGateway> getExclusiveGateways() {
        return (Collection<BpmnExclusiveGateway>) getPrivateField("exclusiveGateways");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnParallelGateway> getParallelGateways() {
        return (Collection<BpmnParallelGateway>) getPrivateField("parallelGateways");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnInclusiveGateway> getInclusiveGateways() {
        return (Collection<BpmnInclusiveGateway>) getPrivateField("inclusiveGateways");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnEventBasedGateway> getEventBasedGateways() {
        return (Collection<BpmnEventBasedGateway>) getPrivateField("eventBasedGateways");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnCallActivity> getCallActivities() {
        return (Collection<BpmnCallActivity>) getPrivateField("callActivities");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnSequenceFlow> getSequenceFlows() {
        return (Collection<BpmnSequenceFlow>) getPrivateField("sequenceFlows");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnEndEvent> getEndEvents() {
        return (Collection<BpmnEndEvent>) getPrivateField("endEvents");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnIntermediateEvent> getIntermediateEvents() {
        return (Collection<BpmnIntermediateEvent>) getPrivateField("intermediateEvents");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnDataObject> getDataObjects() {
        return (Collection<BpmnDataObject>) getPrivateField("dataObjects");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnDataObjectReference> getDataObjectReferences() {
        return (Collection<BpmnDataObjectReference>) getPrivateField("dataObjectsRefs");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnTextAnnotation> getTextAnnotations() {
        return (Collection<BpmnTextAnnotation>) getPrivateField("textAnnotations");
    }

    @SuppressWarnings("unchecked")
    private Collection<BpmnAssociation> getAssociations() {
        return (Collection<BpmnAssociation>) getPrivateField("associations");
    }

    private BpmnLaneSet getLaneSet() {
        return (BpmnLaneSet) getPrivateField("laneSet");
    }
}
