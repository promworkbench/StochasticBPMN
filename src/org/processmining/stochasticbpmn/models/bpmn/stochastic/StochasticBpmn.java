package org.processmining.stochasticbpmn.models.bpmn.stochastic;


import org.processmining.plugins.bpmn.*;
import org.processmining.plugins.bpmn.diagram.BpmnDiagram;
import org.xmlpull.v1.XmlPullParser;

public class StochasticBpmn extends Bpmn {
    public StochasticBpmn() {
    }

    protected boolean importElements(XmlPullParser xpp, Bpmn bpmn) {
        if (xpp.getName().equals("process")) {
            BpmnProcess process = new StochasticBpmnProcess();
            process.importElement(xpp, bpmn);
            this.processes.add(process);
            return true;
        } else return super.importElements(xpp, bpmn);
    }

    public String exportElements() {
        StringBuilder s = new StringBuilder();
        for (BpmnCollaboration collaboration : collaborations) {
            s.append(collaboration.exportElement());
        }
        for (BpmnResource resource : resources) {
            s.append(resource.exportElement());
        }
        for (BpmnProcess process : processes) {
            StochasticBpmnProcess stochasticProcess = (StochasticBpmnProcess) process;
            s.append(stochasticProcess.exportElement());
        }
        for (BpmnMessage message : messages) {
            s.append(message.exportElement());
        }
        for (BpmnDiagram diagram : diagrams) {
            s.append(diagram.exportElement());
        }
        return s.toString();
    }
}
