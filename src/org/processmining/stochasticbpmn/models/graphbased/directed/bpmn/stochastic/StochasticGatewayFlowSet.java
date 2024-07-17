package org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StochasticGatewayFlowSet {
    private final Set<String> flows;

    public StochasticGatewayFlowSet(String... flows) {
        this.flows = Arrays.stream(flows).collect(Collectors.toSet());
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

    @Override
    public int hashCode() {
        return Objects.hashCode(flows);
    }
}
