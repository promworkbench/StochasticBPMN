package org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic;

import java.util.*;
import java.util.stream.Collectors;

public class StochasticGatewayFlowSet {
    private final Set<String> flows;

    public StochasticGatewayFlowSet(String... flows) {
        this.flows = Arrays.stream(flows).collect(Collectors.toSet());
    }

    public StochasticGatewayFlowSet(Collection<String> flows) {
        this.flows = new HashSet<>(flows);
    }

    public Collection<String> getFlows() {
        return flows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StochasticGatewayFlowSet)) return false;
        StochasticGatewayFlowSet bpmnPaths = (StochasticGatewayFlowSet) o;
        return Objects.equals(flows, bpmnPaths.flows);
    }

    public boolean contains(String id) {
        return flows.contains(id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(flows);
    }
}
