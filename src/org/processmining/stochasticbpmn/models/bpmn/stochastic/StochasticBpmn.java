package org.processmining.stochasticbpmn.models.bpmn.stochastic;


import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.BpmnProcess;
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
}
