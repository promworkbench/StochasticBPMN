package org.processmining.newpackageivy.models.bpmn.stochastic;

import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.BpmnExclusiveGateway;
import org.processmining.plugins.bpmn.BpmnProcess;
import org.processmining.plugins.bpmn.BpmnSubProcess;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;
import java.util.Collection;

public class StochasticBpmnProcess extends BpmnProcess {
    public StochasticBpmnProcess() {
        super("process");
    }

    protected boolean importElements(XmlPullParser xpp, Bpmn bpmn) {
        if (xpp.getName().equals("exclusiveGateway")) {
            BpmnExclusiveGateway exclusiveGateway = new StochasticBpmnExclusiveGateway();
            exclusiveGateway.importElement(xpp, bpmn);
            System.out.println(exclusiveGateway.tag);
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
        } else return super.importElements(xpp, bpmn);
    }
}
