package org.processmining.stochasticbpmn.algorithms.reader;

import org.processmining.plugins.bpmn.Bpmn;

import java.io.InputStream;

public interface BpmnReader {
    Bpmn read(final InputStream input) throws Exception;
}
