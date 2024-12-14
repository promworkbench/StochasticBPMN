package org.processmining.stochasticbpmn;

import org.processmining.stochasticbpmn.algorithms.converter.StochasticAPNToStochasticBPMNConverter;
import org.processmining.stochasticbpmn.algorithms.diagram.reader.StochasticBPMNDiagramFromSPNReader;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.algorithms.reader.StochasticAPNReader;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticBPMNDiagram;
import org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic.StochasticGateway;
import org.processmining.stochasticbpmn.models.petrinets.stochastic.StochasticAPN;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        ObjectReader<File, StochasticBPMNDiagram> reader = StochasticBPMNDiagramFromSPNReader.fromFile();
        for (String logName : new String[]{"road_fines", "sepsis", "bpic-2018-reference"} ) {
            String path = "/home/aleks/Documents/DataResources/ProcessMining/StochasticPetriNets/Tobias/pn-slpn-evaluation-wawe/" + logName + "/hot-start";
            File dir = new File(path);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                System.out.println("Found " + directoryListing.length + " files");
                for (File child : directoryListing) {
                    System.out.println("Start " + child.getAbsolutePath());
                    try {
                        StochasticBPMNDiagram diagram = reader.read(child);
                        System.out.println("Finished " + child.getAbsolutePath() +
                                "\n stochastic gates present " + diagram.getGateways().stream()
                                .filter(g -> g instanceof StochasticGateway).collect(Collectors.toSet()).size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}