package org.processmining.stochasticbpmn.algorithms.reader;

import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.StochasticNet;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.pnml.simple.PNMLRoot;
import org.processmining.stochasticbpmn.models.petrinets.stochastic.StochasticAPN;
import org.processmining.stochasticbpmn.utils.StochasticNetDeserializerWithoutContext;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;

public class StochasticAPNInputStreamReader implements StochasticAPNReader{
    @Override
    public StochasticAPN read(InputStream inputStream) throws Exception {
        Serializer serializer = new Persister();
        PNMLRoot pnml = serializer.read(PNMLRoot.class, inputStream);

        StochasticNetDeserializerWithoutContext converter = new StochasticNetDeserializerWithoutContext();
        Object[] res = converter.convertToNet(pnml, "Input Stream");
        StochasticNet net = (StochasticNet) res[0];
        Marking initialMarking = (Marking) res[1];
        Marking finalMarking = (Marking) res[2];
        if ((initialMarking != null) && (initialMarking.isEmpty())) {
            Place sourcePlace = retrieveSourcePlace(net);
            if (sourcePlace != null) {
                initialMarking.add(sourcePlace);
            }
        }
        return new StochasticAPN(net, initialMarking, finalMarking);
    }

    @Override
    public StochasticAPN read(InputStream inputStream, String label) throws Exception {
        return read(inputStream);
    }


    private static Place retrieveSourcePlace(PetrinetGraph petrinetGraph) {
        for(Place place : petrinetGraph.getPlaces()) {
            if ((petrinetGraph.getInEdges(place) == null) ||
                    (petrinetGraph.getInEdges(place).isEmpty())) {
                return place;
            }
        }
        return null;
    }
}
