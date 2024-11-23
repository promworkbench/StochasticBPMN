package org.processmining.stochasticbpmn.algorithms.reader;

import org.processmining.plugins.bpmn.Bpmn;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;

public class BpmnInputStreamReader implements BpmnReader {
    @Override
    public Bpmn read(InputStream input) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(input, (String) null);
        int eventType = xpp.getEventType();

        final Bpmn bpmn = new Bpmn();
        while (eventType != 2) {
            eventType = xpp.next();
        }

        if (xpp.getName().equals(bpmn.tag)) {
            bpmn.importElement(xpp, bpmn);
        } else {
            bpmn.log(bpmn.tag, xpp.getLineNumber(), "Expected " + bpmn.tag + ", got " + xpp.getName());
        }
        return bpmn;
    }
}
