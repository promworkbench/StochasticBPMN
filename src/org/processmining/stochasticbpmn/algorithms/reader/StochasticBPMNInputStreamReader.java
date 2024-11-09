package org.processmining.stochasticbpmn.algorithms.reader;

import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

public class StochasticBPMNInputStreamReader implements StochasticBPMNReader {
    @Override
    public StochasticBpmn read(InputStream input) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(input, (String)null);
        int eventType = xpp.getEventType();

        final StochasticBpmn bpmn = new StochasticBpmn();
        while(eventType != 2) {
            eventType = xpp.next();
        }

        if (xpp.getName().equals(bpmn.tag)) {
            bpmn.importElement(xpp, bpmn);
            System.out.println(bpmn.tag);
        } else {
            bpmn.log(bpmn.tag, xpp.getLineNumber(), "Expected " + bpmn.tag + ", got " + xpp.getName());
        }
        return bpmn;
    }
}
