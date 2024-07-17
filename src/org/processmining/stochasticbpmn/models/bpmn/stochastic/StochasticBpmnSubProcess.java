package org.processmining.stochasticbpmn.models.bpmn.stochastic;

import org.processmining.plugins.bpmn.*;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;
import java.util.Collection;

public class StochasticBpmnSubProcess extends BpmnSubProcess {
    public StochasticBpmnSubProcess() {
        super("subProcess");
    }

    protected boolean importElements(XmlPullParser xpp, Bpmn bpmn) {
        if (xpp.getName().equals("exclusiveGateway")) {
            BpmnExclusiveGateway exclusiveGateway = new StochasticBpmnExclusiveGateway();
            exclusiveGateway.importElement(xpp, bpmn);
            try {
                Field field = BpmnSubProcess.class.getDeclaredField("exclusiveGateways");
                field.setAccessible(true);
                Collection<BpmnExclusiveGateway> exclusiveGateways = (Collection<BpmnExclusiveGateway>) field.get(this);
                exclusiveGateways.add(exclusiveGateway);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                bpmn.log(this.tag, xpp.getLineNumber(), e.getMessage());
            }
            return true;
        } else if (xpp.getName().equals("subProcess")) {
            BpmnSubProcess subProcess = new StochasticBpmnSubProcess();
            subProcess.importElement(xpp, bpmn);
            try {
                Field field = BpmnSubProcess.class.getDeclaredField("subProcesses");
                field.setAccessible(true);
                Collection<BpmnSubProcess> subProcesses = (Collection<BpmnSubProcess>) field.get(this);
                subProcesses.add(subProcess);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                bpmn.log(this.tag, xpp.getLineNumber(), e.getMessage());
            }
            return true;
        } else if (xpp.getName().equals("sequenceFlow")) {
            final BpmnSequenceFlow sequenceFlow = new StochasticBpmnSequenceFlow();
            sequenceFlow.importElement(xpp, bpmn);
            try {
                Field field = BpmnSubProcess.class.getDeclaredField("sequenceFlows");
                field.setAccessible(true);
                Collection<BpmnSequenceFlow> sequenceFlows = (Collection<BpmnSequenceFlow>) field.get(this);
                sequenceFlows.add(sequenceFlow);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                bpmn.log(this.tag, xpp.getLineNumber(), e.getMessage());
            }
            return true;
        } else return super.importElements(xpp, bpmn);
    }
}
