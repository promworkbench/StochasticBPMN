package org.processmining.stochasticbpmn.plugins;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;

@Plugin(
        name = "Import Stochastic BPMN model from extended BPMN 2.0 file",
        level = PluginLevel.NightlyBuild,
        parameterLabels = {"Filename"},
        returnLabels = {"Stochastic BPMN"},
        returnTypes = {StochasticBpmn.class})
@UIImportPlugin(
        description = "Stochastic BPMN 2.0 files",
        extensions = {"bpmn", "xml"},
        pack = "StochasticBPMN")
public class StochasticBpmnImportPlugin extends AbstractImportPlugin {
    public StochasticBpmnImportPlugin() {
    }

    public StochasticBpmn importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes) throws Exception {
        StochasticBpmn bpmn = this.importBpmnFromStream(context, input, filename, fileSizeInBytes);
        if (bpmn == null) {
            return null;
        } else {
            context.getFutureResult(0).setLabel(filename);
            return bpmn;
        }
    }

    private StochasticBpmn importBpmnFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(input, (String)null);
        int eventType = xpp.getEventType();

        StochasticBpmn bpmn;
        for (bpmn = new StochasticBpmn(); eventType != 2; eventType = xpp.next()) {
        }

        if (xpp.getName().equals(bpmn.tag)) {
            bpmn.importElement(xpp, bpmn);
            System.out.println(bpmn.tag);
        } else {
            bpmn.log(bpmn.tag, xpp.getLineNumber(), "Expected " + bpmn.tag + ", got " + xpp.getName());
        }

        if (bpmn.hasErrors()) {
            context.getProvidedObjectManager().createProvidedObject("Log of BPMN import", bpmn.getLog(), XLog.class, context);
            return null;
        } else {
            return bpmn;
        }
    }
}
